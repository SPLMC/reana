package splGenerator.transformation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.sun.corba.se.spi.orbutil.fsm.State;

import fdtmc.FDTMC;
import splGenerator.ActivityDiagram;
import splGenerator.ActivityDiagramElement;
import splGenerator.SPLFilePersistence;
import splGenerator.SplGenerator;
import splGenerator.Transition;
import tool.RDGNode;

public class Transformer {

	/**
	 * This method is responsible for creating 
	 * @param ad
	 * @return
	 */
	public RDGNode transformAD(ActivityDiagram ad) {
		FDTMC f = new FDTMC();
		f.setVariableName("s");
		
		fdtmc.State initState = f.createInitialState(); 
		fdtmc.State errorState = f.createErrorState();
		
		
		ActivityDiagramElement initialActivity = ad.getStartNode();
		HashSet<Transition> trans = initialActivity.getTransitions(); 
		
		for (Transition t : trans) {
			fdtmc.State source = initState;
			createFDTMCTransition(source, errorState, t, f); 
		}		
				
		SPLFilePersistence.fdtmc2Dot(f, ad.getName());
		
		RDGNode answer = new RDGNode(ad.getName(), "True", f);
		return answer; 
	}

	private fdtmc.FDTMC createFDTMCTransition(fdtmc.State source, fdtmc.State errorState,
			Transition trans, FDTMC f) {
		
		fdtmc.State target = null;
		if (trans.getTarget().getClass().getSimpleName().equals("EndNode"))
			target = f.createSuccessState(); 
		else
			target = f.createState();
		
		f.createTransition(source, target, trans.getElementName(), Double.toString(trans.getProbability()));
		f.createTransition(source, errorState, trans.getElementName(), Double.toString(1-trans.getProbability()));
		
		source = target; 
		splGenerator.ActivityDiagramElement nextAct = trans.getTarget();
				
		for (Transition t : nextAct.getTransitions()) {
			createFDTMCTransition(source, errorState, t, f); 
		}
		
		return f;
	}
}
