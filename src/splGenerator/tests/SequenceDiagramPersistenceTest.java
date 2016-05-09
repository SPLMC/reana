package splGenerator.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import splGenerator.Activity;
import splGenerator.ActivityDiagram;
import splGenerator.ActivityDiagramElement;
import splGenerator.Fragment;
import splGenerator.Lifeline;
import splGenerator.Message;
import splGenerator.SPL;
import splGenerator.SequenceDiagram;
import splGenerator.Transition;

public class SequenceDiagramPersistenceTest {

	@Test
	public void testSimpleDiagram() {
		SPL spl = SPL.createSPL("spl2");
		ActivityDiagramElement init, act1, end;
		Lifeline l1, l2, l3;
		Message m1, m2, m3, m4;
		
		//Creation of the Activity Diagram
		ActivityDiagram ad = spl.getActivityDiagram();
		ad.setName("ad1");
		init = ad.getStartNode();
		act1 = ActivityDiagramElement.createElement(ActivityDiagramElement.ACTIVITY, "Activity 1");
		end = ActivityDiagramElement.createElement(ActivityDiagramElement.END_NODE, "End node");
		Transition t1 = init.createTransition(act1, "T1", 1); 
		Transition t2 = act1.createTransition(end, "T2", 1); 
		ad.addElement(act1);
		ad.addElement(end);
		ad.addElement(t1); 
		ad.addElement(t2); 
		
		//Creation of the sequence diagram
		SequenceDiagram sd = SequenceDiagram.createSequenceDiagram("simpleDiagram", "true");
		l1 = sd.createLifeline("Lifeline1"); 
		l2 = sd.createLifeline("Lifeline2"); 
		l3 = sd.createLifeline("Lifeline3");
		
		m1 = sd.createMessage(l1, l2, Message.SYNCHRONOUS, "Message1", 0.999);
		m2 = sd.createMessage(l2, l3, Message.ASYNCHRONOUS, "Message2", 0.999); 
		m3 = sd.createMessage(l3, l3, Message.SYNCHRONOUS, "Message3", 0.999); 
		m4 = sd.createMessage(l3, l1, Message.REPLY, "reply", 0.999); 
		
		
		//Assigning the sequence diagram to an activity in activity diagram
		Activity a = (Activity)act1;
		a.addSequenceDiagram(sd); 
		
		FileReader file; 
		try {
			file = new FileReader("/home/andlanna/workspace2/reana/src/"
					+ "splSimulator/tests/spl2_Structure.xml");
			BufferedReader buffer = new BufferedReader(file); 
			StringBuilder fileContent = new StringBuilder(); 
			String line = buffer.readLine(); 
			do {
				fileContent.append(line);
				line = buffer.readLine(); 
			} while (line != null);
			
			String expected = fileContent.toString().replaceAll("\\s+", ""); 
			assertEquals(expected, spl.getXmlRepresentation().replaceAll("\\s+", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testSeqDiagWithOptionalFragment() {
		ActivityDiagram ad;
		ActivityDiagramElement init, act1, end;
		Transition t1, t2; 
		
		SequenceDiagram sd;
		Lifeline l1, l2, l3; 
		Message m1, m2, m3, m4;
		Fragment f1; 
		
		//Creating the activity diagram structure
		SPL spl = new SPL("spl3"); 
		//Creating the activity structure
		ad = spl.createActivityDiagram("ad1");
		init = ad.getStartNode();
		act1 = ActivityDiagramElement.createElement(ActivityDiagramElement.ACTIVITY, 
				"Activity 1");
		end = ActivityDiagramElement.createElement(ActivityDiagramElement.END_NODE,
				"End Node 1"); 
		t1 = init.createTransition(act1, "T1", 1);
		t2 = act1.createTransition(end, "T2", 1);
		
		//Add activity diagram's structures into the ad object
		ad.addElement(act1);
		ad.addElement(end); 
		ad.addElement(t1); 
		ad.addElement(t2);
		
		
		
		  
		//Create the sequence diagram structure
		sd = SequenceDiagram.createSequenceDiagram("seqDiagWithOptionalFragment", "true");
		l1 = sd.createLifeline("Lifeline1");
		l2 = sd.createLifeline("Lifeline2"); 
		l3 = sd.createLifeline("Lifeline3");
				
		sd.createMessage(l1, l2, Message.SYNCHRONOUS, "Message1", 0.999);
		f1 = sd.createFragment(Fragment.OPTIONAL, "CombinedFragment1");
		SequenceDiagram sdf = f1.addSequenceDiagram("row1", "g"); 
		sdf.createMessage(l2, l3, Message.SYNCHRONOUS, "Message2", 0.999);
		sdf.createMessage(l3, l2, Message.REPLY, "reply1", 0.999);
		sd.createMessage(l2, l1, Message.REPLY, "reply2", 0.999);
		
		//linking activity act1 to the sequence diagram 
		((Activity)act1).addSequenceDiagram(sd);
		
		//Testing 
		FileReader file; 
		try {
			file = new FileReader("/home/andlanna/workspace2/reana/src/"
					+ "splSimulator/tests/spl3_Structure.xml");
			BufferedReader buffer = new BufferedReader(file);
			StringBuilder fileContent = new StringBuilder(); 
			String line = buffer.readLine(); 
			do {
				fileContent.append(line); 
				line = buffer.readLine();
			} while (line != null);
			
			String expected = fileContent.toString().replaceAll("\\s+", "");
			
			assertEquals(expected, spl.getXmlRepresentation().toString().replaceAll("\\s+", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	
	@Test
	public void testSeqDiagWithAlternativeFragment() {
		ActivityDiagram ad;
		ActivityDiagramElement init, act1, end;
		Transition t1, t2; 
		
		SequenceDiagram sd;
		Lifeline l1, l2, l3; 
		Message m1, m2, m3, m4, m5;
		Fragment f1; 
		
		//Creating the activity diagram structure
		SPL spl = new SPL("spl4"); 
		//Creating the activity structure
		ad = spl.createActivityDiagram("ad1");
		init = ad.getStartNode();
		act1 = ActivityDiagramElement.createElement(ActivityDiagramElement.ACTIVITY, 
				"Activity 1");
		end = ActivityDiagramElement.createElement(ActivityDiagramElement.END_NODE,
				"End Node 1"); 
		t1 = init.createTransition(act1, "T1", 1);
		t2 = act1.createTransition(end, "T2", 1);
		
		//Add activity diagram's structures into the ad object
		ad.addElement(act1);
		ad.addElement(end); 
		ad.addElement(t1); 
		ad.addElement(t2);
		
		  
		//Create the sequence diagram structure
		sd = SequenceDiagram.createSequenceDiagram("seqDiagWithAlternativeFragment", "true");
		l1 = sd.createLifeline("Lifeline1");
		l2 = sd.createLifeline("Lifeline2"); 
		l3 = sd.createLifeline("Lifeline3");
				
		sd.createMessage(l1, l2, Message.SYNCHRONOUS, "Message 1", 0.999); 
		f1 = sd.createFragment(Fragment.ALTERNATIVE, "CombinedFragment1");
		SequenceDiagram r1 = f1.addSequenceDiagram("option 1", "g"); 
		r1.createMessage(l2, l3, Message.SYNCHRONOUS, "Message 2", 0.999); 
		r1.createMessage(l3, l2, Message.REPLY, "reply 1", 0.999); 
		SequenceDiagram r2 = f1.addSequenceDiagram("option 2", "h"); 
		r2.createMessage(l3, l3, Message.SYNCHRONOUS, "Message 3", 0.999); 
		sd.createMessage(l2, l1, Message.REPLY, "reply 2", 0.999); 
		

		//linking activity act1 to the sequence diagram 
		((Activity)act1).addSequenceDiagram(sd);
		
		//Testing 
		FileReader file; 
		try {
			file = new FileReader("/home/andlanna/workspace2/reana/src/"
					+ "splSimulator/tests/spl4_Structure.xml");
			BufferedReader buffer = new BufferedReader(file);
			StringBuilder fileContent = new StringBuilder(); 
			String line = buffer.readLine(); 
			do {
				fileContent.append(line); 
				line = buffer.readLine();
			} while (line != null);
			
			String expected = fileContent.toString().replaceAll("\\s+", "");
			
			assertEquals(expected, spl.getXmlRepresentation().toString().replaceAll("\\s+", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	

	@Test
	public void testSeqDiagWithNestedFragment() {
		ActivityDiagram ad;
		ActivityDiagramElement init, act1, end;
		Transition t1, t2; 
		
		SequenceDiagram sd;
		Lifeline l1, l2, l3, l4; 
		Message m1, m2, m3, m4, m5, m6, m7, m8, m9;
		Fragment f1, f2, f3; 
		
		//Creating the activity diagram structure
		SPL spl = new SPL("spl5"); 
		//Creating the activity structure
		ad = spl.createActivityDiagram("ad1");
		init = ad.getStartNode();
		act1 = ActivityDiagramElement.createElement(ActivityDiagramElement.ACTIVITY, 
				"Activity 1");
		end = ActivityDiagramElement.createElement(ActivityDiagramElement.END_NODE,
				"End Node 1"); 
		t1 = init.createTransition(act1, "T1", 1);
		t2 = act1.createTransition(end, "T2", 1);
		
		//Add activity diagram's structures into the ad object
		ad.addElement(act1);
		ad.addElement(end); 
		ad.addElement(t1); 
		ad.addElement(t2);
		
		  
		//Create the sequence diagram structure
		sd = SequenceDiagram.createSequenceDiagram("seqDiagWithNestedFragments", "true");
		l1 = sd.createLifeline("Lifeline1");
		l2 = sd.createLifeline("Lifeline2");
		m1 = sd.createMessage(l1, l2, Message.SYNCHRONOUS, "Message1", 0.999);
		f1 = sd.createFragment(Fragment.OPTIONAL, "CombinedFragment 1");
		SequenceDiagram sdf1 = f1.addSequenceDiagram("option 1", "G");  
		l4 = sdf1.createLifeline("Lifeline4"); 
		m2 = sdf1.createMessage(l2, l4, Message.SYNCHRONOUS, "Message2", 0.999);
		f2 = sdf1.createFragment(Fragment.OPTIONAL, "CombinedFragment2");
		SequenceDiagram sdf2 = f2.addSequenceDiagram("option 1" , "H"); 
		l3 = sdf2.createLifeline("Lifeline3");
		m3 = sdf2.createMessage(l4, l3, Message.SYNCHRONOUS, "Message3", 0.999);
		m4 = sdf2.createMessage(l3, l4, Message.REPLY, "reply1", 0.999); 
		f3 = sdf1.createFragment(Fragment.OPTIONAL, "CombinedFragment3"); 
		SequenceDiagram sdf3 = f3.addSequenceDiagram("option 1", "I"); 
		m5 = sdf3.createMessage(l3, l4, Message.SYNCHRONOUS, "Message4", 0.999);
		m6 = sdf3.createMessage(l4, l4, Message.SYNCHRONOUS, "Message5", 0.999); 
		m7 = sdf3.createMessage(l4, l3, Message.REPLY, "reply2", 0.999);
		sdf1.createMessage(l4, l2, Message.REPLY, "reply3", 0.999);
		sd.createMessage(l2, l1, Message.REPLY, "reply4", 0.999); 
		
		//linking activity act1 to the sequence diagram 
		((Activity)act1).addSequenceDiagram(sd);
		
		
		//Testing
		FileReader file;
		try {
			file = new FileReader("/home/andlanna/workspace2/reana/src/splSimulator/"
					+ "tests/spl5_Structure.xml");
			BufferedReader buffer = new BufferedReader(file);
			StringBuilder fileContent = new StringBuilder();
			String line = buffer.readLine();
			do {
				fileContent.append(line);
				line = buffer.readLine(); 
			} while (line != null); 
				
			
			String expectedAnswer = fileContent.toString();
			assertEquals(expectedAnswer.replaceAll("\\s+", ""),
					spl.getXmlRepresentation().replaceAll("\\s+", "")); 

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
