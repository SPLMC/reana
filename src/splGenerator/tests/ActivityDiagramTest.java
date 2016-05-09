package splGenerator.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import splGenerator.ActivityDiagram;
import splGenerator.ActivityDiagramElement;
import splGenerator.MergeNode;
import splGenerator.Transition;


public class ActivityDiagramTest {

	ActivityDiagram ad; 
	
	@Before
	public void setUp() {
		ad = new ActivityDiagram(); 
	}
	
	@Test
	public void testCreateActivityDiagram() { 
		assertNotNull(ad);
		assertNotNull(ad.getStartNode());
		assertEquals("splSimulator.StartNode", ad.getStartNode().getClass().getName());
	}

	
	@Test
	public void testCreateSingleActivityAndTransition() {
		//create the activity
		ActivityDiagramElement act = ActivityDiagramElement.createElement(
				ActivityDiagramElement.ACTIVITY, 
				"Activity 1");
		//Create single transition to an activity
		Transition t = ad.getStartNode().createTransition(act, "Transition 1", 1.0);
		
		//TESTS
		//Ensure t is in source's set of outgoing transitions
		assertTrue(ad.getStartNode().getTransitions().contains(t));
		//Ensute t's source and target are the start node and the recently cre-
		//ted activity
		t = ad.getStartNode().getTransitionByName("Transition 1");
		assertTrue(t.getSource().equals(ad.getStartNode()));
		assertTrue(t.getTarget().equals(act));
		
	}
	
	@Test 
	public void testCreateIsolatedTransition(){
		//set the start node as transition's source
		ActivityDiagramElement source = ad.getStartNode(),
				//create an activity and set it as the transition's target
				target = ActivityDiagramElement.createElement(
						ActivityDiagramElement.ACTIVITY, 
						"Activity 1");
		
		//create a transition and set its source and target elements
		Transition trans = (Transition)ActivityDiagramElement.createElement(
				ActivityDiagramElement.TRANSITION, "Transition 1");
		trans.setSource(source);
		trans.setTarget(target);
		trans.setProbability(1.0);
		
		
		//TESTS
		Transition t = source.getTransitionByName("Transition 1");
		
		//Ensure the transition is in the set of source's outgoing transitions
		assertTrue(source.getTransitions().contains(t));
		assertEquals(1, source.getTransitions().size());		
		assertTrue(trans.getTarget().equals(target));
	}
	
	@Test
	public void testCreateDecisionNodeWith2Transitions() {
		//set the start node as transition's source
		ActivityDiagramElement source = ad.getStartNode(), 
				//create an activity and set it as a transition's target
				target = ActivityDiagramElement.createElement(
						ActivityDiagramElement.ACTIVITY, "Activity 1");
		
		//create a transition from source and target elements
		source.createTransition(target, "Transition 1", 1.0);
		source = target;
		
		//create a decision node and link it to source element
		target = ActivityDiagramElement.createElement(
				ActivityDiagramElement.DECISION_NODE, 
				"Decision node 1");
		
		//create a transition from source and target elements;
		source.createTransition(target, "Transition 2", 1.0);
		source = target; 
		
		ActivityDiagramElement act2 = ActivityDiagramElement.createElement(
						ActivityDiagramElement.ACTIVITY, "Activity 2"
						), 
				act3 = ActivityDiagramElement.createElement(
						ActivityDiagramElement.ACTIVITY, "Activity 3"
						), 
				act4 = ActivityDiagramElement.createElement(
						ActivityDiagramElement.ACTIVITY, "Activity 4"
						);
		source.createTransition(act2, "Transition 3", 0.25);
		source.createTransition(act3, "Transition 4", 0.25);
		source.createTransition(act4, "Transition 5", 0.50);
		
		//Create an merge node and transitions from activities 2, 3 and 4.
		ActivityDiagramElement n = ActivityDiagramElement.createElement(
				ActivityDiagramElement.MERGE_NODE, "Merge node 1");
		act2.createTransition(n, "Transition 6", 1.0);
		act3.createTransition(n, "Transition 7", 1.0);
		act4.createTransition(n, "Transition 8", 1.0);
		
		ActivityDiagramElement act8 = ActivityDiagramElement.createElement(
				ActivityDiagramElement.ACTIVITY, "Activity 8");
		n.createTransition(act8, "Transition 9", 1.0);
		
		ActivityDiagramElement fn = ActivityDiagramElement.createElement(
				ActivityDiagramElement.END_NODE, "End node 1");
		act8.createTransition(fn, "Transition 10", 1.0);
		
		//TESTS
		ActivityDiagramElement e = ad.getStartNode();
		assertTrue(e.getLabels().contains("init"));
		assertEquals(1, e.getTransitions().size());
		Transition t = e.getTransitionByName("Transition 1");
		assertNotNull(t);
		e = t.getTarget();
		assertNotNull(e); 
		assertEquals("splSimulator.Activity", e.getClass().getName());
		assertEquals("Activity 1", e.getElementName());
		assertEquals(1, e.getTransitions().size());
		
		t = e.getTransitionByName("Transition 2"); 
		assertNotNull(t);
		e = t.getTarget();
		assertEquals("splSimulator.DecisionNode", e.getClass().getName());
		assertEquals(3, e.getTransitions().size());
		
		//Tests for transition 3
		t = e.getTransitionByName("Transition 3"); 
		assertNotNull(t);
		assertEquals("splSimulator.Activity", t.getTarget().getClass().getName());
		assertEquals("Activity 2", t.getTarget().getElementName());
		
		//Tests for transition 4
		t = e.getTransitionByName("Transition 4");
		assertNotNull(t); 
		assertEquals("splSimulator.Activity", t.getTarget().getClass().getName());
		assertEquals("Activity 3", t.getTarget().getElementName());
		
		//Tests for transition 5
		t = e.getTransitionByName("Transition 5");
		assertNotNull(t);
		assertEquals("splSimulator.Activity", t.getTarget().getClass().getName());
		assertEquals("Activity 4", t.getTarget().getElementName());
		
		//Tests for act2
		t = act2.getTransitionByName("Transition 6");
		assertEquals(1, act2.getTransitions().size());
		assertNotNull(t);
		assertEquals("splSimulator.MergeNode", t.getTarget().getClass().getName());
		assertEquals("Merge node 1", t.getTarget().getElementName());
		
		//Tests for act3
		t = act3.getTransitionByName("Transition 7");
		assertEquals(1, act3.getTransitions().size());
		assertNotNull(t);
		assertEquals("splSimulator.MergeNode", t.getTarget().getClass().getName());
		assertEquals("Merge node 1", t.getTarget().getElementName());
		
		//Tests for act 4
		t = act4.getTransitionByName("Transition 8"); 
		assertEquals(1, act4.getTransitions().size());
		assertNotNull(t);
		assertEquals("splSimulator.MergeNode", t.getTarget().getClass().getName());
		assertEquals("Merge node 1", t.getTarget().getElementName());
		
		//Tests for merge node
		t = n.getTransitionByName("Transition 9"); 
		assertEquals(1, n.getTransitions().size());
		assertNotNull(t);
		assertEquals("splSimulator.Activity", t.getTarget().getClass().getName());
		assertEquals("Activity 8", t.getTarget().getElementName());
		
		//Tests for act8
		t = act8.getTransitionByName("Transition 10");
		assertEquals(1, act8.getTransitions().size());
		assertNotNull(t);
		assertEquals("splSimulator.EndNode", t.getTarget().getClass().getName());
		assertEquals("End node", t.getTarget().getElementName());
		assertTrue(t.getTarget().getLabels().contains("success"));
	}
}
