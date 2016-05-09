package splGenerator.tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import splGenerator.Activity;
import splGenerator.ActivityDiagram;
import splGenerator.ActivityDiagramElement;
import splGenerator.SequenceDiagram;

public class SplBehavioralModelTest {

	@Test
	public void testLinkActivityToSingleSequenceDiagram() {
		ActivityDiagram ad = new ActivityDiagram();
		Activity act1 = new Activity("Activity 1");
		ad.addElement(act1);
		SequenceDiagram sd = SequenceDiagram.createSequenceDiagram(
				"Sequence Diagram 1", "true");
		boolean answer = act1.addSequenceDiagram(sd);
		
		assertTrue(answer);
		assertTrue(ad.containsElement("Activity 1"));
		Activity testActivity = ad.getActivityByName("Activity 1");
		assertNotNull(testActivity);
		assertTrue(testActivity.containsSequenceDiagram("Sequence Diagram 1"));
		SequenceDiagram testSeqDiag = testActivity.getSeqDiagByName(
				"Sequence Diagram 1");
		assertNotNull(testSeqDiag);
	}

	
	@Test
	public void testLinkActivityToMoreThanOneSequenceDiagram() {
		ActivityDiagram ad = new ActivityDiagram(); 
		Activity act1 = new Activity("Activity 1");
		SequenceDiagram sd1 = SequenceDiagram.createSequenceDiagram("sd1", "true"), 
				sd2 = SequenceDiagram.createSequenceDiagram("sd2", "true"), 
				sd3 = SequenceDiagram.createSequenceDiagram("sd3", "true"); 
		
		assertTrue(ad.addElement(act1));
		assertTrue(act1.addSequenceDiagram(sd1)); 
		assertTrue(act1.addSequenceDiagram(sd2));
		assertTrue(act1.addSequenceDiagram(sd3));
		
		//Tests for the Activity Diagram
		assertNotNull(ad);
		assertEquals("splSimulator.ActivityDiagram", ad.getClass().getName());
		assertTrue(ad.containsElement("Activity 1")); 
		
		//Tests for Activity 1
		Activity a = ad.getActivityByName("Activity 1"); 
		assertNotNull(a); 
		assertEquals("splSimulator.Activity", a.getClass().getName());
		assertEquals("Activity 1", a.getElementName());
		assertEquals(3, a.getSequenceDiagrams().size(), 0);
		
		SequenceDiagram testSd; 
		Iterator<SequenceDiagram> it = a.getSequenceDiagrams().iterator(); 
		
		testSd = it.next(); 
		assertNotNull(testSd);
		assertEquals("splSimulator.SequenceDiagram", testSd.getClass().getName());
		assertEquals("sd1", testSd.getName());
		assertEquals("true", testSd.getGuardCondition());
		
		testSd = it.next(); 
		assertNotNull(testSd);
		assertEquals("splSimulator.SequenceDiagram", testSd.getClass().getName());
		assertEquals("sd2", testSd.getName());
		assertEquals("true", testSd.getGuardCondition());
		
		testSd = it.next(); 
		assertNotNull(testSd); 
		assertEquals("splSimulator.SequenceDiagram", testSd.getClass().getName());
		assertEquals("sd3", testSd.getName());
		assertEquals("true", testSd.getGuardCondition());
		
	}
	
	
}
