package splGenerator.tests;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;

import splGenerator.Fragment;
import splGenerator.Lifeline;
import splGenerator.Message;
import splGenerator.SequenceDiagram;
import splGenerator.SequenceDiagramElement;

public class SequenceDiagramTest {

	@Test
	public void testCreateSimpleSequenceDiagram() {
		SequenceDiagram sd = SequenceDiagram.createSequenceDiagram("sd1", "true");
		Lifeline l1, l2, l3;
		Message m1, m2, m3, m4, m5; 
		
		sd.setGuard("true");
		//Create lifelines used by sequence diagram
		l1 = sd.createLifeline("Lifeline 1");
		l2 = sd.createLifeline("Lifeline 2");
		l3 = sd.createLifeline("Lifeline 3");
		
		//Create the messages between lifelines
		m1 = sd.createMessage(l1, l2, Message.SYNCHRONOUS, "m1", 0.999);
		m2 = sd.createMessage(l2, l2, Message.SYNCHRONOUS, "m2", 0.999);
		m3 = sd.createMessage(l2, l3, Message.ASYNCHRONOUS, "m3", 0.999);
		m4 = sd.createMessage(l3, l2, Message.REPLY, "m4", 0.999);
		m5 = sd.createMessage(l2, l1, Message.REPLY, "m5", 0.999);
		
		//Tests. 
		//Test if the sequence diagram is recoverable by its name 
		sd = SequenceDiagram.getSequenceDiagramByName("sd1");
		assertNotNull(sd);
		assertEquals("sd1", sd.getName());
		assertEquals("true", sd.getGuardCondition());
		
		//Test if the lifelines are being stored accordingly in the SequenceDiagram
		//object
		/*Ensure
		 * The lifelines are stored into the "dictionary" of SDs elements
		 * The SD object contains the lifelines created
		 */
		assertTrue(sd.containsLifeline("Lifeline 1"));
		assertTrue(sd.containsLifeline("Lifeline 2"));
		assertTrue(sd.containsLifeline("Lifeline 3"));
		SequenceDiagramElement e = SequenceDiagramElement.getElementByName(
				"Lifeline 1");
		assertNotNull(e);
		assertEquals("Lifeline 1", e.getName());
		assertEquals("splSimulator.Lifeline", e.getClass().getName());
		e = SequenceDiagramElement.getElementByName("Lifeline 2");
		assertNotNull(e);
		assertEquals("Lifeline 2", e.getName());
		assertEquals("splSimulator.Lifeline", e.getClass().getName());
		e = SequenceDiagramElement.getElementByName("Lifeline 3");
		assertNotNull(e);
		assertEquals("Lifeline 3", e.getName());
		assertEquals("splSimulator.Lifeline", e.getClass().getName());
		
		//Ensure the sequence diagram elements are disposed in the order they 
		//were added to the sequence diagram's object.
		sd = SequenceDiagram.getSequenceDiagramByName("sd1");
		LinkedList <SequenceDiagramElement> els = sd.getElements();
		Iterator <SequenceDiagramElement> it = els.iterator();
		
		Message m; 
		//Message 1
		e = it.next(); 
		m = (Message)e;
		assertNotNull(m);
		assertEquals("splSimulator.Message", m.getClass().getName());
		assertEquals("m1", m.getName());
		assertEquals(Message.SYNCHRONOUS, m.getType());
		assertEquals(0.999, m.getProbability(), 0);
		assertEquals("splSimulator.Lifeline", m.getSource().getClass().getName());
		assertEquals("Lifeline 1", m.getSource().getName());
		assertEquals("splSimulator.Lifeline", m.getTarget().getClass().getName());
		assertEquals("Lifeline 2", m.getTarget().getName());
		
		//Message 2
		e = it.next(); 
		m = (Message)e;
		assertNotNull(m);
		assertEquals("splSimulator.Message", m.getClass().getName());
		assertEquals("m2", m.getName());
		assertEquals(Message.SYNCHRONOUS, m.getType());
		assertEquals(0.999, m.getProbability(), 0);
		assertEquals("splSimulator.Lifeline", m.getSource().getClass().getName());
		assertEquals("Lifeline 2", m.getSource().getName());
		assertEquals("splSimulator.Lifeline", m.getTarget().getClass().getName());
		assertEquals("Lifeline 2", m.getTarget().getName());
		
		//Message 3
		e = it.next(); 
		m = (Message)e;
		assertNotNull(m);
		assertEquals("splSimulator.Message", m.getClass().getName());
		assertEquals("m3", m.getName());
		assertEquals(Message.ASYNCHRONOUS, m.getType());
		assertEquals(0.999, m.getProbability(), 0);
		assertEquals("splSimulator.Lifeline", m.getSource().getClass().getName());
		assertEquals("Lifeline 2", m.getSource().getName());
		assertEquals("splSimulator.Lifeline", m.getTarget().getClass().getName());
		assertEquals("Lifeline 3", m.getTarget().getName());
		
		//Message 4
		e = it.next(); 
		m = (Message)e;
		assertNotNull(m);
		assertEquals("splSimulator.Message", m.getClass().getName());
		assertEquals("m4", m.getName());
		assertEquals(Message.REPLY, m.getType());
		assertEquals(0.999, m.getProbability(), 0);
		assertEquals("splSimulator.Lifeline", m.getSource().getClass().getName());
		assertEquals("Lifeline 3", m.getSource().getName());
		assertEquals("splSimulator.Lifeline", m.getTarget().getClass().getName());
		assertEquals("Lifeline 2", m.getTarget().getName());
		
		//Message 5
		e = it.next(); 
		m = (Message)e;
		assertNotNull(m);
		assertEquals("splSimulator.Message", m.getClass().getName());
		assertEquals("m5", m.getName());
		assertEquals(Message.REPLY, m.getType());
		assertEquals(0.999, m.getProbability(), 0);
		assertEquals("splSimulator.Lifeline", m.getSource().getClass().getName());
		assertEquals("Lifeline 2", m.getSource().getName());
		assertEquals("splSimulator.Lifeline", m.getTarget().getClass().getName());
		assertEquals("Lifeline 1", m.getTarget().getName());
	}
	
	
	@Test
	public void testCreateSequenceDIagramWithCombinedFragment(){
		/**
		 * Instructions for building the whole sequence diagram 
		 */
		SequenceDiagram sd = SequenceDiagram.createSequenceDiagram("sd1", "true");
		Lifeline l1, l2, l3, l4; 
		Message m1, m2, m3, m4, m5, m6, m7, m8, m9;
		Fragment f1, f2; 
		
		l1 = sd.createLifeline("Lifeline 1"); 
		l2 = sd.createLifeline("Lifeline 2"); 
		l3 = sd.createLifeline("Lifeline 3"); 
		l4 = sd.createLifeline("Lifeline 4"); 
		
		//Creation of ordered sequence diagram's elements
		m1 = sd.createMessage(l1, l2, Message.SYNCHRONOUS, "m1", 0.999);
		f1 = sd.createFragment(Fragment.PARALLEL, "fragment 1");
		SequenceDiagram f1r1 = f1.addSequenceDiagram("row 1", "true");
		m2 = f1r1.createMessage(l2, l3, Message.SYNCHRONOUS, "m2", 0.999);
		m3 = f1r1.createMessage(l3, l2, Message.REPLY, "m3", 0.999);
		m4 = sd.createMessage(l3, l3, Message.SYNCHRONOUS, "m4", 0.999);
		f2 = sd.createFragment(Fragment.OPTIONAL, "fragment 2"); 
		SequenceDiagram f2r1 = f2.addSequenceDiagram("row 1", "true");
		m5 = f2r1.createMessage(l2, l3, Message.ASYNCHRONOUS, "m5", 0.999);
		m6 = f2r1.createMessage(l3, l4, Message.SYNCHRONOUS, "m6", 0.999);
		m7 = f2r1.createMessage(l4, l2, Message.REPLY, "m7", 0.999);
		m8 = sd.createMessage(l2, l1, Message.REPLY, "m8", 0.999); 
		
		
		
		//Ensure the sequence diagram is not null and its name and presence
		//condition are correct.
		assertNotNull (sd);
		assertEquals("splSimulator.SequenceDiagram", sd.getClass().getName());
		assertEquals("sd1", sd.getName());
		assertEquals("true", sd.getGuardCondition());
		
		SequenceDiagramElement test; 
		//Ensure the lifelines were created accordingly and they are stored in
		//the sequence diagram object and at the set of SD elements
		assertNotNull(l1); 
		assertTrue(sd.containsLifeline("Lifeline 1")); 
		test = SequenceDiagramElement.getElementByName("Lifeline 1");
		assertNotNull(test);
		assertEquals("splSimulator.Lifeline", test.getClass().getName());
		
		assertNotNull(l2);
		assertTrue(sd.containsLifeline("Lifeline 2"));
		test = SequenceDiagramElement.getElementByName("Lifeline 2");
		assertNotNull(test); 
		assertEquals("splSimulator.Lifeline", test.getClass().getName());
		
		assertNotNull(l3);
		assertTrue(sd.containsLifeline("Lifeline 3"));
		test = SequenceDiagramElement.getElementByName("Lifeline 3");
		assertNotNull(test); 
		assertEquals("splSimulator.Lifeline", test.getClass().getName());
		
		assertNotNull(l4);
		assertTrue(sd.containsLifeline("Lifeline 4"));
		test = SequenceDiagramElement.getElementByName("Lifeline 4");
		assertNotNull(test); 
		assertEquals("splSimulator.Lifeline", test.getClass().getName());
		
		
		//Ensure the sequence diagram elements are disposed according the order
		//they were created. 
		Iterator<SequenceDiagramElement> it = sd.getElements().iterator(); 
		test = it.next(); 
		
		//tests for m1
		assertNotNull(test); 
		assertEquals("splSimulator.Message", test.getClass().getName());
		assertEquals("m1", test.getName()); 
		assertEquals("Lifeline 1", ((Message)test).getSource().getName());
		assertEquals("Lifeline 2", ((Message)test).getTarget().getName());
		assertEquals(0.999,((Message)test).getProbability(), 0);
		assertEquals(Message.SYNCHRONOUS, ((Message)test).getType(), 0);
		
		//tests for f1
		test = it.next();
		assertNotNull(f1);
		assertEquals("splSimulator.Fragment", test.getClass().getName());
		assertEquals("fragment 1", test.getName());
		assertEquals(Fragment.PARALLEL, ((Fragment)test).getType());
		assertEquals(1, f1.getSequenceDiagrams().size(), 0);
		assertEquals("fragment 1 - row 1", f1.getSequenceDiagrams().getFirst().getName());
		assertEquals("true", f1.getSequenceDiagrams().getFirst().getGuardCondition());
		assertTrue(f1.getSequenceDiagrams().getFirst().containsLifeline("Lifeline 2"));
		assertTrue(f1.getSequenceDiagrams().getFirst().containsLifeline("Lifeline 3"));
		Iterator<SequenceDiagramElement> itMessage = f1.getSequenceDiagrams().
				getFirst().getElements().iterator();
		SequenceDiagramElement e = itMessage.next();
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m2", ((Message)e).getName());
		assertEquals(Message.SYNCHRONOUS, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 3"), 
				((Message)e).getTarget());
		e = itMessage.next(); 
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m3", ((Message)e).getName());
		assertEquals(Message.REPLY, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 3"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				((Message)e).getTarget());
		
		
		//tests for m4
		test = it.next();
		assertNotNull(test); 
		assertEquals("splSimulator.Message", test.getClass().getName());
		assertEquals("m4", test.getName()); 
		assertEquals("Lifeline 3", ((Message)test).getSource().getName());
		assertEquals("Lifeline 3", ((Message)test).getTarget().getName());
		assertEquals(0.999,((Message)test).getProbability(), 0);
		assertEquals(Message.SYNCHRONOUS, ((Message)test).getType(), 0);
		
		
		//tests for f2
		test = it.next();
		assertNotNull(f2);
		assertEquals("splSimulator.Fragment", test.getClass().getName());
		assertEquals("fragment 2", test.getName());
		assertEquals(Fragment.OPTIONAL, ((Fragment)test).getType());
		assertEquals(1, f2.getSequenceDiagrams().size(), 0);
		assertEquals("fragment 2 - row 1", f2.getSequenceDiagrams().getFirst().getName());
		assertEquals("true", f2.getSequenceDiagrams().getFirst().getGuardCondition());
		assertTrue(f2.getSequenceDiagrams().getFirst().containsLifeline("Lifeline 2"));
		assertTrue(f2.getSequenceDiagrams().getFirst().containsLifeline("Lifeline 3"));
		assertTrue(f2.getSequenceDiagrams().getFirst().containsLifeline("Lifeline 4"));
		itMessage = f2.getSequenceDiagrams().getFirst().getElements().iterator();
		e = itMessage.next();
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m5", ((Message)e).getName());
		assertEquals(Message.ASYNCHRONOUS, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 3"), 
				((Message)e).getTarget());
		e = itMessage.next(); 
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m6", ((Message)e).getName());
		assertEquals(Message.SYNCHRONOUS, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 3"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 4"), 
				((Message)e).getTarget());
		e = itMessage.next(); 
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m7", ((Message)e).getName());
		assertEquals(Message.REPLY, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 4"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				((Message)e).getTarget());
		
		
		//tests for m8
		test = it.next();
		assertNotNull(test); 
		assertEquals("splSimulator.Message", test.getClass().getName());
		assertEquals("m8", test.getName()); 
		assertEquals("Lifeline 2", ((Message)test).getSource().getName());
		assertEquals("Lifeline 1", ((Message)test).getTarget().getName());
		assertEquals(0.999,((Message)test).getProbability(), 0);
		assertEquals(Message.REPLY, ((Message)test).getType(), 0);
	}
	
	
	
	@Test
	public void testAltLoopAndOptNestedFragments() {
		SequenceDiagram sd; 
		Lifeline l1, l2, l3; 
		Message m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11; 
		Fragment f1, f2, f3, f4;
		SequenceDiagram f1r1, f1r2, f1r3; 
		SequenceDiagram f2r1;
		SequenceDiagram f3r1; 
		SequenceDiagram f4r1; 
		
		//Creating the sequence diagram elements of the sequence diagram
		sd = SequenceDiagram.createSequenceDiagram("sd1", "true");
		l1 = sd.createLifeline("Lifeline 1");
		l2 = sd.createLifeline("Lifeline 2");
		l3 = sd.createLifeline("Lifeline 3");
		
		m1 = sd.createMessage(l1, l3, Message.ASYNCHRONOUS, "m1", 0.999);
		f1 = sd.createFragment(Fragment.ALTERNATIVE, "fragment 1");
		f1r1 = f1.addSequenceDiagram("row 1", "p");
		m2 = f1r1.createMessage(l2, l3, Message.ASYNCHRONOUS, "m2", 0.999); 
		m3 = f1r1.createMessage(l3, l2, Message.REPLY, "m3", 0.999);
		f1r2 = f1.addSequenceDiagram("row 2", "q"); 
		m4 = f1r2.createMessage(l2, l2, Message.SYNCHRONOUS, "m4", 0.999);
		f1r3 = f1.addSequenceDiagram("row 3", "else");
		m5 = f1r3.createMessage(l1, l3, Message.SYNCHRONOUS, "m5", 0.999);
		m6 = f1r3.createMessage(l3, l1, Message.REPLY, "m6", 0.999);
		m7 = sd.createMessage(l3, l1, Message.REPLY, "m7", 0.999);
		f2 = sd.createFragment(Fragment.LOOP, "fragment 2"); 
		f2r1 = f2.addSequenceDiagram("row 1", "i<=10");
		m8 = f2r1.createMessage(l2, l2, Message.SYNCHRONOUS, "m8", 0.999);
		f3 = sd.createFragment(Fragment.OPTIONAL, "fragment 3");
		f3r1 = f3.addSequenceDiagram("row 1", "f"); 
		m9 = f3r1.createMessage(l2, l3, Message.ASYNCHRONOUS, "m9", 0.999);
		f4 = f3r1.createFragment(Fragment.OPTIONAL, "fragment 4"); 
		f4r1 = f4.addSequenceDiagram("row 1", "g"); 
		m10 = f4r1.createMessage(l2, l2, Message.SYNCHRONOUS, "m10", 0.999);
		m11 = sd.createMessage(l3, l1, Message.REPLY, "m11", 0.999);
		
		
		//Tests
		//Ensure the sequence diagram is not null and its name and presence
		//condition are correct.
		assertNotNull (sd);
		assertEquals("splSimulator.SequenceDiagram", sd.getClass().getName());
		assertEquals("sd1", sd.getName());
		assertEquals("true", sd.getGuardCondition());
		
		
		SequenceDiagramElement test; 
		//Ensure the lifelines were created accordingly and they are stored in
		//the sequence diagram object and at the set of SD elements
		assertNotNull(l1); 
		assertTrue(sd.containsLifeline("Lifeline 1")); 
		test = SequenceDiagramElement.getElementByName("Lifeline 1");
		assertNotNull(test);
		assertEquals("splSimulator.Lifeline", test.getClass().getName());
		
		assertNotNull(l2);
		assertTrue(sd.containsLifeline("Lifeline 2"));
		test = SequenceDiagramElement.getElementByName("Lifeline 2");
		assertNotNull(test); 
		assertEquals("splSimulator.Lifeline", test.getClass().getName());
		
		assertNotNull(l3);
		assertTrue(sd.containsLifeline("Lifeline 3"));
		test = SequenceDiagramElement.getElementByName("Lifeline 3");
		assertNotNull(test); 
		assertEquals("splSimulator.Lifeline", test.getClass().getName());
		

		//Ensure the sequence diagram elements are disposed according the order
		//they were created. 
		Iterator<SequenceDiagramElement> it = sd.getElements().iterator(); 
		test = it.next(); 
		
		//tests for m1
		assertNotNull(test); 
		assertEquals("splSimulator.Message", test.getClass().getName());
		assertEquals("m1", test.getName()); 
		assertEquals("Lifeline 1", ((Message)test).getSource().getName());
		assertEquals("Lifeline 3", ((Message)test).getTarget().getName());
		assertEquals(0.999,((Message)test).getProbability(), 0);
		assertEquals(Message.ASYNCHRONOUS, ((Message)test).getType(), 0);
		

		//tests for f1
		test = it.next();
		assertNotNull(f1);
		assertEquals("splSimulator.Fragment", test.getClass().getName());
		assertEquals("fragment 1", test.getName());
		assertEquals(Fragment.ALTERNATIVE, ((Fragment)test).getType());
		assertEquals(3, f1.getSequenceDiagrams().size(), 0);
		Iterator<SequenceDiagram> itSd = f1.getSequenceDiagrams().iterator(); 
		SequenceDiagram t = itSd.next(); 
		assertEquals("fragment 1 - row 1", t.getName());
		assertEquals("p", t.getGuardCondition());
		assertTrue(t.containsLifeline("Lifeline 2"));
		assertTrue(t.containsLifeline("Lifeline 3"));
		Iterator<SequenceDiagramElement> itMessage = t.getElements().iterator();
		SequenceDiagramElement e = itMessage.next();
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m2", ((Message)e).getName());
		assertEquals(Message.ASYNCHRONOUS, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 3"), 
				((Message)e).getTarget());
		e = itMessage.next(); 
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m3", ((Message)e).getName());
		assertEquals(Message.REPLY, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 3"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				((Message)e).getTarget());		
		t = itSd.next();
		assertEquals("fragment 1 - row 2", t.getName());
		assertEquals("q", t.getGuardCondition());
		assertTrue(t.containsLifeline("Lifeline 2"));
		itMessage = t.getElements().iterator();
		e = itMessage.next();
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m4", ((Message)e).getName());
		assertEquals(Message.SYNCHRONOUS, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				((Message)e).getTarget());
		t = itSd.next();
		assertEquals("fragment 1 - row 3", t.getName());
		assertEquals("else", t.getGuardCondition());
		assertTrue(t.containsLifeline("Lifeline 1"));
		assertTrue(t.containsLifeline("Lifeline 3"));
		itMessage = t.getElements().iterator();
		e = itMessage.next();
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m5", ((Message)e).getName());
		assertEquals(Message.SYNCHRONOUS, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 1"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 3"), 
				((Message)e).getTarget());
		e = itMessage.next();
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m6", ((Message)e).getName());
		assertEquals(Message.REPLY, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 3"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 1"), 
				((Message)e).getTarget());
		
		
		//tests for m7
		test = it.next(); 
		assertNotNull(test); 
		assertEquals("splSimulator.Message", test.getClass().getName());
		assertEquals("m7", test.getName()); 
		assertEquals("Lifeline 3", ((Message)test).getSource().getName());
		assertEquals("Lifeline 1", ((Message)test).getTarget().getName());
		assertEquals(0.999,((Message)test).getProbability(), 0);
		assertEquals(Message.REPLY, ((Message)test).getType(), 0);

		
		//tests for f2
		test = it.next();
		assertNotNull(f2);
		assertEquals("splSimulator.Fragment", test.getClass().getName());
		assertEquals("fragment 2", test.getName());
		assertEquals(Fragment.LOOP, ((Fragment)test).getType());
		assertEquals(1, f2.getSequenceDiagrams().size(), 0);
		itSd = f2.getSequenceDiagrams().iterator(); 
		t = itSd.next(); 
		assertEquals("fragment 2 - row 1", t.getName());
		assertEquals("i<=10", t.getGuardCondition());
		assertTrue(t.containsLifeline("Lifeline 2"));
		itMessage = t.getElements().iterator();
		e = itMessage.next();
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m8", ((Message)e).getName());
		assertEquals(Message.SYNCHRONOUS, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				((Message)e).getTarget());

		
		//tests for f3
		test = it.next();
		assertNotNull(f3);
		assertEquals("splSimulator.Fragment", test.getClass().getName());
		assertEquals("fragment 3", test.getName());
		assertEquals(Fragment.OPTIONAL, ((Fragment)test).getType());
		assertEquals(1, f3.getSequenceDiagrams().size(), 0);
		itSd = f3.getSequenceDiagrams().iterator(); 
		t = itSd.next(); 
		assertEquals("fragment 3 - row 1", t.getName());
		assertEquals("f", t.getGuardCondition());
		assertTrue(t.containsLifeline("Lifeline 2"));
		assertTrue(t.containsLifeline("Lifeline 3"));
		itMessage = t.getElements().iterator();
		e = itMessage.next();
		assertEquals("splSimulator.Message", ((Message)e).getClass().getName());
		assertEquals("m9", ((Message)e).getName());
		assertEquals(Message.ASYNCHRONOUS, ((Message)e).getType());
		assertEquals(0.999, ((Message)e).getProbability(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				((Message)e).getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 3"), 
				((Message)e).getTarget());
		e = itMessage.next();
		assertNotNull(e);
		assertEquals("splSimulator.Fragment", e.getClass().getName());
		assertEquals("fragment 4", e.getName());
		assertEquals(Fragment.OPTIONAL, ((Fragment)e).getType());
		assertEquals(1, ((Fragment)e).getSequenceDiagrams().size());
		SequenceDiagram s = ((Fragment)e).getSequenceDiagrams().getFirst(); 
		assertNotNull(s); 
		assertEquals("splSimulator.SequenceDiagram", s.getClass().getName());
		assertEquals("fragment 4 - row 1", s.getName());
		assertEquals("g", s.getGuardCondition());
		assertTrue(s.containsLifeline("Lifeline 2"));
		Iterator <SequenceDiagramElement> its = s.getElements().iterator();
		Message m = (Message)its.next();
		assertNotNull(m);
		assertEquals("splSimulator.Message", m.getClass().getName());
		assertEquals("m10", m.getName());
		assertEquals(0.999, m.getProbability(), 0);
		assertEquals(Message.SYNCHRONOUS, m.getType(), 0);
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				m.getSource());
		assertEquals(SequenceDiagramElement.getElementByName("Lifeline 2"), 
				m.getTarget());
		
		//tests for m11
		test = it.next(); 
		assertNotNull(test); 
		assertEquals("splSimulator.Message", test.getClass().getName());
		assertEquals("m11", test.getName()); 
		assertEquals("Lifeline 3", ((Message)test).getSource().getName());
		assertEquals("Lifeline 1", ((Message)test).getTarget().getName());
		assertEquals(0.999,((Message)test).getProbability(), 0);
		assertEquals(Message.REPLY, ((Message)test).getType(), 0);		
	}
}
