package splSimulator;

import java.io.StringWriter;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
		File output = new java.io.File(new String(name
				+ "_behavioral_model.xml").replaceAll("\\s+", "_"));

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;

			docBuilder = docFactory.newDocumentBuilder();

			// CREATING THE XML STRUCTURE
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("SplBehavioralModel");
			Attr splBehavioralModelName = doc.createAttribute("name");
			splBehavioralModelName.setValue(name);
			rootElement.setAttributeNode(splBehavioralModelName);
			doc.appendChild(rootElement);

			// Creating the DOM object representing the activity diagram
			Element domActDiagram = ad.getDOM(doc);
			rootElement.appendChild(domActDiagram);

			// Creating the DOM object representing the sequence diagrams
			List<Activity> setOfActivities = ad.getSetOfActivities();
			HashSet<SequenceDiagram> setOfSequenceDiagrams = new HashSet<SequenceDiagram>();
			HashSet<Lifeline> setOfLifelines = new HashSet<Lifeline>();
			HashSet<Fragment> setOfFragments = new HashSet<Fragment>();

			// 1st step --> get all the SequenceDiagrams, Lifelines and
			// Fragments used by the SPL.
			Iterator<Activity> ita = setOfActivities.iterator();
			while (ita.hasNext()) {
				Activity a = ita.next();
				// get all the sequence diagrams associated to the activity and
				// add them
				// to the set of sequence diagrams.
				setOfSequenceDiagrams.addAll(a.getTransitiveSequenceDiagram());
				setOfLifelines.addAll(a.getTranstiveLifelines());
				setOfFragments.addAll(a.getTransitiveFragments()); 
				// setOfFragments.addAll(a.getTransitiveFragments());
				// setOfLifelines.addAll(a.getTransitiveLifelines());
				// As each sequence diagram may have one or more fragments
				// inside it, and
				// each fragment is defined by at least one sequence diagram,
				// next step is
				// to get all the fragmens and sequence diagrams associated with
				// it.
			}

			Iterator<SequenceDiagram> its = setOfSequenceDiagrams.iterator();
			// while(its.hasNext()) {
			// SequenceDiagram d = its.next();
			// setOfFragments.addAll(d.getFragments());
			// setOfLifelines.addAll(d.getLifelines());
			// Iterator<Fragment> itf = setOfFragments.iterator();
			// while (itf.hasNext()) {
			// Fragment f = itf.next();
			// setOfSequenceDiagrams.addAll(f.getSequenceDiagrams());
			// }
			// }

			Element domSeqDiagram = doc.createElement("SequenceDiagrams");
			its = setOfSequenceDiagrams.iterator();
			while (its.hasNext()) {
				SequenceDiagram d = its.next();
				Element e = d.getDOM(doc);
				domSeqDiagram.appendChild(e);
			}

			Element domLifelines = doc.createElement("Lifelines");
			Iterator<Lifeline> itl = setOfLifelines.iterator();
			while (itl.hasNext()) {
				Lifeline l = itl.next();
				Element domLife = doc.createElement("Lifeline");
				domLife.setAttribute("name", l.getName());
				domLife.setAttribute("reliability",
						Double.toString(l.getReliability()));
				domLifelines.appendChild(domLife);
			}

			Element domFragments = doc.createElement("Fragments");
			Iterator<Fragment> itf = setOfFragments.iterator();
			while (itf.hasNext()) {
				Fragment f = itf.next();
				Element domF = f.getDOM(doc);
				domFragments.appendChild(domF);
			}

			domSeqDiagram.appendChild(domLifelines);
			domSeqDiagram.appendChild(domFragments);
			rootElement.appendChild(domSeqDiagram);

			// Transform the content into an xml representation
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
