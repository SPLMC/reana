package splSimulator;

import java.io.StringWriter;
import java.io.File;

import javax.sound.midi.Sequence;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class SPL {

	String name; 
	ActivityDiagram ad; 
	
	private static SPL instance; 
	
	public SPL(String name) {
		this();
		this.name = name;
	}

	public SPL() {
		this.ad = new ActivityDiagram();
	}

	public static SPL createSPL(String name) {
		instance = new SPL(name);
		return instance;
	}

	public String getXmlRepresentation() {
		StringWriter answer = new StringWriter();
		File output = new java.io.File(new String(name + "_behavioral_model.xml").replaceAll("\\s+", "_")); 
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
		
			docBuilder = docFactory.newDocumentBuilder();
		
		
		//CREATING THE XML STRUCTURE
		//root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("SplBehavioralModel"); 
		Attr splBehavioralModelName = doc.createAttribute("name");
		splBehavioralModelName.setValue(name);
		rootElement.setAttributeNode(splBehavioralModelName);
		doc.appendChild(rootElement);

		
		//Creating the DOM object representing the activity diagram
		Element domActDiagram = ad.getDOM(doc);
		rootElement.appendChild(domActDiagram);
		
		//Creating the DOM object representing the sequence diagrams
		Element domSeqDiagram = SequenceDiagram.getDOM(doc);
		rootElement.appendChild(domSeqDiagram);
		
		//Transform the content into an xml representation
		TransformerFactory transFactory = TransformerFactory.newInstance(); 
		Transformer transformer = transFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(answer);
		StreamResult result_file = new StreamResult(output);
		transformer.transform(source, result);
		transformer.transform(source, result_file);
		
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return answer.toString();
	}

	public ActivityDiagram createActivityDiagram(String name) {
		ad = new ActivityDiagram(); 
		ad.setName(name);
		return ad;
	}

	public ActivityDiagram getActivityDiagram() {
		return ad;
	}

}
