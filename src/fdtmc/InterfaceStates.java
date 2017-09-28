package fdtmc;

public class InterfaceStates {

	private State initialInlined;
	private State initialFragment;
	private State successInlined;
	private State successFragment;
	private State errorInlined;
	private State errorFragment;
	
	
	public InterfaceStates(State initialInlined, State initialFragment, State successInlined,
						   State successFragment, State errorInlined, State errorFragment) {
		this.initialInlined = initialInlined;
		this.initialFragment = initialFragment;
		this.successInlined = successInlined;
		this.successFragment = successFragment;
		this.errorInlined = errorInlined;
		this.errorFragment = errorFragment;
	}
	
	public State getInitialInlined() {
		return initialInlined;
	}
	
	public void setInitialInlined(State initialInlined) {
		this.initialInlined = initialInlined;
	}
	
	public State getInitialFragment() {
		return initialFragment;
	}
	
	public void setInitialFragment(State initialFragment) {
		this.initialFragment = initialFragment;
	}
	
	public State getSuccessInlined() {
		return successInlined;
	}
	
	public void setSuccessInlined(State successInlined) {
		this.successInlined = successInlined;
	}
	
	public State getSuccessFragment() {
		return successFragment;
	}
	
	public void setSuccessFragment(State successFragment) {
		this.successFragment = successFragment;
	}
	
	public State getErrorInlined() {
		return errorInlined;
	}
	
	public void setErrorInlined(State errorInlined) {
		this.errorInlined = errorInlined;
	}
	
	public State getErrorFragment() {
		return errorFragment;
	}
	
	public void setErrorFragment(State errorFragment) {
		this.errorFragment = errorFragment;
	}
	
}
