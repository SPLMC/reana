package splSimulator;

import java.util.Vector;

public class ActivityDiagram {

	/**
	 * An activity diagram is composed of a set of ActivityDiagramElements, 
	 * disposed in a sequential order. 
	 */
	private Vector<ActivityDiagramElement> elements;
	//There is only one startNode at an ActivityDiagram
	private ActivityDiagramElement startNode;  
	
	/**
	 * Method for adding an element into the Activity Diagram, in an sequential
	 * fashion. The element can be a start node, an activity, a transition bet-
	 * ween two elements, a decision node, a merge node and an end node. 
	 * @param e - Activity diagram element being added into the activity diagram
	 * @return True in case the element was successfully added, False in case 
	 * there is already an element equals the element being added. 
	 */
	public boolean addElement(ActivityDiagramElement e) {
		return false;
	}
	
	
	
	
	
	
	
	
	
	public ActivityDiagram() {
		startNode = ActivityDiagramElement.createElement(
				ActivityDiagramElement.START_NODE, 
				null);
		elements = new Vector<ActivityDiagramElement>();
	}









	public ActivityDiagramElement getStartNode() {
		return startNode;
	}
}
