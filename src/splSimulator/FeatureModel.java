package splSimulator;

import java.util.HashSet;

public class FeatureModel {

	
	
	private String fmName;
	private HashSet<String> crossTreeConstraints;
	private Feature root; 

	
	private FeatureModel() {
		root = new Feature("root"); 
		crossTreeConstraints = new HashSet<String>(); 
	}
	
	private FeatureModel(String fmName) {
		this();
		this.fmName = fmName;
	}

	public static FeatureModel createFeatureModel(String fmName) {
		return new FeatureModel(fmName);
	}

	public String getName() {
		return fmName;
	}

	public Feature getRoot() {
		return root;
	}

	public HashSet<String> getCrossTreeConstraints() {
		return crossTreeConstraints;
	}

	public boolean addCrossTreeConstraint(String ctc) {
		return crossTreeConstraints.add(ctc);
	}

}
