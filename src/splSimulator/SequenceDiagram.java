package splSimulator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class SequenceDiagram {
	
	//Static elements
	private static HashMap<String, SequenceDiagram> sequenceDiagrams = 
			new HashMap<String, SequenceDiagram>(); 
	
	//Sequence diagram attributes
	private String name; 
	private String guardCondition; 
	private HashSet<Lifeline> lifelines; 
	private LinkedList<SequenceDiagramElement> elements;

	public static SequenceDiagram createSequenceDiagram(String name, String guard) {
		SequenceDiagram temp = new SequenceDiagram(name, guard);
		sequenceDiagrams.put(name, temp);
		if (sequenceDiagrams.containsKey(name))
			return temp; 
		else 
			return null;
	}


	public static SequenceDiagram getSequenceDiagramByName (String name) {
		SequenceDiagram s = sequenceDiagrams.get(name);
		return s; 
	}
	
	
	private SequenceDiagram() {
		lifelines = new HashSet<Lifeline>();
		elements = new LinkedList<SequenceDiagramElement>();
	}
	
	private SequenceDiagram(String name, String guard) {
		this();
		this.name = name;
		this.guardCondition = guard;
	}

	
	public void setGuard(String guard) {
		guardCondition = guard;
	}


	public Lifeline createLifeline(String name) {
		Lifeline l = (Lifeline) SequenceDiagramElement.createElement(
				SequenceDiagramElement.LIFELINE, name);
		boolean inserted = lifelines.add(l);
		if (inserted)
			return l;
		else 
			return null;
	}


	public String getName() {
		return name;
	}


	public String getGuardCondition() {
		return guardCondition;
	}


	public boolean containsLifeline(String lifelineName) {
		Iterator<Lifeline> it = lifelines.iterator();
		while (it.hasNext()) {
			Lifeline l = it.next();
			if (l.getName().equals(lifelineName))
				return true;
		}
		return false;
	}


	public Message createMessage(Lifeline source, Lifeline target, int type, 
			String name, double probability) {
		SequenceDiagramElement e = SequenceDiagramElement.createElement(
				SequenceDiagramElement.MESSAGE, name);
		//Check if source and target lifelines are part of the sequence diagram
		if (!lifelines.contains(source))
			lifelines.add(source);
		if (!lifelines.contains(target))
			lifelines.add(target);
		//Create the message object
		Message m = (Message) e;
		m.setType(type);
		m.setSource(source);
		m.setTarget(target);
		m.setProbability(probability);
		
		boolean answer = elements.add(m);
		if (answer)
			return m;
		else
			return null;
	}


	public LinkedList<SequenceDiagramElement> getElements() {
		return elements;
	}


	public Fragment createFragment(int type, String name) {
		SequenceDiagramElement e = SequenceDiagramElement.createElement(
				SequenceDiagramElement.FRAGMENT, name);
		Fragment f = (Fragment) e; 
		f.setType(type);
		
		boolean answer = elements.add(f);
		if (answer) 
			return f;
		else 
			return null;
	}

}
