package fdtmc;

import java.util.Map;

public class InlineInterface {
	private final FDTMC _fdtmc;
	private Interface iface;
	private FDTMC fragment;
	private Map<State, State> statesMapping;
	private Map<State, State> fragmentStatesMapping;
	private State initialInlined;
    private State initialFragment;
    private State successInlined;
    private State successFragment;
    private State errorInlined ;
    private State errorFragment;
	
    public InlineInterface(FDTMC source, Interface ifaceArg, FDTMC fragmentArg, 
    						Map<State, State> statesMappingArg) {
    	_fdtmc = source;
    	iface = ifaceArg;
    	fragment = fragmentArg;
    	statesMapping = statesMappingArg;
    	
    }
    
    void compute() {
    	fragmentStatesMapping = _fdtmc.inlineStates(fragment);
        _fdtmc.inlineTransitions(fragment, fragmentStatesMapping);

        initialInlined = iface.getInitial();
        initialFragment = fragment.getInitialState();
        successInlined = iface.getSuccess();
        successFragment = fragment.getSuccessState();
        errorInlined = iface.getError();
        errorFragment = fragment.getErrorState();

        _fdtmc.createTransition(statesMapping.get(initialInlined),
                              fragmentStatesMapping.get(initialFragment),
                              "",
                              "1");
        _fdtmc.createTransition(fragmentStatesMapping.get(successFragment),
                              statesMapping.get(successInlined),
                              "",
                              "1");
        if (errorFragment != null) {
            _fdtmc.createTransition(fragmentStatesMapping.get(errorFragment),
                                  statesMapping.get(errorInlined),
                                  "",
                                  "1");
        }
    }
	
	
}
