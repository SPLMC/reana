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
		try {
			File modelFile = prepareFile(model, "model", "param");
			File propertyFile = prepareFile(property, "property", "prop");

			File resultsFile = File.createTempFile("result", null);

			String formula;
			if (usePrism && !model.contains("param")) {
				String command = prismPath + " "
								 + modelFile.getAbsolutePath() + " "
								 + propertyFile.getAbsolutePath() + " "
					 			 + "-exportresults " + resultsFile.getAbsolutePath();
			    formula = invokeAndGetResult(command, resultsFile.getAbsolutePath());
			} else {
				String command = paramPath+" "
								 + modelFile.getAbsolutePath() + " "
								 + propertyFile.getAbsolutePath() + " "
								 + "--result-file " + resultsFile.getAbsolutePath() + ".out";
			    formula = invokeAndGetResult(command, resultsFile.getAbsolutePath());
			}
			return formula.trim().replaceAll("\\s+", "");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return "";
	}
	
	private File prepareFile(String io, String tempPrefix, String tempSuffix) throws IOException {
		File file = File.createTempFile(tempPrefix, tempSuffix);
		FileWriter writer = new FileWriter(file);
		writer.write(io);
		writer.flush();
		writer.close();
		return file;
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
		return lines.get(lines.size()-1);
	}

}
