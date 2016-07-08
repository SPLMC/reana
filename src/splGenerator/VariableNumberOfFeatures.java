package splGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import splGenerator.Util.SPLFilePersistence;
import splGenerator.tests.SDTransformationsTest;
import splar.core.fm.FeatureModel;
import splar.core.fm.FeatureTreeNode;

public class VariableNumberOfFeatures extends VariableBehavioralParameters {

	FeatureModelParameters fmParameters = FeatureModelParameters
			.getConfiguration(FeatureModelParameters.GHEZZI_FEATURE_MODEL);
	private int fragmentSize;
	private HashSet<Lifeline> lifelines;
	private int numberOfAltFragments;
	private int numberOfLoopFragments;

	private HashMap<String, String> renamedFeatures;

	@Override
	protected LinkedList<SPL> employTransformation(SPL spl)
			throws CloneNotSupportedException {
		// 1st step: obtain all SPL's characteristics for creating similar SPLs
		// get the feature model's characteristics
		defineFMParameters(spl);
		// get the behavioral model's characteristics
		defineBehavioralModelParameters(spl);

		// 2nd step: create the first seed SPL.
		LinkedList<SPL> answer = new LinkedList<SPL>();
		SPL currentVersion = createSplDeepCopy(spl);


		while (currentValue <= maxValue) {
			renamedFeatures = new HashMap<String, String>();
			int lastFeatureIndex = lastFeatureIndex(currentVersion.getFeatureModel()
					.getRoot());

			// 3rd step: from each seed SPL, we will create a new SPL having the
			// same feature and behavioral models' characteristics than its seed
			SplGenerator generator = SplGenerator.newInstance();
			generator.setFeatureModelParameters(fmParameters);
			generator.setNumberOfFeatures(variationStep + 1);
			generator.setFragmentSize(this.fragmentSize);
			generator.setNumberOfActivities(1);
			generator.setNumberOfDecisionNodes(0);
			generator.setNumberOfLifelines(6);
			generator.setNumberOfReliabiliatiesValues(0.990, 0.9999, 3);
			generator.setNumberOfAltFragments(this.numberOfAltFragments);
			generator.setNumberOfLoopsFragments(this.numberOfLoopFragments);

			SPL temp = generator.generateSPL(SplGenerator.SPLOT,
					SplGenerator.SYMMETRIC);

			int nextIndex = lastFeatureIndex;

			renameFeatures(temp.getFeatureModel().getRoot(), temp, nextIndex);
			createFeatureIDEFile(temp, "");
			
			temp = appendSPL(temp, currentVersion);
			
			answer.add(temp);

			currentVersion = createSplDeepCopy(temp);
			System.out.println(currentVersion.getFeatureModel().FM2JavaCNF());
			currentValue += variationStep;
		}
		return answer;
	}

	private void createFeatureIDEFile(SPL temp, String obs) {
		String x = temp.getFeatureModel().dumpFeatureIdeXML();
		try {
			File f = new File("/home/andlanna/workspace2/reana/src/splGenerator/generatedModels/" + currentValue + "_" + obs + ".xml");
			PrintStream p = new PrintStream(f);
			PrintStream oldOut = java.lang.System.out;
			java.lang.System.setOut(p);
			System.out.print(x);
			p.flush();
			p.close();
			java.lang.System.setOut(oldOut);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private SPL appendSPL (SPL temp, SPL currentVersion) {
		SPL answer = null;
		//1st step: create a deep copy of the current version of the spl to be changed. 
		answer = createSplDeepCopy(currentVersion);

		createFeatureIDEFile(answer, "beforeAppend");
		System.out.println("I created the featureIDE file before appending the new feature model");
		
		//2nd step: obtain all features to be added in the current version of the spl.
		Enumeration<?> children = temp.getFeatureModel().getRoot().children();
		LinkedList<FeatureTreeNode> childrenToAdd = new LinkedList<FeatureTreeNode>();
		
		while (children.hasMoreElements()) {
			FeatureTreeNode a = (FeatureTreeNode)children.nextElement();
			childrenToAdd.add(a);
		}
		
		LinkedList<Fragment> fragmentsToAdd = new LinkedList<Fragment>();
		
		for (FeatureTreeNode node : childrenToAdd) {
			fragmentsToAdd.addAll(getFragmentsByGuardCondition(node.getName(), temp));
			answer.getFeatureModel().getRoot().add(node);
		}
		
		createFeatureIDEFile(answer, "afterAppend");
		System.out.println("I created the featureIDE file after appending the new feature model");
		
		//3rd step: get the fragments associated to features which will be add at new feature model
		SequenceDiagram sdRoot = getSequenceDiagramByGuardCondition("R", answer);
		
		for (Fragment fr : fragmentsToAdd) {
			int pos = new Random().nextInt(sdRoot.getElements().size());
			sdRoot.getElements().add(pos, fr);
		}
		
		return answer;
	}
	
	

	private SequenceDiagram getSequenceDiagramByGuardCondition(String string,
			SPL spl) {
		SequenceDiagram answer = null; 
		for (Activity a : spl.getActivityDiagram().getSetOfActivities()) {
			for (SequenceDiagram sd : a.getTransitiveSequenceDiagram()) {
				if (sd.getGuardCondition().equals(string)) {
					answer = sd; 
				}
			}
		}
		return answer;
	}

	private LinkedList<Fragment> getFragmentsByGuardCondition(String name,
			SPL temp) {
		LinkedList<Fragment> answer = new LinkedList<Fragment>();
		for (Activity a : temp.getActivityDiagram().getSetOfActivities()) {
			for (SequenceDiagram sd : a.getSequenceDiagrams()) {
				for (Fragment fr : sd.getFragments()) {
					for (SequenceDiagram s: fr.getSequenceDiagrams()) {
						if (s.getGuardCondition().equals(name)) {
							answer.add(fr);
						} else {
						}
					}
				}
			}
		}
		
		return answer;
	}

	private LinkedList<Fragment> getFragmentsByFeatureName(String name, SPL temp) {
		LinkedList<Fragment> answer = new LinkedList<Fragment>();
		List<Activity> activities = temp.getActivityDiagram()
				.getSetOfActivities();
		for (Activity act : activities) {
			HashSet<Fragment> fragments = act.getTransitiveFragments();
			for (Fragment fr : fragments) {
				for (SequenceDiagram sd : fr.getSequenceDiagrams()) {
					if (sd.getGuardCondition().equals(name)) {
						answer.add(fr);
					}
				}
			}
		}
		return answer;
	}

	private void renameFeatures(FeatureTreeNode node, SPL spl, int nextIndex) {

		if (node.isRoot()) {
			Enumeration<?> children = node.children();
			while (children.hasMoreElements()) {
				FeatureTreeNode f = (FeatureTreeNode) children.nextElement();
				nextIndex++;
				renameFeatures(f, spl, nextIndex);
			}
		} else {
			StringBuilder strBldrName = new StringBuilder();
			if (node.getName().startsWith("o_")
					|| node.getName().startsWith("m_")
					|| node.getName().startsWith("g_")) {
				String[] strs = node.getName().split("_");
				strs[1] = Integer.toString(nextIndex);
				for (int i = 0; i < strs.length; i++) {
					if (strs[i].equals("")) {
						strBldrName.append("");
					} else
						strBldrName.append(strs[i]);
					strBldrName.append("_");
				}
				strBldrName.deleteCharAt(strBldrName.length() - 1);
				if (!renamedFeatures.containsKey(node.getName())) {
					renamedFeatures.put(node.getName(), strBldrName.toString());
					renameSequenceDiagram(node.getName(), strBldrName.toString(), spl); 
					node.setName(strBldrName.toString());
				}
				Enumeration<?> children = node.children();
				while (children.hasMoreElements()) {
					FeatureTreeNode f = (FeatureTreeNode) children
							.nextElement();
					nextIndex++;
					renameFeatures(f, spl, nextIndex);
				}
			}

			if (node.getName().startsWith("_Ge")
					|| node.getName().startsWith("_Gi")) {
				String[] strs = node.getName().split("_");
				strs[2] = Integer.toString(nextIndex);
				for (int i = 0; i < strs.length; i++) {
					if (strs[i].equals("")) {
						strBldrName.append("_");
					} else {
						strBldrName.append("__");
						strBldrName.append(strs[i]);
					}
				}
				strBldrName = strBldrName.deleteCharAt(strBldrName.length() - 1);
				strBldrName = strBldrName.deleteCharAt(strBldrName.length() - 1);
				if (strBldrName.charAt(0) == '_'){
//					System.out.println("achei");
					strBldrName = strBldrName.deleteCharAt(0);
				}
				if (!renamedFeatures.containsKey(node.getName())) {
					renamedFeatures.put(node.getName(), strBldrName.toString());
					renameSequenceDiagram(node.getName(), strBldrName.toString(), spl);
					node.setName(strBldrName.toString());
				}
				Enumeration<?> children = node.children();
				while (children.hasMoreElements()) {
					FeatureTreeNode f = (FeatureTreeNode) children
							.nextElement();
					renameFeatures(f, spl, nextIndex);
				}
			}
		}
	}

	private void renameSequenceDiagram(String oldName, String newName, SPL spl) {
		for (Activity a : spl.getActivityDiagram().getSetOfActivities()) {
			for (SequenceDiagram sd: a.getSequenceDiagrams()) {
				if (sd.getGuardCondition().equals(oldName)) {
					sd.setGuard(newName);
				}
				LinkedList<SequenceDiagram> otherSDs = sd.getTransitiveSequenceDiagram();
				for (SequenceDiagram s : otherSDs) {
					if (s.getGuardCondition().equals(oldName)){
						s.setGuard(newName);
					}
				}
			}
		}
	}

	private int lastFeatureIndex(FeatureTreeNode feature) {
		int answer = parseIndex(feature.getName());
		Enumeration<?> children = feature.children();
		while (children.hasMoreElements()) {
			Object obj = children.nextElement();
			FeatureTreeNode c = (FeatureTreeNode) obj;
			answer = Integer.max(answer, lastFeatureIndex(c));
		}
		return answer;
	}

	private int parseIndex(String name) {
		int answer;
		String[] strs = name.split("_");
		if (strs.length > 1) {
			int pos = 0;
			while (strs[pos].equals("") || strs[pos].equals("o")
					|| strs[pos].equals("m") || strs[pos].equals("g")
					|| strs[pos].equals("Gi") || strs[pos].equals("Ge")) {
				pos++;
			}
			answer = Integer.parseInt(strs[pos]);
		} else {
			answer = 0;
		}
		return answer;
	}

	private void defineBehavioralModelParameters(SPL spl) {
		SplGenerator g = spl.getSplGenerator();
		this.fragmentSize = g.getFragmentSize();
		this.lifelines = getLifelines(spl);
		this.numberOfAltFragments = g.getNumberOfAltFragments();
		this.numberOfLoopFragments = g.getNumberOfLoopFragments();
	}

	private HashSet<Lifeline> getLifelines(SPL spl) {
		HashSet<Lifeline> answer = new HashSet<Lifeline>();
		for (Activity a : spl.getActivityDiagram().getSetOfActivities()) {
			answer.addAll(a.getTransitiveLifelines());
		}
		return answer;
	}

	private void defineFMParameters(SPL spl) {
		String className = spl.getSplGenerator().getFeatureModelParameters()
				.getClass().getSimpleName();
		switch (className) {
		case "GhezziConfigurationSet":
			fmParameters = FeatureModelParameters
					.getConfiguration(FeatureModelParameters.GHEZZI_FEATURE_MODEL);
			break;

		default:
			fmParameters = null;
			break;
		}
	}

	private void printObject(Object o) {
		// Object o = fmParameters2;
		StringBuilder str = new StringBuilder();

		str.append("Class name: " + o.getClass().getName());
		str.append("\n");
		str.append("---");
		str.append("\n\n");

		str.append("Constructors");
		str.append("\n");
		for (Constructor c : o.getClass().getConstructors()) {
			str.append(Modifier.toString(c.getModifiers()) + " ");
			str.append(c.getName());
			str.append("\n");
		}
		str.append("\n\n");

		str.append("Field");
		str.append("\n");
		for (Field f : o.getClass().getDeclaredFields()) {
			str.append(Modifier.toString(f.getModifiers()) + " ");
			str.append(f.getName());
			str.append("\n");
		}
		str.append("\n\n");

		str.append("Method");
		str.append("\n");
		for (Method m : o.getClass().getMethods()) {
			str.append(Modifier.toString(m.getModifiers()) + " ");
			str.append(m.getName());
			str.append("(");
			for (Parameter p : m.getParameters()) {
				str.append(p.getType().getClass().getSimpleName() + " ");
				str.append(p.getName());
			}
			str.append(")");
			str.append("\n");
		}
		str.append("\n\n");

		String answer = str.toString();
		System.out.println(answer);
	}

}
