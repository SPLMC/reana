package paramwrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import com.sun.media.sound.PortMixerProvider;

import java.util.logging.Level;

public class Formula {
	private static final Logger LOGGER = Logger.getLogger(Formula.class.getName());
	private ParamWrapper paramWrapper;
	File modelFile;
	FileWriter modelWriter;
	File propertyFile;
	FileWriter propertyWriter;
	File resultsFile;
	String formula;
	
	public Formula(ParamWrapper paramWrapper) {
		this.paramWrapper = paramWrapper;
	}
	
	public void setModelFile(String model, String param) {
		try {
			modelFile = File.createTempFile(model, param);
			FileWriter modelWriter = new FileWriter(modelFile);
			modelWriter.write(model);
			modelWriter.flush();
			modelWriter.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	public File getModelFile() {
		return modelFile;
	}

	public void setPropertyFile(String property, String prop) {
		try {
			propertyWriter = new FileWriter(propertyFile);
			propertyWriter.write(property);
			propertyWriter.flush();
			propertyWriter.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	public File getPropertyFile() {
		return propertyFile;
	}

	public void setResultsFile(String result) {
		try {
			resultsFile = File.createTempFile(result, null);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	public File getResultsFile() {
		return resultsFile;
	}

	public String getFormula() {
		return formula.trim().replaceAll("\\s+", "");
	}

	public void setFormula() {
		try {
				formula = paramWrapper.invokeModelChecker(modelFile.getAbsolutePath(), propertyFile.getAbsolutePath(),
				resultsFile.getAbsolutePath());
		}catch (IOException e){
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
	
	public void setParametricFormula() {
		try {
				formula = paramWrapper.invokeParametricModelChecker(modelFile.getAbsolutePath(), propertyFile.getAbsolutePath(),
				resultsFile.getAbsolutePath());
		}catch (IOException e){
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}
}
