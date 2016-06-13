package splGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import splGenerator.Util.SPLFilePersistence;
import jdk.nashorn.internal.ir.WhileNode;

/**
 * This class is responsible for generating models considering the variation of
 * the parameters involved into Feature and Behavioral models comprising the
 * Software Product Line. Each model is build by employing a parameter variation
 * of its previous model. So this class is responsible for taking a SPL object
 * as input and manipulate it according to the parameter's variation. Each
 * subclass implements a parameter variation.
 * 
 * @author andlanna
 *
 */
public abstract class VariableBehavioralParameters {

	public static final int ACTIVITIES = 0;
	public static final int SEQUENCEDIAGRAMSIZE = 1;
	public static final int FRAGMENTSIZE = 2;
	public static final int DECISIONNODES = 3;
	public static final int SCATTEREDFRAGMENTS = 4; // per presence condition
	public static final int REPLICATEDFRAGMENTS = 5; // per presence condition
	public static final int LIFELINERELIABILITY = 6;
	public static final int CONFIGURATIONS = 7;

	/**
	 * Factory method for getting an instance of a VariableBehavioralParameters
	 * subclass, according to the input parameters. Parameter's values are
	 * defined as constants in this class, and are listed in the following:
	 * 
	 * @param option
	 * @return
	 */
	public static VariableBehavioralParameters getInstance(int option) {
		VariableBehavioralParameters answer = null;

		switch (option) {
		case LIFELINERELIABILITY:
			answer = new VariableLifelineReliability();
			break;

		case FRAGMENTSIZE:
			answer = new VariableFragmentSize();
			break;

		default:
			answer = null;
			break;
		}

		return answer;
	}

	protected int minValue;
	protected int maxValue;
	protected int variationStep;
	protected int currentValue;

	private LinkedList<SPL> changedModels;

	public VariableBehavioralParameters() {
		this.minValue = 0;
		this.maxValue = 0;
		this.variationStep = 0;
		this.currentValue = 0;
		this.changedModels = new LinkedList<SPL>();
	}

	public void setVariationValues(int minValue, int maxValue, int variationStep) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.variationStep = variationStep;
		this.currentValue = this.minValue;
	}

	/**
	 * This method is responsible for employing the variations at the input
	 * software product lines. For each Software Product Line passed contained
	 * in the input parameter, it apply the transformation defined by the method
	 * employTransformation (that is implemented differently according to each
	 * subclass). The transformation will be applied at the variable parameter
	 * according to the subclass instantiated. While the variable parameter's
	 * value is within the range (min and max value), a set of different models
	 * will be created, according to the step value. The result is a set of SPL
	 * objects containing different (evolved) Feature or Behavioral Models.
	 * 
	 * @param spls
	 *            This input parameter contains a set of SPL objects,
	 *            represented by a linked list, which will be subject to the
	 *            variations applied at the variable parameter.
	 * @return The function's return is a set of SPL objects, containing all the
	 *         variations of the input SPLs according to the variable parameter.
	 * @throws CloneNotSupportedException
	 * @throws IOException
	 */
	public LinkedList<SPL> generateSplVariation(LinkedList<SPL> spls)
			throws CloneNotSupportedException, IOException {
		LinkedList<SPL> answer = new LinkedList<SPL>();
		int turn = 0; 
		while (currentValue >= minValue && currentValue <= maxValue) {
			LinkedList<SPL> temp = new LinkedList<SPL>();
			turn++; 
			System.out.println("turn: " + turn);
			for (SPL s : spls) { //for each spl to be transformed, 
				ActivityDiagram.reset();
				ActivityDiagramElement.reset();
				SequenceDiagram.reset();
				SequenceDiagramElement.reset();
				File f = File.createTempFile("spl", ".xml");
				f.deleteOnExit();
				FileOutputStream stream = new FileOutputStream(f);
				stream.write(s.getXmlRepresentation().getBytes());
				stream.flush();
				SPL t = SPL.getSplFromXml(f.getAbsolutePath()); //its "deep copy" is produced
				t.setName("model_" + spls.indexOf(s));
				t.setFeatureModel(s.getFeatureModel());
				temp.add(t); 
				while (!temp.isEmpty()) { //then, the transformation is applied on each copy
					SPL spl = temp.removeFirst();
					answer.addAll(employTransformation(spl));
				}
				currentValue += variationStep; //The step varies according to its step
			}
		}
		return answer;
	}

	protected abstract LinkedList<SPL> employTransformation(SPL spl)
			throws CloneNotSupportedException;

	public int getCurrentValue() {
		return currentValue;
	}

	public int getMaximumValue() {
		return maxValue;
	}

	public int getVariationStep() {
		return variationStep;
	}

}
