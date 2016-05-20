package splGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fdtmc.FDTMC;
import fdtmc.State;
import fdtmc.Transition;
import splar.core.fm.FeatureModel;
import splar.core.fm.FeatureTreeNode;
import tool.RDGNode;

public class SPLFilePersistence {

	
	private static String modelsPath = "/home/andlanna/workspace2/reana/src/splGenerator/generatedModels/";
	private static String cnfFilePrefix = "cnf_";
	private static String dotFilePrefix = "dot_"; 
	private static String splName = "spl";
	
	public static void FM2JavaCNF(FeatureModel fm) {
		try {
			FileWriter writer = new FileWriter(modelsPath + cnfFilePrefix + splName + ".txt");
			BufferedWriter buffer = new BufferedWriter(writer);
			String fileContent = fm.FM2JavaCNF(); 
			buffer.write(fileContent);
			buffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	
	
	/**
	 * This function is responsible for creating a DOT representation for an 
	 * FDTMC passed as an argument. The resulting graph is persisted in a DOT
	 * file, named as the value passed by the parameter "name". 
	 * @param f - the FDTMC that will be persisted at a DOT file.
	 * @param name - the filename of the DOT file. 
	 * @author andlanna
	 */
	public static void fdtmc2Dot(FDTMC f, String name) { 
		StringBuilder builder = new StringBuilder(); 
		
		builder.append("digraph graphname {\n"); 
		
		
		//creation of states strings
		for (State s : f.getStates()) {
			String entry = s.getVariableName() + s.getIndex();
			builder.append(entry + " [" ); 
			builder.append("label=\"" + entry + "\""); 
			
			if (s.getLabel() != null){
				if (s.getLabel().equals("initial")) {
					builder.append(",color=blue,shape=doublecircle");
				}
				if (s.getLabel().equals("success")) {
					builder.append(",color=green,shape=doublecircle");
				}
				if (s.getLabel().equals("error")){
					builder.append(",color=red,shape=doublecircle");
				}
			}
			builder.append(" ];\n");
		}
		
		//Creation of edges in graph
		for (State s: f.getStates()) {
			String sourceEntry = s.getVariableName() + s.getIndex();
			for (Transition t : f.getTransitions().get(s)) {
				State target = t.getTarget(); 
				String targetEntry = target.getVariableName() + target.getIndex();
				
				builder.append(sourceEntry); 
				builder.append(" -> "); 
				builder.append(targetEntry);
				builder.append(" [label=\"" + t.getActionName() + " / "); 
				builder.append(t.getProbability() + "\"");
				
				if (target.getLabel() != null)
					if (target.getLabel().equals("error"))
						builder.append(", style=dotted");
				builder.append("];");
				builder.append("\n");
			}
		}
		
		builder.append("}");
		
		//Persist the dot content into a file
		try {
			FileWriter fl = new FileWriter(modelsPath + dotFilePrefix + name + ".dot");
			BufferedWriter buffer = new BufferedWriter(fl); 
			buffer.write(builder.toString());
			buffer.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	
	/**
	 * This method is responsible to create an RDG representation using the DOT 
	 * format. It receives an RDG node and the filename as input parameters, 
	 * creates the DOT structure and persist it at an DOT file at the specified
	 * folder.   
	 * @param node the initial RDG node from where the structure will be created
	 * @param filename the filename where the DOT structure will be persisted.
	 */
	public static void rdg2Dot (RDGNode node, String filename) {
		StringBuilder builder = new StringBuilder(); 
		
		builder.append("digraph graphname {\n");
		
		String nodesDefinition = createNodeDefinition(node); 
		builder.append(nodesDefinition);
		
		String edgesDefinition = createEdgeDefinition(node); 
		builder.append(edgesDefinition);
		
		builder.append("}");
		try {
			FileWriter file = new FileWriter(modelsPath + dotFilePrefix + "RDG_" + filename + ".dot"); 
			file.write(builder.toString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static String createEdgeDefinition(RDGNode node) {
		StringBuilder answer = new StringBuilder();
		
		for (RDGNode r : node.getDependencies()) {
			answer.append(node.getId()); 
			answer.append(" -> "); 
			answer.append(r.getId());
			answer.append(";");
			answer.append("\n");
			
			answer.append(createEdgeDefinition(r));
		}
		return answer.toString();
	}


	private static String createNodeDefinition(RDGNode node) {
		StringBuilder answer = new StringBuilder(); 
		
		answer.append(node.getId()); 
		answer.append("[");
		
		answer.append("shape=record"); 
		answer.append(", ");
		
		answer.append("label=\"{");
		answer.append("{");
		answer.append(node.getId());
		answer.append("|");
		answer.append(node.getPresenceCondition());
		answer.append("|");
		answer.append(node.getFDTMC().getVariableName());
		answer.append("}");
		answer.append("}\"");
		
		answer.append("];");
		answer.append("\n");
		
		for (RDGNode n : node.getDependencies()){
			answer.append(createNodeDefinition(n)); 
		}
		return answer.toString();
	}
	

}
