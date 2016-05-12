package splGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import splar.core.fm.FeatureModel;

public class SPLFilePersistence {

	
	private static String modelsPath = "/home/andlanna/workspace2/reana/src/splGenerator/generatedModels/";
	private static String cnfFilePrefix = "cnf_";
	private static String splName = "spl"; 
	
	public static void FM2JavaCNF(FeatureModel fm) {
		try {
			FileWriter writer = new FileWriter(modelsPath + cnfFilePrefix + splName + ".txt");
			BufferedWriter buffer = new BufferedWriter(writer);
			String fileContent = fm.FM2JavaCNF(); 
			buffer.write(fileContent);
			buffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	

}
