/**
 *
 */
package paramwrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fdtmc.FDTMC;

/**
 * Façade to a PARAM executable.
 *
 * @author Thiago
 *
 */
public class ParamWrapper implements ParametricModelChecker {
	private static final Logger LOGGER = Logger.getLogger(ParamWrapper.class.getName());

	private String paramPath;
	private final String prismPath = "/opt/prism-4.2.1-src/bin/prism";
	private boolean usePrism = false;

	public ParamWrapper(String paramPath) {
		this.paramPath = paramPath;
	}

	public String fdtmcToParam(FDTMC fdtmc) {
		ParamModel model = new ParamModel(fdtmc);
		return model.toString();
	}

	@Override
	public String getReliability(FDTMC fdtmc) {
		String model = fdtmcToParam(fdtmc);
		String reliabilityProperty = "P=? [ F \"success\" ]";

		return evaluate(model, reliabilityProperty);
	}

	private String evaluate(String model, String property) {
		Formula formula = new Formula(this);

		// File modelFile = File.createTempFile("model", "param");
		// FileWriter modelWriter = new FileWriter(modelFile);
		// modelWriter.write(model);
		// modelWriter.flush();
		// modelWriter.close();
		formula.setModelFile("model", "param");

		// File propertyFile = File.createTempFile("property", "prop");
		// FileWriter propertyWriter = new FileWriter(propertyFile);
		// propertyWriter.write(property);
		// propertyWriter.flush();
		// propertyWriter.close();

		formula.setPropertyFile("property", "prop");

		// File resultsFile = File.createTempFile("result", null);
		formula.setResultsFile("result");

		// String formula;
		if (usePrism && !model.contains("param")) {
			// formula = invokeModelChecker(modelFile.getAbsolutePath(),
			// propertyFile.getAbsolutePath(),
			// resultsFile.getAbsolutePath());
			formula.setFormula();
		} else {
			// formula = invokeParametricModelChecker(modelFile.getAbsolutePath(),
			// propertyFile.getAbsolutePath(),
			// resultsFile.getAbsolutePath());
			formula.setParametricFormula();
		}
		return formula.getFormula();

	}

	protected String invokeParametricModelChecker(String modelPath, String propertyPath, String resultsPath)
			throws IOException {
		String commandLine = paramPath + " " + modelPath + " " + propertyPath + " " + "--result-file " + resultsPath;
		return invokeAndGetResult(commandLine, resultsPath + ".out");
	}

	String invokeModelChecker(String modelPath, String propertyPath, String resultsPath) throws IOException {
		String commandLine = prismPath + " " + modelPath + " " + propertyPath + " " + "-exportresults " + resultsPath;
		return invokeAndGetResult(commandLine, resultsPath);
	}

	private String invokeAndGetResult(String commandLine, String resultsPath) throws IOException {
		LOGGER.fine(commandLine);
		Process program = Runtime.getRuntime().exec(commandLine);
		int exitCode = 0;
		try {
			exitCode = program.waitFor();
		} catch (InterruptedException e) {
			LOGGER.severe("Exit code: " + exitCode);
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		List<String> lines = Files.readAllLines(Paths.get(resultsPath), Charset.forName("UTF-8"));
		// Formula
		return lines.get(lines.size() - 1);
	}

}
