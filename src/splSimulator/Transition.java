package splSimulator;

public class Transition extends ActivityDiagramElement{

	private ActivityDiagramElement source;
	private ActivityDiagramElement target;
	private double probability;

	public Transition(){
		
	}
	
	public Transition (String elementName){
		super(elementName);
	}
	
	public void setSource(ActivityDiagramElement source) {
		this.source = source;
		source.addOutgoingTransition(this);
	}
	
	public ActivityDiagramElement getSource() {
		return source;
	}
	
	public void setTarget(ActivityDiagramElement target) {
		this.target = target;
	}
	
	public ActivityDiagramElement getTarget() {
		return target;
	}
	
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
