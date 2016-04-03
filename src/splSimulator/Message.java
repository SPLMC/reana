package splSimulator;

public class Message extends SequenceDiagramElement{

	//Constants representing message types
	public static final int SYNCHRONOUS = 1; 
	public static final int ASYNCHRONOUS = 2; 
	public static final int REPLY = 3;
	
	// Message's attributes 
	private int type; //synchronous, asynchronous or reply
	private double probability; //for representing channel's and component's
								//realiabilities
	private Lifeline source; 
	private Lifeline target;

	
	public Message(String name) {
		super(name);
	}
	
	public void setProbability (double probability) {
		this.probability = probability; 
	}
	
	public double getProbability () {
		return probability; 
	}
	
	public void setSource(Lifeline source) {
		this.source = source; 
	}
	
	public Lifeline getSource() {
		return source; 
	}
	
	public void setTarget (Lifeline target) {
		this.target = target; 
	}
	
	public Lifeline getTarget() {
		return target; 
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
