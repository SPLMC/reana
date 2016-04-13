package splSimulator;

import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Fragment extends SequenceDiagramElement{

	/**
	 * Constants defined for specifying the types of combined fragments
	 * 
	 */
	public static final int OPTIONAL = 1;
	public static final int ALTERNATIVE = 2;
	public static final int PARALLEL = 3;
	public static final int LOOP = 4;
	
	private int type; //for representing ALT, OPT or Loop fragments
	private String guard; //for representing guard conditions
	private LinkedList <SequenceDiagram> sequenceDiagrams; //each fragment is 
								// composed by, at least, one sequence diagram.
	
	
	public Fragment(String name) {
		super(name);
		sequenceDiagrams = new LinkedList<SequenceDiagram>();
	}


	public void setType(int type) {
		this.type = type; 
	}


	public int getType() {
		return type;
	}


	public SequenceDiagram addSequenceDiagram(String rowName, String guard) {
		String elementName = this.getName() + " - " + rowName; 
		SequenceDiagram rowSD = SequenceDiagram.createSequenceDiagram(
				elementName, guard); 
		boolean answer;
		switch (type) {
		case OPTIONAL:
			if (!sequenceDiagrams.isEmpty())
				sequenceDiagrams.clear();
			answer = sequenceDiagrams.add(rowSD);
			break;

		case PARALLEL:
			if (!sequenceDiagrams.isEmpty())
				sequenceDiagrams.clear();
			answer = sequenceDiagrams.add(rowSD);
			break;
			
		case ALTERNATIVE:
			answer = sequenceDiagrams.add(rowSD);
			break;
			
		case LOOP: 
			if (!sequenceDiagrams.isEmpty())
				sequenceDiagrams.clear();
			answer = sequenceDiagrams.add(rowSD);
			break;
		default:
			answer = false; 
			break;
		}
		
		if (answer) 
			return rowSD; 
		else 
			return null; 
	}


	public LinkedList<SequenceDiagram> getSequenceDiagrams() {
		return sequenceDiagrams;
	}


	
	public Element getDOM(Document doc) {
		Element root = doc.createElement("Fragment");
		
		//Defining the attributes of FRAGMENT DOM element
		root.setAttribute("name", getName());
		switch (type) {
		case ALTERNATIVE:	
			root.setAttribute("type", "alternative");
			break;

		case LOOP: 
			root.setAttribute("type", "loop");
			break;
			
		case OPTIONAL: 
			root.setAttribute("type", "optional");
			break;
			
		case PARALLEL: 
			root.setAttribute("type", "parallel");
			break;
			
		default:
			break;
		}
		
		//Defining the REPRESENTEDBY DOM element
		
		Iterator<SequenceDiagram> it = sequenceDiagrams.iterator(); 
		while (it.hasNext()) {
			SequenceDiagram s = it.next();
			Element rb = doc.createElement("RepresentedBy"); 
			rb.setAttribute("seqDiagName", s.getName());
			root.appendChild(rb); 
		}
		
		return root;
	}
}
