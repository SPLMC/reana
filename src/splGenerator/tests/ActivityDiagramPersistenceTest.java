package splGenerator.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import splGenerator.Activity;
import splGenerator.ActivityDiagram;
import splGenerator.ActivityDiagramElement;
import splGenerator.SPL;
import splGenerator.Transition;

public class ActivityDiagramPersistenceTest {

	SPL spl; 
	public static String expectedHeader = "<?xml version=\"1.0\" "
			+ "encoding=\"UTF-8\" standalone=\"no\"?>"; 
	
	
	@Before
	public void setUp() {
		//Creating a simple software product line
		spl = SPL.createSPL("spl1");
	}
	
	
	
	@Test 
	public void testCreateSplWithOneActivDiagramAndOneActivity() {
		ActivityDiagram ad = spl.createActivityDiagram("ad1");
		ActivityDiagramElement init = ad.getStartNode();
		ActivityDiagramElement act = ActivityDiagramElement.createElement(
				ActivityDiagramElement.ACTIVITY, "Activity 1");
		ActivityDiagramElement end = ActivityDiagramElement.createElement(
				ActivityDiagramElement.END_NODE, "End Node"); 

		//Creating the structure of Activity Diagram
		ad.addElement(act);
		ad.addElement(end); 
		init.createTransition(act, "transition 1", 1); 
		act.createTransition(end, "transition 2", 1); 
		
//		System.out.println(spl.getXmlRepresentation());
	}
	
	
	@Test 
	public void testCreateSplWithActivityDiagramAndVariousElements() {
		ActivityDiagramElement act1, act2, act3, act4, act5, act6;
		ActivityDiagramElement dec1, mer1;
		ActivityDiagramElement endNode;
		Transition t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11;
		
		//Creating the activities, decision and merge nodes
		ActivityDiagram ad = spl.createActivityDiagram("ad1");
		ActivityDiagramElement init = ad.getStartNode();
		init.setElementName("Start node 1");
		act1 = ActivityDiagramElement.createElement(
				ActivityDiagramElement.ACTIVITY, "Activity 1");
		act2 = ActivityDiagramElement.createElement(
				ActivityDiagramElement.ACTIVITY, "Activity 2");
		act3 = ActivityDiagramElement.createElement(
				ActivityDiagramElement.ACTIVITY, "Activity 3");
		act4 = ActivityDiagramElement.createElement(
				ActivityDiagramElement.ACTIVITY, "Activity 4");
		act5 = ActivityDiagramElement.createElement(
				ActivityDiagramElement.ACTIVITY, "Activity 5");
		act6 = ActivityDiagramElement.createElement(
				ActivityDiagramElement.ACTIVITY, "Activity 6");
		dec1 = ActivityDiagramElement.createElement(
				ActivityDiagramElement.DECISION_NODE, "Decision Node 1");
		mer1 = ActivityDiagramElement.createElement(
				ActivityDiagramElement.MERGE_NODE, "Merge Node 1");
		endNode = ActivityDiagramElement.createElement(
				ActivityDiagramElement.END_NODE, "Final Node");
		endNode.setElementName("End Node 1");
		
		//adding labels to activities
		
		
		//creating the transitions 
		t1 = init.createTransition(act1, "T1", 1);
		t2 = act1.createTransition(act2, "T2", 1);
		t3 = act2.createTransition(dec1, "T3", 1);
		t4 = dec1.createTransition(act3, "T4", 1);
		t5 = dec1.createTransition(act4, "T5", 1);
		t6 = dec1.createTransition(act5, "T6", 1);
		t7 = act3.createTransition(mer1, "T7", 1);
		t8 = act4.createTransition(mer1, "T8", 1);
		t9 = act5.createTransition(mer1, "T9", 1);
		t10 = mer1.createTransition(act6, "T10", 1);
		t11 = act6.createTransition(endNode, "T11", 1);

		//adding elements to the activity diagram... Should be refactor!
		ad.addElement(act1);
		ad.addElement(act2); 
		ad.addElement(act3); 
		ad.addElement(act4); 
		ad.addElement(act5); 
		ad.addElement(act6); 
		
		ad.addElement(dec1); 
		ad.addElement(mer1); 
		ad.addElement(endNode); 
		
		ad.addElement(t1); 
		ad.addElement(t2); 
		ad.addElement(t3); 
		ad.addElement(t4); 
		ad.addElement(t5); 
		ad.addElement(t6); 
		ad.addElement(t7); 
		ad.addElement(t8); 
		ad.addElement(t9); 
		ad.addElement(t10); 
		ad.addElement(t11); 
		
		
		
		FileReader file;
		try {
			file = new FileReader("/home/andlanna/workspace2/reana/src/"
					+ "splSimulator/tests/SplModelStructure.xml");
		 
			BufferedReader buffer = new BufferedReader(file); 
			assertNotNull(file);
		
			String line = null;
			StringBuilder fileContent = new StringBuilder();
			line = buffer.readLine();
			do {
				fileContent.append(line); 
				line = buffer.readLine();
			} while (line != null); 

			String expected = fileContent.toString().replaceAll("\\s+", ""); 
			
			assertEquals(expected, spl.getXmlRepresentation().replaceAll("\\s+", ""));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		System.out.println(spl.getXmlRepresentation());
	}
	
}
