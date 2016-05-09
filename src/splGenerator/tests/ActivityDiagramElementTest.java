package splGenerator.tests;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import splGenerator.ActivityDiagramElement;

public class ActivityDiagramElementTest {

	@Test
	public void testCreateStartNode() {
		ActivityDiagramElement st = ActivityDiagramElement.createElement(
				ActivityDiagramElement.START_NODE, 
				null);
		Assert.assertTrue(st.getClass().getName().equals("splSimulator.StartNode"));
		Assert.assertEquals("Start node", st.getElementName());
	}
	
	
	@Test
	public void testCreateActivity() {
		ActivityDiagramElement st = ActivityDiagramElement.createElement(
				ActivityDiagramElement.ACTIVITY, 
				"Activity 1");
		Assert.assertTrue(st.getClass().getName().equals("splSimulator.Activity"));
		Assert.assertEquals("Activity 1", st.getElementName());
	}
	
	
	@Test
	public void testCreateTransition() {
		ActivityDiagramElement st = ActivityDiagramElement.createElement(
				ActivityDiagramElement.TRANSITION, 
				"Transition 1");
		Assert.assertTrue(st.getClass().getName().equals("splSimulator.Transition"));
		Assert.assertEquals("Transition 1", st.getElementName());
	}
	
	
	@Test
	public void testCreateDecisionNode() {
		ActivityDiagramElement st = ActivityDiagramElement.createElement(
				ActivityDiagramElement.DECISION_NODE, 
				"Decision node 1");
		Assert.assertTrue(st.getClass().getName().equals("splSimulator.DecisionNode"));
		Assert.assertEquals("Decision node 1", st.getElementName());
	}
	
	
	@Test
	public void testCreateMergeNode() {
		ActivityDiagramElement st = ActivityDiagramElement.createElement(
				ActivityDiagramElement.MERGE_NODE, 
				"Merge node 1");
		Assert.assertTrue(st.getClass().getName().equals("splSimulator.MergeNode"));
		Assert.assertEquals("Merge node 1", st.getElementName());
	}
	
	
	@Test
	public void testCreateEndNode() {
		ActivityDiagramElement st = ActivityDiagramElement.createElement(
				ActivityDiagramElement.END_NODE,
				null);
		Assert.assertTrue(st.getClass().getName().equals("splSimulator.EndNode"));
		Assert.assertEquals("End node", st.getElementName());
	}
}
