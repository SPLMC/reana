package fdtmc;

public class Inline {
	
	private Interface iface;
	private FDTMC fragment;
	
	private State initialInlined;
    private State initialFragment;
    private State successInlined;
    private State successFragment;
    private State errorInlined;
    private State errorFragment;

	public Inline(Interface iface, FDTMC fragment) {
		
		this.iface = iface;
		this.fragment = fragment;
		
		 this.initialInlined = iface.getInitial();
	     this.initialFragment = fragment.getInitialState();
	     this.successInlined = iface.getSuccess();
	     this.successFragment = fragment.getSuccessState();
	     this.errorInlined = iface.getError();
	     this.errorFragment = fragment.getErrorState();
		
	}

	public State getInitialInlined() {
		return initialInlined;
	}

	public State getInitialFragment() {
		return initialFragment;
	}

	public State getSuccessInlined() {
		return successInlined;
	}
	
	public State getSuccessFragment() {
		return successFragment;
	}

	public State getErrorInlined() {
		return errorInlined;
	}

	public State getErrorFragment() {
		return errorFragment;
	}
}
