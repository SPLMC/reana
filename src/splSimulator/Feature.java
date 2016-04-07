package splSimulator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Feature {

	public static final boolean MANDATORY = true;
	public static final boolean ABSTRACT = true;
	public static final boolean HIDDEN = true;

	//Constants used to define the type of children's relation.
	public static final int LEAF = 0; 
	public static final int OR = 1; 
	public static final int AND = 2; 
	public static final int ALTERNATIVE = 3;
	
	//Set of features created for the Feature Model. 
	private static HashMap<String, Feature> features = 
			new HashMap<String, Feature>(); 
	
	
	private String name; 
	private HashSet<Feature> children;
	private int type;
	private boolean mandatory;
	private boolean abs;
	private boolean hidden; 
	
	public static Feature createFeature(String name) {
		Feature f = new Feature(name); 
		features.put(name, f);
		if (features.get(name).equals(f))
			return f; 
		else 
			return null;
	}
	
	
	public Feature() {
		children = new HashSet<Feature>();
	}
	
	public Feature(String name) {
		this();
		this.name = name;
	}

	public Feature addChild(String name, int type, boolean mand, boolean abs, 
			boolean hid) {
		Feature f = createFeature(name);
		
		f.setType(type);
		f.setMandatory(mand);
		f.setAbstract(abs);
		f.setHidden(hid);
		boolean inserted = children.add(f);
		if (inserted)
			return f; 
		else 
			return null;
	}




	private void setHidden(boolean hidden) {
		this.hidden = hidden; 
	}


	private void setAbstract(boolean abs) {
		this.abs = abs; 
	}


	private void setMandatory(boolean mandatory) {
		this.mandatory = mandatory; 		
	}


	public HashSet<Feature> getChildren() {
		return children;
	}


	public String getName() {
		return name;
	}

	
	private void setType(int type) {
		this.type = type;
	}

	
	public int getType() {
		return type;
	}


	public boolean isMandatory() {
		return mandatory;
	}


	public boolean isAbstract() {
		return abs;
	}


	public boolean isHidden() {
		return hidden;
	}


	public Feature getChildrenByName(String name) {
		Iterator<Feature> itFeat = children.iterator();
		Feature f; 
		while (itFeat.hasNext()) {
			f = itFeat.next(); 
			if (f.getName().equals(name))
				return f; 
		}
		return null;
	}


	public boolean deleteFeature(String name) {
		
		Feature f = features.get(name); 
		//1st step: delete the feature from the feature model
		boolean delFm = children.remove(f);
		
		//2nd step: delete the feature from the set of created features
		boolean delSetFeat = features.remove(f.getName(), f); 
		
		return (delFm && delSetFeat);
	}


	

}
