package splGenerator.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import splGenerator.Feature;
import splGenerator.FeatureModel;
import splGenerator.SPL;
import splGenerator.SplGenerator;

public class GhezzisSimilarGeneratorTest {

	static SPL spl;
	static FeatureModel fm; 
	static SplGenerator generator;
	
	@BeforeClass
	public static void setUp() {
		generator = SplGenerator.newInstance();
		generator.setNumberOfFeatures(20); 
		generator.setFragmentSize(7); 
		generator.setNumberOfActivities(1); 
		generator.setNumberOfDecisionNodes(0);
		generator.setNumberOfLifelines(10);

		
		
		/**
		 * For the generation of SPL's models, we must first define the 
		 * parameters of the models (according to its dimensions) and 
		 * them call the method generateSPL passing as an argument the 
		 * method which will be used for models generation. 
		 */
		SPL spl = generator.generateSPL(SplGenerator.GHEZZIGENERATOR);
		
		/**
		 * For the generation of SPL's models, we must first generate the
		 * Feature Model and then we should create the Behavioral Models
		 */
//		fm = generator.generateFeatureModel(SplGenerator.GHEZZIGENERATOR); 
//		spl = generator.generateBehavioralModel(fm);
//		fm.persistXMLFile();
	}
	
	@Test
	public void testNumberOfActivitiesEqualsToOne() {
//		fm.exportXML();  
		Feature root = fm.getRoot(); 
		System.out.println("Feature " + root.getName() + " abstract? " + root.isAbstract());
		System.out.println(spl.getXmlRepresentation().toString());
	}
	
	
	@Test
	public void testNumberOfDecisionNodesEqualsToZero() {
		fail("Not yet implemented");
	}
	
	
	@Test
	public void testNumberOfFeaturesInFeatureModel() {
		fail("Not yet implemented");
	}
	
	
	@Test
	public void testNumberOfMessagesByFragment() {
		fail("Not yet implemented");
	}
	
	
	@Test
	public void testGeneratedBehavioralModels() {
		fail("Not yet implemented");
	}
	
	
	@Test
	public void testGeneratedFeatureModels() {
		fail("Not yet implemented");
	}
	
	
	@Test
	public void testGeneratedConfigurationKnowledge() {
		fail("Not yet implemented");
	}

}
