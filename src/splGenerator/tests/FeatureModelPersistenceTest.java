package splGenerator.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;

import splGenerator.Feature;
import splGenerator.FeatureModel;




public class FeatureModelPersistenceTest {

	public String featureModels[] = new String[6]; 
	
	
	public String readFile(BufferedReader b) {
		StringBuilder sb;
		sb = new StringBuilder();
		String line;
		try {
			line = b.readLine();
			while (line != null) {
			sb.append(line); 
			sb.append("\n"); 
			line = b.readLine();
		}
		b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	@Before
	public void setUp() {
		String fn[]; 
		BufferedReader br[]; 
		
		fn = new String[6];
		fn[0] = System.getProperty("user.dir") + "/src/splSimulator/tests/m1.xml"; 
		fn[1] = System.getProperty("user.dir") + "/src/splSimulator/tests/m2.xml"; 
		fn[2] = System.getProperty("user.dir") + "/src/splSimulator/tests/m3.xml"; 
		fn[3] = System.getProperty("user.dir") + "/src/splSimulator/tests/m4.xml"; 
		fn[4] = System.getProperty("user.dir") + "/src/splSimulator/tests/m5.xml"; 
		fn[5] = System.getProperty("user.dir") + "/src/splSimulator/tests/m6.xml"; 
		
		br = new BufferedReader[6];
		try { 
			br[0] = new BufferedReader(new FileReader(fn[0]));
			br[1] = new BufferedReader(new FileReader(fn[1])); 
			br[2] = new BufferedReader(new FileReader(fn[2]));
			br[3] = new BufferedReader(new FileReader(fn[3]));
			br[4] = new BufferedReader(new FileReader(fn[4]));
			br[5] = new BufferedReader(new FileReader(fn[5]));
		
			featureModels[0] = readFile(br[0]);
			featureModels[1] = readFile(br[1]);
			featureModels[2] = readFile(br[2]);
			featureModels[3] = readFile(br[3]);
			featureModels[4] = readFile(br[4]);
			featureModels[5] = readFile(br[5]);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@Test
	public void testM1() {
		FeatureModel fm = FeatureModel.createFeatureModel("fm1");
		Feature root = fm.getRoot();
//		root.setName("Root");
		root.addChild("f1", Feature.LEAF, !Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		root.setAbstract(true);
		root.setType(Feature.AND);
		
		String xmlFm = fm.exportXML(); 
		assertEquals(featureModels[0].replaceAll("\\s+", ""), 
				xmlFm.replaceAll("\\s+", "")); 
	}
	
	
	@Test
	public void testM2() { 
		FeatureModel fm = FeatureModel.createFeatureModel("fm2"); 
		Feature root = fm.getRoot();
		root.addChild("f1", Feature.LEAF, Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		root.setAbstract(true);
		root.setType(Feature.AND);
		
		String fmXML = fm.exportXML(); 
		assertEquals(featureModels[1].replaceAll("\\s+", ""), 
				fmXML.replaceAll("\\s+", ""));
	}
	
	
	@Test
	public void testM3() {
		FeatureModel fm = FeatureModel.createFeatureModel("fm3");
		Feature root = fm.getRoot();
		root.setType(Feature.AND);
		root.setAbstract(true);
		root.addChild("f1", Feature.LEAF, !Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		root.addChild("f2", Feature.LEAF, !Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		
		String xmlFm = fm.exportXML(); 
		assertEquals(featureModels[2].replaceAll("\\s+", ""), 
				xmlFm.replaceAll("\\s+", ""));
	}
	
	
	@Test
	public void testM4() {
		FeatureModel fm = FeatureModel.createFeatureModel("fm4"); 
		Feature root = fm.getRoot(); 
		root.setType(Feature.ALTERNATIVE);
		root.setAbstract(true);
		root.addChild("f1", Feature.LEAF, Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		root.addChild("f2", Feature.LEAF, Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		
		String xmlFm = fm.exportXML(); 
		assertEquals(featureModels[3].replaceAll("\\s+", ""), 
				xmlFm.replaceAll("\\s+", ""));
	}
	
	
	@Test
	public void testM5() {
		FeatureModel fm = FeatureModel.createFeatureModel("fm5"); 
		Feature root = fm.getRoot();
		root.setType(Feature.OR);
		root.setAbstract(true);
		root.addChild("f1", Feature.LEAF, Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		root.addChild("f2", Feature.LEAF, Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		
		String xmlFm = fm.exportXML(); 
		assertEquals(featureModels[4].replaceAll("\\s+", ""), xmlFm.replaceAll("\\s+", "")); 
	}
	
	
	@Test
	public void testM6() {
		FeatureModel fm = FeatureModel.createFeatureModel("fm6"); 
		Feature root = fm.getRoot(); 
		root.setType(Feature.AND);
		root.setAbstract(true);
		
		Feature f1 = root.addChild("f1", Feature.AND, Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		Feature f3 = f1.addChild("f3", Feature.LEAF, !Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		
		Feature f2 = root.addChild("f2", Feature.ALTERNATIVE, !Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		Feature f4 = f2.addChild("f4", Feature.LEAF, Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		Feature f5 = f2.addChild("f5", Feature.LEAF, Feature.MANDATORY, !Feature.ABSTRACT, !Feature.HIDDEN);
		
		String xmlFm = fm.exportXML(); 
		assertEquals(featureModels[5].replaceAll("\\s+", ""), xmlFm.replaceAll("\\s+", ""));
	}

}
