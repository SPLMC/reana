package splSimulator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Activity extends ActivityDiagramElement{

	LinkedList<SequenceDiagram> sequenceDiagrams; 
	
	public Activity() {
		super();
		sequenceDiagrams = new LinkedList<SequenceDiagram>();
	}
	
	public Activity(String elementName) {
		super(elementName);
		sequenceDiagrams = new LinkedList<SequenceDiagram>();
	}

	public boolean addSequenceDiagram(SequenceDiagram sd) {
		if (sequenceDiagrams == null)
			sequenceDiagrams = new LinkedList<SequenceDiagram>();
		
		boolean answer = sequenceDiagrams.add(sd); 
		return answer;
	}

	public boolean containsSequenceDiagram(String seqDiagName) { 
		Iterator<SequenceDiagram> itSeqDiag = sequenceDiagrams.iterator();
		SequenceDiagram s; 
		while (itSeqDiag.hasNext()) {
			s = itSeqDiag.next(); 
			if (s.getName().equals(seqDiagName))
				return true; 
		}
		return false;
	}

	public SequenceDiagram getSeqDiagByName(String seqDiagName) {
		Iterator <SequenceDiagram> itSeqDiag = sequenceDiagrams.iterator();
		SequenceDiagram s; 
		
		while (itSeqDiag.hasNext()) {
			s = itSeqDiag.next();
			if (s.getName().equals(seqDiagName))
				return s; 
		}
		return null;
	}
	
	public LinkedList<SequenceDiagram> getSequenceDiagrams() {
		return sequenceDiagrams;
	}


	public Element getDom(Document doc) {
		Element e = super.getDom(doc);

		if (sequenceDiagrams.size() > 0) {
			Iterator<SequenceDiagram> its = sequenceDiagrams.iterator(); 
			Element seqDiag; 
			while (its.hasNext()) {
				SequenceDiagram sd = its.next();
				seqDiag = doc.createElement("RepresentedBy");
				seqDiag.setAttribute("seqDiagName", sd.getName());
				e.appendChild(seqDiag);
			}
		}
		
		return e;
	}

	public HashSet<SequenceDiagram> getTransitiveSequenceDiagram() {
		HashSet<SequenceDiagram> answer = new HashSet<SequenceDiagram>();
		Iterator<SequenceDiagram> its = sequenceDiagrams.iterator(); 
		while (its.hasNext()) {
			SequenceDiagram s = its.next(); 
			answer.add(s);
			HashSet<Fragment> setOfFragments = s.getFragments(); 
			Iterator<Fragment> itf = setOfFragments.iterator(); 
			while (itf.hasNext()) {
				Fragment f = itf.next();
				answer.addAll(f.getTransitiveSequenceDiagram()); 
			}
		}
		answer.addAll(sequenceDiagrams); 
		return answer;
	}

	public HashSet<Lifeline> getTranstiveLifelines() {
		HashSet<Lifeline> answer = new HashSet<Lifeline>(); 
		
		Iterator<SequenceDiagram> it = sequenceDiagrams.iterator(); 
		while (it.hasNext()) {
			SequenceDiagram s = it.next(); 
			answer.addAll(s.getTransitiveLifeline()); 
		}
		
		return answer;
	}

	public HashSet<Fragment> getTransitiveFragments() {
		HashSet<Fragment> answer = new HashSet<Fragment>(); 
		Iterator<SequenceDiagram> itsd = sequenceDiagrams.iterator(); 
		while (itsd.hasNext()) {
			SequenceDiagram s = itsd.next();
			answer.addAll(s.getTransitiveFragments()); 
		}
		
		
		return answer;
	}	
}
