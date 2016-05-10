package splGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import splar.core.fm.FeatureTreeNode;

public class ConfigurationKnowledge {

	private HashMap<Feature, LinkedList<SequenceDiagram>> mapping = new HashMap<Feature, LinkedList<SequenceDiagram>>();
	private HashMap<FeatureTreeNode, LinkedList<SequenceDiagram>> mappingSplar = new HashMap<FeatureTreeNode, LinkedList<SequenceDiagram>>();
	
	public void associateArtifact(Feature feature, SequenceDiagram artifact) {
		System.out.println("Feature " + feature.getName() + " abstract? " + feature.isAbstract());
		LinkedList<SequenceDiagram> list = mapping.get(feature); 
		if (list == null)
			list = new LinkedList<SequenceDiagram>();
		boolean sdInserted = list.add(artifact);
		System.out.println("sdInserted: " + sdInserted);
		if (sdInserted)
			feature.setAbstract(false);
		System.out.println("Feature " + feature.getName() + " abstract? " + feature.isAbstract());
		mapping.put(feature, list); 
	}


	public void associateArtifact(FeatureTreeNode feature, SequenceDiagram artifact) {
		System.out.println("Feature " + feature.getName() + " abstract? " + feature.getProperty("abstract"));
		LinkedList<SequenceDiagram> list = mappingSplar.get(feature); 
		if (list == null)
			list = new LinkedList<SequenceDiagram>();
		boolean sdInserted = list.add(artifact);
		System.out.println("sdInserted: " + sdInserted);
		if (sdInserted)
			feature.setProperty("abstract", "false");
		System.out.println("Feature " + feature.getName() + " abstract? " + feature.getProperty("abstract"));
		mappingSplar.put(feature, list);
	}
}
