package splSimulator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

	
	public static Element getDOM(Document doc) {
		Element root = doc.createElement("SequenceDiagrams"); 

		//1st step: create the elements related to the lifelines used in all sequence diagrams
		//Create the DOM object representing the lifelines
		Element lifelines = doc.createElement("Lifelines");
		Element fragments = doc.createElement("Fragments");
		Iterator <SequenceDiagramElement> itl = SequenceDiagramElement.elements.values().iterator(); 
		Element life; 
		Element fragment; 
		while (itl.hasNext()) {
			SequenceDiagramElement e = itl.next();
			if (e.getClass().getSimpleName().equals("Lifeline")) {
				Lifeline l = (Lifeline) e;
				life = doc.createElement("Lifeline"); 
				life.setAttribute("name", l.getName());
				life.setAttribute("reliability", Double.toString(l.getReliability()));
				lifelines.appendChild(life);
			}
			if (e.getClass().getSimpleName().equals("Fragment")) {
				Fragment f = (Fragment) e; 
//				fragment = doc.createElement("Fragment");
				fragment = f.getDOM(doc); 
//				fragment.setAttribute("name", f.getName());
//				switch (f.getType()) {
//				case Fragment.ALTERNATIVE:
//					fragment.setAttribute("type", "alternative");
//					break;
//				
//				case Fragment.LOOP:
//					fragment.setAttribute("type", "loop");
//					break;
//				
//				case Fragment.OPTIONAL:
//					fragment.setAttribute("type", "optional");
//					break;
//				
//				case Fragment.PARALLEL:
//					fragment.setAttribute("type", "parallel");
//					break;
//				
//				default:
//					break;
//				}
				fragments.appendChild(fragment);
			}
		}
		
		//2nd step: create the elements related to the fragments present a the sequence diagram 
		
		
		//3rd step: create the elements related to each sequence diagram created for the software product line
		//Create the DOM object representing the set of sequence diagrams
//		Element sequenceDiagrams = doc.createElement("SequenceDiagrams"); 
		Iterator <SequenceDiagram> its = SequenceDiagram.sequenceDiagrams.values().iterator();
		Element sd; 
		while (its.hasNext()) {
			SequenceDiagram s = its.next(); 
			sd = doc.createElement("SequenceDiagram");
			sd.setAttribute("name", s.getName());
			sd.setAttribute("guard", s.getGuardCondition());
			
			Iterator<SequenceDiagramElement> ite = s.elements.iterator(); 
			while(ite.hasNext()) {
				SequenceDiagramElement el = ite.next(); 
				Element e = el.getDOM(doc); 
				sd.appendChild(e);
			}
			
//			sequenceDiagrams.appendChild(sd);
			root.appendChild(sd);
		}
		
		//Linking the DOM objects to the root element
		root.appendChild(lifelines);
		root.appendChild(fragments);
//		root.appendChild(sequenceDiagrams); 
		return root;
	}
}
