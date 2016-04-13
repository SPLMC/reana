package splSimulator;

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
}
