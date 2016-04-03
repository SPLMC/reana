package splSimulator;

import java.util.HashMap;

public class SequenceDiagramElement {

	public static final int LIFELINE = 1;
	public static final int MESSAGE = 2;
	public static final int FRAGMENT = 3;
	public static HashMap<String, SequenceDiagramElement> elements = 
			new HashMap<String, SequenceDiagramElement>();
	
	//Sequence Diagram Element's attributes
	private String name;

	
	public static SequenceDiagramElement createElement(int type, String name) {
		SequenceDiagramElement e = null; 
		switch (type) {
		case LIFELINE:
			e = new Lifeline(name); 
			elements.put(name, e);
			break;

		case MESSAGE:
			e = new Message(name);
			elements.put(name, e);
			break;
			
		case FRAGMENT: 
			e = new Fragment(name); 
			elements.put(name, e);
			
		default:
			break;
		}
		return e;
	} 
	
	public SequenceDiagramElement(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public static SequenceDiagramElement getElementByName(String elementName) {
		return elements.get(elementName);
	}
	
}
