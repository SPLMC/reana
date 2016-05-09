package splGenerator.tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import splGenerator.Activity;
import splGenerator.ActivityDiagram;
import splGenerator.ActivityDiagramElement;
import splGenerator.Fragment;
import splGenerator.Message;
import splGenerator.SPL;
import splGenerator.SequenceDiagram;
import splGenerator.SequenceDiagramElement;
import splGenerator.StartNode;
import splGenerator.Transition;

public class ReadSplFromXmlRepresentationTest {

	/**
	 * This test case ensures an XML file can be read accordingly and its 
	 * content can be represented at memory by the following classes: 
	 * - SPL
	 * - ActivityDiagram
	 * - SequenceDiagram
	 * - Lifeline
	 * - Fragment
	 */
	
	@Test
	public void testReadSimpleModel() {
		String fileName = "/home/andlanna/workspace2/reana/src/splSimulator/"
				+ "tests/spl2_Structure.xml"; 
		
		SPL spl = SPL.getSplFromXml(fileName); 
		
		assertNotNull(spl);
		assertEquals("spl2", spl.getName());
		
		ActivityDiagram a = spl.getActivityDiagram();
		assertNotNull(a);
		assertEquals("ad1", a.getName());
		assertEquals(1, a.getSetOfActivities().size()); 
		
		Activity act1 = a.getActivityByName("Activity 1");
		assertNotNull(act1);
		assertEquals("Activity", act1.getClass().getSimpleName());
		assertEquals("Activity 1", act1.getElementName());
		assertEquals(1, act1.getSequenceDiagrams().size(), 0);
		assertNotNull(act1.getSeqDiagByName("simpleDiagram"));
		
		SequenceDiagram s = act1.getSeqDiagByName("simpleDiagram");
		assertNotNull(s);
		assertEquals("simpleDiagram", s.getName()); 
		assertEquals("true", s.getGuardCondition()); 
		
		assertEquals(3, s.getLifelines().size(), 0); 
		assertTrue(s.containsLifeline("Lifeline1")); 
		assertTrue(s.containsLifeline("Lifeline2")); 
		assertTrue(s.containsLifeline("Lifeline3"));
		
		Iterator<SequenceDiagramElement> it = s.getElements().iterator(); 
		

		Message m1 = (Message)it.next(); 
		assertNotNull(m1); 
		assertEquals("Message1", m1.getName()); 
		assertEquals(0.999, m1.getProbability(), 0); 
		assertEquals("Lifeline1", m1.getSource().getName()); 
		assertEquals("Lifeline2", m1.getTarget().getName()); 
		assertEquals(Message.SYNCHRONOUS, m1.getType(), 0);

 
		Message m2 = (Message)it.next();
		assertNotNull(m2); 
		assertEquals("Message2", m2.getName()); 
		assertEquals(0.999, m2.getProbability(), 0); 
		assertEquals("Lifeline2", m2.getSource().getName()); 
		assertEquals("Lifeline3", m2.getTarget().getName()); 
		assertEquals(Message.ASYNCHRONOUS, m2.getType(), 0);
		
 
		Message m3 = (Message)it.next();
		assertNotNull(m3); 
		assertEquals("Message3", m3.getName()); 
		assertEquals(0.999, m3.getProbability(), 0); 
		assertEquals("Lifeline3", m3.getSource().getName()); 
		assertEquals("Lifeline3", m3.getTarget().getName()); 
		assertEquals(Message.SYNCHRONOUS, m3.getType(), 0);
		

		Message m4 = (Message)it.next();
		assertNotNull(m4); 
		assertEquals("reply", m4.getName()); 
		assertEquals(0.999, m4.getProbability(), 0); 
		assertEquals("Lifeline3", m4.getSource().getName()); 
		assertEquals("Lifeline1", m4.getTarget().getName()); 
		assertEquals(Message.REPLY, m4.getType(), 0);	
	}
	

	
	@Test
	public void testReadSeqDiagWithOptionalFragment() {
		String fileName = "/home/andlanna/workspace2/reana/src/splSimulator/"
				+ "tests/spl3_Structure.xml"; 
		
		//Read the XML file and load the SPL's model into memory
		SPL spl = SPL.getSplFromXml(fileName); 
		
		//Ensure the SPL object was created accordingly
		assertNotNull(spl);
		assertEquals("spl3", spl.getName());
		
		//Ensure there is an activity diagram associated with the SPL object.
		ActivityDiagram a = spl.getActivityDiagram();
		assertNotNull(a);
		assertEquals("ad1", a.getName());
		assertEquals(1, a.getSetOfActivities().size()); 
		
		//Ensure the singleton activity was created accordingly and has a sequence
		//diagram associated with it.
		Activity act1 = a.getActivityByName("Activity 1");
		assertNotNull(act1);
		assertEquals("Activity", act1.getClass().getSimpleName());
		assertEquals("Activity 1", act1.getElementName());
		assertEquals(1, act1.getSequenceDiagrams().size(), 0);
		assertNotNull(act1.getSeqDiagByName("seqDiagWithOptionalFragment"));
		
		//Ensure the sequence diagram was created accordingly and its elements 
		//are disposed in the right ordering
		SequenceDiagram s = act1.getSeqDiagByName("seqDiagWithOptionalFragment");
		assertNotNull(s);
		assertEquals("seqDiagWithOptionalFragment", s.getName()); 
		assertEquals("true", s.getGuardCondition()); 

		//Ensure the first message was created accordingly
		Iterator<SequenceDiagramElement> it = s.getElements().iterator(); 
		SequenceDiagramElement e = it.next();
		Message m1 = (Message) e; 
		assertEquals("Message1", m1.getName()); 
		assertEquals(0.999, m1.getProbability(), 0); 
		assertEquals("Lifeline1", m1.getSource().getName()); 
		assertEquals("Lifeline2", m1.getTarget().getName()); 
		assertEquals(Message.SYNCHRONOUS, m1.getType(), 0); 
		
		//Ensure the fragment is created accordingly, has a singleton sequence
		//diagram asssociated with it, and such sequence diagram is created 
		//accordingly
		e = it.next();
		Fragment f1 = (Fragment) e; 
		assertEquals("CombinedFragment1", f1.getName());
		assertEquals(1, f1.getSequenceDiagrams().size(), 0);
		SequenceDiagram sd = f1.getSequenceDiagrams().getFirst(); 
		assertEquals("g", sd.getGuardCondition()); 
		assertEquals("CombinedFragment1 - row1", sd.getName()); 
		Iterator<SequenceDiagramElement> itCf1 = sd.getElements().iterator();
		Message m2 = (Message)itCf1.next();
		assertNotNull(m2);
		assertEquals("Message2", m2.getName()); 
		assertEquals(0.999, m2.getProbability(), 0);
		assertEquals("Lifeline2", m2.getSource().getName());
		assertEquals("Lifeline3", m2.getTarget().getName());
		assertEquals(Message.SYNCHRONOUS, m2.getType(), 0);
		Message r1 = (Message)itCf1.next();
		assertNotNull(r1);
		assertEquals("reply1", r1.getName()); 
		assertEquals(0.999, r1.getProbability(), 0);
		assertEquals("Lifeline3", r1.getSource().getName());
		assertEquals("Lifeline2", r1.getTarget().getName());
		assertEquals(Message.REPLY, r1.getType(), 0);
		assertFalse(itCf1.hasNext()); 
		
		//Verifying if the last message (reply) of the sequence diagram was 
		//created accordingly
		e = it.next(); 
		Message r2 = (Message) e; 
		assertNotNull(r2);
		assertEquals("reply2", r2.getName()); 
		assertEquals(0.999, r2.getProbability(), 0);
		assertEquals("Lifeline2", r2.getSource().getName());
		assertEquals("Lifeline1", r2.getTarget().getName());
		assertEquals(Message.REPLY, r2.getType(), 0);
	}
	
	
	
	@Test
	public void testReadSeqDiagWithAlternativeFragment() {
		String fileName = "/home/andlanna/workspace2/reana/src/splSimulator/"
				+ "tests/spl4_Structure.xml"; 
		
		//Read the XML file and load the SPL's model into memory
		SPL spl = SPL.getSplFromXml(fileName); 
		
		//Ensure the SPL object was created accordingly
		assertNotNull(spl);
		assertEquals("spl4", spl.getName());
		
		//Ensure there is an activity diagram associated with the SPL object.
		ActivityDiagram a = spl.getActivityDiagram();
		assertNotNull(a);
		assertEquals("ad1", a.getName());
		assertEquals(1, a.getSetOfActivities().size());
		
		//Ensure the transitions are created accordingly
		ActivityDiagramElement source = (StartNode) a.getStartNode();
		Transition t1 = source.getTransitionByName("T1");
		assertNotNull(t1);
		assertEquals("T1", t1.getElementName()); 
		assertEquals(1.0, t1.getProbability(), 0); 
		assertEquals("Start node", t1.getSource().getElementName()); 
		assertEquals("Activity 1", t1.getTarget().getElementName()); 
		source = t1.getTarget(); 
		Transition t2 = source.getTransitionByName("T2");
		assertNotNull(source);
		assertNotNull(t2);
		assertEquals("T2", t2.getElementName()); 
		assertEquals(1.0, t2.getProbability(), 0); 
		assertEquals("Activity 1", t2.getSource().getElementName()); 
		assertEquals("End node", t2.getTarget().getElementName()); 
		assertEquals("EndNode", t2.getTarget().getClass().getSimpleName());
		
		//Ensure the singleton activity was created accordingly and has a sequence
		//diagram associated with it.
		Activity act1 = a.getActivityByName("Activity 1");
		assertNotNull(act1);
		assertEquals("Activity", act1.getClass().getSimpleName());
		assertEquals("Activity 1", act1.getElementName());
		assertEquals(1, act1.getSequenceDiagrams().size(), 0);
		assertNotNull(act1.getSeqDiagByName("seqDiagWithAlternativeFragment"));
		
		//Ensure the sequence diagram was created accordingly and its elements 
		//are disposed in the right ordering
		SequenceDiagram s = act1.getSeqDiagByName("seqDiagWithAlternativeFragment");
		assertNotNull(s);
		assertEquals("seqDiagWithAlternativeFragment", s.getName()); 
		assertEquals("true", s.getGuardCondition()); 

		//Ensure the first message was created accordingly
		Iterator<SequenceDiagramElement> it = s.getElements().iterator(); 
		SequenceDiagramElement e = it.next();
		Message m1 = (Message) e; 
		assertEquals("Message 1", m1.getName()); 
		assertEquals(0.999, m1.getProbability(), 0); 
		assertEquals("Lifeline1", m1.getSource().getName()); 
		assertEquals("Lifeline2", m1.getTarget().getName()); 
		assertEquals(Message.SYNCHRONOUS, m1.getType(), 0); 
		
		//Ensure the alternative fragment is created accordingly, has a singleton sequence
		//diagram asssociated with it, and such sequence diagram is created 
		//accordingly
		e = it.next();
		Fragment f1 = (Fragment) e; 
		assertEquals("CombinedFragment1", f1.getName());
		assertEquals(Fragment.ALTERNATIVE, f1.getType(), 0);
		assertEquals(2, f1.getSequenceDiagrams().size(), 0);
		//First sequence Diagram 
		Iterator<SequenceDiagram> itSd = f1.getSequenceDiagrams().iterator(); 
		SequenceDiagram sd = itSd.next(); 
		assertEquals("CombinedFragment1 - option 1", sd.getName());
		assertEquals("g", sd.getGuardCondition()); 
		Iterator<SequenceDiagramElement> itSdOpt1 = sd.getElements().iterator();
		SequenceDiagramElement el = itSdOpt1.next(); 
		assertNotNull(el);
		Message m2 = (Message) el;
		assertEquals("Message 2", m2.getName()); 
		assertEquals(0.999, m2.getProbability(), 0); 
		assertEquals("Lifeline2", m2.getSource().getName()); 
		assertEquals("Lifeline3", m2.getTarget().getName()); 
		assertEquals(Message.SYNCHRONOUS, m2.getType(), 0); 
		el = itSdOpt1.next();
		Message r1 = (Message) el;
		assertEquals("reply 1", r1.getName()); 
		assertEquals(0.999, r1.getProbability(), 0); 
		assertEquals("Lifeline3", r1.getSource().getName()); 
		assertEquals("Lifeline2", r1.getTarget().getName()); 
		assertEquals(Message.REPLY, r1.getType(), 0); 
		//Second sequence Diagram 
		sd = itSd.next(); 
		assertEquals("CombinedFragment1 - option 2", sd.getName());
		assertEquals("h", sd.getGuardCondition()); 
		Iterator<SequenceDiagramElement> itSdOpt2 = sd.getElements().iterator(); 
		el = itSdOpt2.next(); 
		Message m3 = (Message) el; 
		assertNotNull(m3); 
		assertEquals("Message 3", m3.getName()); 
		assertEquals(0.999, m3.getProbability(), 0); 
		assertEquals("Lifeline3", m3.getSource().getName()); 
		assertEquals("Lifeline3", m3.getTarget().getName()); 
		assertEquals(Message.SYNCHRONOUS, m3.getType(), 0);
		
		//Verifying if the last message (reply) of the sequence diagram was 
		//created accordingly
		e = it.next(); 
		assertNotNull(e);
		Message r2 = (Message) e; 
		assertNotNull(r2); 
		assertEquals("reply 2", r2.getName()); 
		assertEquals(0.999, r2.getProbability(), 0); 
		assertEquals("Lifeline2", r2.getSource().getName()); 
		assertEquals("Lifeline1", r2.getTarget().getName()); 
		assertEquals(Message.REPLY, r2.getType(), 0);
	}
	
	
	@Test
	public void testReadSeqDiagWithNestedFragment() {
		String fileName = "/home/andlanna/workspace2/reana/src/splSimulator/"
				+ "tests/spl5_Structure.xml"; 
		
		//Read the XML file and load the SPL's model into memory
		SPL spl = SPL.getSplFromXml(fileName); 
		
		//Ensure the SPL object was created accordingly
		assertNotNull(spl);
		assertEquals("spl5", spl.getName());
		
		//Ensure there is an activity diagram associated with the SPL object.
		ActivityDiagram a = spl.getActivityDiagram();
		assertNotNull(a);
		assertEquals("ad1", a.getName());
		assertEquals(1, a.getSetOfActivities().size());
		
		//Ensure the transitions are created accordingly
		ActivityDiagramElement source = (StartNode) a.getStartNode();
		Transition t1 = source.getTransitionByName("T1");
		assertNotNull(t1);
		assertEquals("T1", t1.getElementName()); 
		assertEquals(1.0, t1.getProbability(), 0); 
		assertEquals("Start node", t1.getSource().getElementName()); 
		assertEquals("Activity 1", t1.getTarget().getElementName()); 
		source = t1.getTarget(); 
		Transition t2 = source.getTransitionByName("T2");
		assertNotNull(source);
		assertNotNull(t2);
		assertEquals("T2", t2.getElementName()); 
		assertEquals(1.0, t2.getProbability(), 0); 
		assertEquals("Activity 1", t2.getSource().getElementName()); 
		assertEquals("End node", t2.getTarget().getElementName()); 
		assertEquals("EndNode", t2.getTarget().getClass().getSimpleName());
		
		//Ensure the singleton activity was created accordingly and has a sequence
		//diagram associated with it.
		Activity act1 = a.getActivityByName("Activity 1");
		assertNotNull(act1);
		assertEquals("Activity", act1.getClass().getSimpleName());
		assertEquals("Activity 1", act1.getElementName());
		assertEquals(1, act1.getSequenceDiagrams().size(), 0);
		assertNotNull(act1.getSeqDiagByName("seqDiagWithNestedFragments"));
		
		//Ensure the sequence diagram was created accordingly and its elements 
		//are disposed in the right ordering
		SequenceDiagram s = act1.getSeqDiagByName("seqDiagWithNestedFragments");
		assertNotNull(s);
		assertEquals("seqDiagWithNestedFragments", s.getName()); 
		assertEquals("true", s.getGuardCondition()); 

		//Ensure the first message was created accordingly
		Iterator<SequenceDiagramElement> it = s.getElements().iterator(); 
		SequenceDiagramElement e = it.next();
		Message m1 = (Message) e; 
		assertEquals("Message1", m1.getName()); 
		assertEquals(0.999, m1.getProbability(), 0); 
		assertEquals("Lifeline1", m1.getSource().getName()); 
		assertEquals("Lifeline2", m1.getTarget().getName()); 
		assertEquals(Message.SYNCHRONOUS, m1.getType(), 0); 
		
		//Ensure the optional fragment is created accordingly, has a singleton sequence
		//diagram asssociated with it, and such sequence diagram is created 
		//accordingly
		e = it.next();
		Fragment f1 = (Fragment) e; 
		assertEquals("CombinedFragment 1", f1.getName());
		assertEquals(Fragment.OPTIONAL, f1.getType(), 0);
		assertEquals(1, f1.getSequenceDiagrams().size(), 0);
		//First sequence Diagram
		Iterator<SequenceDiagram> itSd = f1.getSequenceDiagrams().iterator(); 
		SequenceDiagram sd = itSd.next(); 
		assertEquals("CombinedFragment 1 - option 1", sd.getName());
		assertEquals("G", sd.getGuardCondition()); 
		Iterator<SequenceDiagramElement> itSdOpt1 = sd.getElements().iterator();
		SequenceDiagramElement el = itSdOpt1.next(); 
		assertNotNull(el);
		Message m2 = (Message) el;
		assertEquals("Message2", m2.getName()); 
		assertEquals(0.999, m2.getProbability(), 0); 
		assertEquals("Lifeline2", m2.getSource().getName()); 
		assertEquals("Lifeline4", m2.getTarget().getName()); 
		assertEquals(Message.SYNCHRONOUS, m2.getType(), 0); 
		
		el = itSdOpt1.next();
		Fragment f2 = (Fragment) el;
		assertEquals("CombinedFragment2", f2.getName());
		assertEquals(Fragment.OPTIONAL, f2.getType(), 0);
		assertEquals(1, f2.getSequenceDiagrams().size(), 0); 
		SequenceDiagram f2Opt1 = f2.getSequenceDiagrams().getFirst();
		assertNotNull(f2Opt1);
		Iterator<SequenceDiagramElement> elsF2opt1 = f2Opt1.getElements().iterator();
		SequenceDiagramElement elf2 = elsF2opt1.next();
		assertNotNull(elf2);
		Message m1f2 = (Message) elf2;
		assertEquals("Message3", m1f2.getName());
		assertEquals(0.999, m1f2.getProbability(), 0); 
		assertEquals("Lifeline4", m1f2.getSource().getName());
		assertEquals("Lifeline3", m1f2.getTarget().getName());
		assertEquals(Message.SYNCHRONOUS, m1f2.getType(), 0);
		elf2 = elsF2opt1.next();
		Message r1f2 = (Message) elf2;
		assertEquals("reply1", r1f2.getName());
		assertEquals(0.999, r1f2.getProbability(), 0); 
		assertEquals("Lifeline3", r1f2.getSource().getName());
		assertEquals("Lifeline4", r1f2.getTarget().getName());
		assertEquals(Message.REPLY, r1f2.getType(), 0);
		
		el = itSdOpt1.next();
		Fragment f3 = (Fragment) el;
		assertEquals("CombinedFragment3", f3.getName());
		assertEquals(Fragment.OPTIONAL, f3.getType(), 0);
		assertEquals(1, f3.getSequenceDiagrams().size(), 0); 
		SequenceDiagram f3Opt1 = f3.getSequenceDiagrams().getFirst();
		assertNotNull(f3Opt1);
		assertEquals("CombinedFragment3 - option 1", f3Opt1.getName());
		assertEquals("I", f3Opt1.getGuardCondition());
		Iterator<SequenceDiagramElement> elsF3Opt1 = f3Opt1.getElements().iterator();
		SequenceDiagramElement elf3 = elsF3Opt1.next();
		assertNotNull(elf3);
		Message m4 = (Message) elf3; 
		assertEquals("Message4", m4.getName()); 
		assertEquals(0.999, m4.getProbability(), 0);
		assertEquals("Lifeline3", m4.getSource().getName());
		assertEquals("Lifeline4", m4.getTarget().getName());
		assertEquals(Message.SYNCHRONOUS, m4.getType(), 0);
		elf3 = elsF3Opt1.next(); 
		Message m5 = (Message) elf3; 
		assertEquals("Message5", m5.getName()); 
		assertEquals(0.999, m5.getProbability(), 0);
		assertEquals("Lifeline4", m5.getSource().getName());
		assertEquals("Lifeline4", m5.getTarget().getName());
		assertEquals(Message.SYNCHRONOUS, m5.getType(), 0);
		elf3 = elsF3Opt1.next(); 
		Message r2 = (Message) elf3; 
		assertEquals("reply2", r2.getName()); 
		assertEquals(0.999, r2.getProbability(), 0);
		assertEquals("Lifeline4", r2.getSource().getName());
		assertEquals("Lifeline3", r2.getTarget().getName());
		assertEquals(Message.REPLY, r2.getType(), 0);
		
		
		el = itSdOpt1.next();
		Message r3 = (Message) el; 
		assertEquals("reply3", r3.getName()); 
		assertEquals(0.999, r3.getProbability(), 0);
		assertEquals("Lifeline4", r3.getSource().getName());
		assertEquals("Lifeline2", r3.getTarget().getName());
		assertEquals(Message.REPLY, r3.getType(), 0);
		
		
		e = it.next();
		Message r4 = (Message) e; 
		assertEquals("reply4", r4.getName()); 
		assertEquals(0.999, r4.getProbability(), 0); 
		assertEquals("Lifeline2", r4.getSource().getName()); 
		assertEquals("Lifeline1", r4.getTarget().getName()); 
		assertEquals(Message.REPLY, r4.getType(), 0);

	}
}
