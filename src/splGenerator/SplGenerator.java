package splGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SplGenerator {

	/**
	 * Constants used by the SPL models generator
	 */
	//Constants for choosing which FM generator will be employed
	public static final int GHEZZIGENERATOR = 0;
	public static final int SPLOT = 1; 
	
	//Constants for choosing which topology resemblance between FM and UML 
	//behavioral models will be used
	public static final int SYMMETRIC = 0; 
	//add constants for  new methods here... 
	

	private static SplGenerator instance = null;
	
	private FeatureModel fm; 
	private ConfigurationKnowledge ck; 

	
	/**
	 * GENERATORS GENERAL PARAMETERS
	 * The following attributes represent the general attributes of the SPL
	 * generator. Here we set the folders and files names used by the generator.
	 */
	private String modelsPath = "/home/andlanna/workspace2/reana/src/splGenerator/generatedModels/";
	private String fmFilePrefix = "fm_"; 
	private String umlFilePrefix = "uml_"; 
	
	
	
	
	/**
	 * The attributes below regard to the Model's parameters, i.e., they 
	 * represent the values of each parameter for a especific model.
	 */
	private int numberOfFeatures;
	private int fragmentSize; 			// =numberOfMessages/fragments
	private int numberOfActivities;		
	private int numberOfDecisionNodes;
	private int numberOfLifelines; 		
	
	
	
	
	/**
	 * Indexes used by the behavioral models generator
	 */
	private int idxModel; 
	private int idxFeature; 
	//Indexes for activity diagram elements
	private int idxActivity;
	private int idxActTransition;
	private int idxDecisionNode; 
	private int idxMergeNode; 
	//Indexes for sequence diagram elements
	private int idxSequenceDiagram;
	private int idxLifeline;
	private int idxFragment;
	private int idxMessage;
	
	
	/**
	 * Lists of elements created during the behavioral models generation
	 */
	private HashSet<Activity> setOfActivities; 
	private List<Fragment> listOfPendingFragments; 
	private List<Feature>  listOfPendingFeatures; 
	
	
	private SplGenerator() {
		ck = new ConfigurationKnowledge();
		
		idxModel = 0;
		idxFeature = 0; 
		idxActivity = 0; 
		idxActTransition = 0; 
		idxDecisionNode = 0; 
		idxMergeNode = 0;
		idxSequenceDiagram = 0;
		idxLifeline = 0;
		idxFragment = 0;
		idxMessage = 0;
		
		listOfPendingFragments = new LinkedList<Fragment>();
		listOfPendingFeatures  = new LinkedList<Feature>();
	}

	
	/**
	 * This method is responsible for generating the artifacts of a software 
	 * product line in an automated fashion. When it is called, its results is
	 * an SPL object containing references for a FeatureModel object representing
	 * the SPL's feature model, an ActivityDiagram object containing representing
	 * the coarse-grained behavior of a software product line which activities are
	 * refined into their respective sequence diagrams and ConfigurationKnowledge
	 * object representing the configuration Knowledge of the software product line.   
	 * @param generationMethod The parameter value represents the generation method
	 * which will be employed by the generator. The values it may assume are defi-
	 * ned by the constants GHEZZIGENERATOR and SPLOTGENERATOR. 
	 * @return the SPL object representing the whole software product line, and its
	 * artifacts.
	 */
	public SPL generateSPL(int generationMethod) {
		SPL spl = null;
		return spl;
	}
	
	
	public static SplGenerator newInstance() {
		instance = new SplGenerator(); 
		return instance;
	}


	public void setNumberOfFeatures(int numFeatures) {
		this.numberOfFeatures = numFeatures; 
	}


	public void setFragmentSize(int fragSize) {
		this.fragmentSize = fragSize; 
	}


	public void setNumberOfActivities(int numActivities) {
		this.numberOfActivities = numActivities; 		
	}


	public void setNumberOfDecisionNodes(int numDecisionNodes) {
		this.numberOfDecisionNodes = numDecisionNodes; 		
	}


	public void setNumberOfLifelines(int numLifelines) {
		this.numberOfLifelines = numLifelines; 
	}
	
	/**
	 * This method is responsible for creating all the elements of the
	 * behavioral models and assembling them in a correct manner.
	 * @param fm - The feature model associated with the Software 
	 * Product Line
	 * @return spl - the SPL object containing the Feature and Behavioral
	 * models of the software product line. 
	 */
	public SPL generateBehavioralModel(FeatureModel fm) {
		
		SPL spl = SPL.createSPL("SPL_model_" + idxModel++);
		ActivityDiagram ad = generateActivityDiagramStructure();
		spl.setActivityDiagram(ad);
		
		//creating the sequence diagrams elements before creating the sequence
		//diagrams
		for (int i=0; i<numberOfLifelines; i++) {
			SequenceDiagramElement.createElement(SequenceDiagramElement.LIFELINE, 
					"Lifeline" + idxLifeline++);
		}

		
		//generate the Sequence Diagram related to the Root feature
		Feature root = fm.getRoot(); 
		SequenceDiagram sdRoot = randomSequenceDiagram("root", "true");
		System.out.println(sdRoot.toString());
		ck.associateArtifact(root, sdRoot);
		
		
		//creating the fragments of the sequence diagram
		for (int i=0; i<numberOfFeatures; i++) {
			Fragment f = (Fragment) Fragment.createElement(
					SequenceDiagramElement.FRAGMENT, "Fragment_" + idxFragment++);
			f.setType(Fragment.OPTIONAL);
			listOfPendingFragments.add(f);
		}
		
		for (int i=0; i<listOfPendingFragments.size(); i++) {
			Fragment fr = randomFragment();
			Feature f = randomFeature();
			SequenceDiagram sd = randomSequenceDiagram("SD_" + idxSequenceDiagram++, f.getName());
			fr.addSequenceDiagram(sd); 
			ck.associateArtifact(f, sd);
			
			//insert it on a random position of root's sequence diagram
			int position = randomPosition(sdRoot);
			sdRoot.getElements().add(position, fr);
		}
		
		Activity a = ad.getActivityByName("Activity_0");
		System.out.println(a);
		a.addSequenceDiagram(sdRoot);
		
		 
		
		return spl;
	}


	private int randomPosition(SequenceDiagram sdRoot) {
		int tam = sdRoot.getElements().size();
		Random ran = new Random(); 
		int position = ran.nextInt(tam); 
		return position;
	}


	private Fragment randomFragment() {
		Random ran = new Random(); 
		int i = ran.nextInt(listOfPendingFragments.size()); 
		Fragment f = listOfPendingFragments.remove(i);
		return f;
	}


	private Feature randomFeature() {
		Feature answer = null; 
		if (listOfPendingFeatures.size() > 0) {
			Random ran = new Random(); 
			int i = ran.nextInt(listOfPendingFeatures.size()); 
			answer = listOfPendingFeatures.remove(i-1); 
		} else  { 
			//TODO Alterar depois!!!!
			fm.getRoot();
		}
		return answer;
	}


	/**
	 * This method creates a Sequence Diagram randomly according to the parameters
	 * defined for the SPLGenerator object. 
	 * @param name - the name of the sequence diagram  
	 * @param guard - the guard condition expressed by propositional logical 
	 * formula, described in terms of feature's names.  
	 * @return Sequence Diagram object randomly generated.
	 */
	private SequenceDiagram randomSequenceDiagram(String name, String guard) {
		SequenceDiagram sd = SequenceDiagram.createSequenceDiagram(name, guard); 
		Lifeline source = randomLifeline();
		for (int i=0; i<fragmentSize; i++) {
			Lifeline target = randomLifeline();
			sd.createMessage(source, target, Message.SYNCHRONOUS, "T" + idxActTransition++, target.getReliability());
			source = target; 
		}
		
		return sd;
	}


	private Lifeline randomLifeline() {
		Random ran = new Random(); 
		int i = ran.nextInt(numberOfLifelines); 
		Lifeline l = (Lifeline)Lifeline.getElementByName("Lifeline" + i); 
		return l;
	}

	/**
	 * This method is responsible for creating the structure of the activity 
	 * diagram representing the coarse-grained behavior of the software product
	 * line. Currently it is only creating sequential activity diagrams, but 
	 * soon it will be changed for creating random and more complex structures.
	 * @return The ActivityDiagram object representing the SPL's activity 
	 * diagram.
	 */
	private ActivityDiagram generateActivityDiagramStructure() {
		//TODO Change the method for creating random and more complex ADs. 
		ActivityDiagram ad = new ActivityDiagram();
		ad.setName("AD_SPL_" + idxModel);
		
		setOfActivities = new HashSet<Activity>();
		for (int i=0; i<numberOfActivities; i++) {
			Activity a = new Activity("Activity_" + idxActivity++);
			ad.addElement(a);
		}
		
		Iterator<Activity> it = setOfActivities.iterator();
		ActivityDiagramElement source = ad.getStartNode(), 
				               target; 
		while(it.hasNext()) {
			target = it.next();
			source.createTransition(target, "T" + idxActTransition, 1);
			source = target; 
		}
		target = ActivityDiagramElement.createElement(ActivityDiagramElement.END_NODE, "EndNode");
		ad.addElement(target); 
		
		return ad; 
	}

	/**
	 * This method is used for creating Feature Models according to the choosen 
	 * algorithm. Initially it supports two algorithms, that are chooosen 
	 * according to the generatorAlgorithm parameter: GHEZZIGENERATOR that results
	 * into a FM that all features besides root are optional, and SPLOTGENERATOR
	 * that uses the SPLOT FM generator for producing valid feature models.  
	 * @param generatorAlgorithm
	 * @return fm - a Feature Model object representing the whole Feature Model, 
	 * including its crosstree constraints. 
	 */
	public FeatureModel generateFeatureModel(int generatorAlgorithm) { 
		switch (generatorAlgorithm) {
		case GHEZZIGENERATOR:
			fm = generateGhezziFeatureModel(); 
			break;

		default:
			fm = generateSplotFeatureModel(); 
			break;
		}
		
//		fm.persistXMLFile();
		
		return fm;
	}


	private FeatureModel generateSplotFeatureModel() {
		// TODO Auto-generated method stub
		return null;
	}


	private FeatureModel generateGhezziFeatureModel() {
		FeatureModel tmp = FeatureModel.createFeatureModel("FeatureModel_" + idxModel++);
		Feature root = tmp.getRoot(); 
		root.setType(Feature.OR);
		root.setAbstract(Feature.ABSTRACT);
//		System.out.println("root" + root);
		for (int i=0; i<numberOfFeatures; i++) {
			Feature f = root.addChild("Feature_" + idxFeature++, Feature.ALTERNATIVE, Feature.MANDATORY, Feature.ABSTRACT, !Feature.HIDDEN);
			listOfPendingFeatures.add(f); 
		}
		return tmp;
	}


	


}
