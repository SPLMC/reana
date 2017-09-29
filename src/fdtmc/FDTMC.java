package fdtmc;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FDTMC {

	public static final String INITIAL_LABEL = "initial";
	public static final String SUCCESS_LABEL = "success";
	public static final String ERROR_LABEL = "error";

	private Set<State> states;
	private State initialState;
	private State successState;
	private State errorState;
	private String variableName;
	private int index;
	private Map<State, List<Transition>> transitionSystem;
	private Map<String, List<Interface>> interfaces;

	public FDTMC() {
		states = new LinkedHashSet<State>();
		initialState = null;
		variableName = null;
		index = 0;
		transitionSystem = new LinkedHashMap<State, List<Transition>>();
		interfaces = new LinkedHashMap<String, List<Interface>>();
	}

	public Collection<State> getStates() {
		return states;
	}

	public void setVariableName(String name) {
		variableName = name;
	}

	public String getVariableName() {
		return variableName;
	}

	public int getVariableIndex() {
		return index;
	}

	public State createState() {
		State temp = new State();
		temp.setVariableName(variableName);
		temp.setIndex(index);
		states.add(temp);
		transitionSystem.put(temp, null);
		if (index == 0)
			initialState = temp;
		index++;
		return temp;
	}

	public State createState(String label) {
		State temp = createState();
		temp.setLabel(label);
		return temp;
	}

	public State createInitialState() {
		State initial = createState();
		setInitialState(initial);
		return initial;
	}

	private void setInitialState(State initialState) {
		this.initialState = initialState;
		initialState.setLabel(INITIAL_LABEL);
	}

	public State getInitialState() {
		return initialState;
	}

	public State createSuccessState() {
		State success = createState();
		setSuccessState(success);
		createTransition(success, success, "", "1.0");
		return success;
	}

	private void setSuccessState(State successState) {
		this.successState = successState;
		successState.setLabel(SUCCESS_LABEL);
	}

	public State getSuccessState() {
		return successState;
	}

	public State createErrorState() {
		State error = createState();
		setErrorState(error);
		createTransition(error, error, "", "1.0");
		return error;
	}

	private void setErrorState(State errorState) {
		this.errorState = errorState;
		errorState.setLabel(ERROR_LABEL);
	}

	public State getErrorState() {
		return errorState;
	}

	public Transition createTransition(State source, State target,
			String action, String reliability) {
		if (source == null) {
			return null;
		}

		List<Transition> transitionsList = transitionSystem.get(source);
		if (transitionsList == null) {
			transitionsList = new LinkedList<Transition>();
		}

		Transition newTransition = new Transition(source, target, action,
				reliability);
		boolean success = transitionsList.add(newTransition);
		transitionSystem.put(source, transitionsList);
		return success ? newTransition : null;
	}

	/**
	 * Creates an explicit interface to another FDTMC.
	 * 
	 * An interface is an FDTMC fragment with 3 states (initial, success, and
	 * error) and 2 transitions (initial to success with probability {@code id}
	 * and initial to error with probability 1 - {@code id}).
	 * 
	 * @param id
	 *            Identifier of the FDTMC to be abstracted away.
	 * @param initial
	 *            Initial state of the interface.
	 * @param success
	 *            Success state of the interface.
	 * @param error
	 *            Error state of the interface.
	 */
	public Interface createInterface(String id, State initial, State success,
			State error) {
		Transition successTransition = createTransition(initial, success, "",
				id);
		Transition errorTransition = createTransition(initial, error, "",
				"1 - " + id);
		Interface newInterface = new Interface(id, initial, success, error,
				successTransition, errorTransition);

		List<Interface> interfaceOccurrences = null;
		if (interfaces.containsKey(id)) {
			interfaceOccurrences = interfaces.get(id);
		} else {
			interfaceOccurrences = new LinkedList<Interface>();
			interfaces.put(id, interfaceOccurrences);
		}
		interfaceOccurrences.add(newInterface);
		return newInterface;
	}

	public State getStateByLabel(String label) {
		Iterator<State> it = states.iterator();
		while (it.hasNext()) {
			State s = it.next();
			if (s.getLabel().equals(label))
				return s;
		}
		return null;
	}

	public Transition getTransitionByActionName(String action) {
		Collection<List<Transition>> stateAdjacencies = transitionSystem
				.values();
		Iterator<List<Transition>> iteratorStateAdjacencies = stateAdjacencies
				.iterator();
		while (iteratorStateAdjacencies.hasNext()) {
			List<Transition> transitions = iteratorStateAdjacencies.next();

			Transition temporaryTransition = compareTransitionsLabels(
					transitions, action);
			return temporaryTransition;
		}
		return null;

	}

	private Transition compareTransitionsLabels(List<Transition> transitions,
			String action) {
		Iterator<Transition> iteratorTransitions = transitions.iterator();
		while (iteratorTransitions.hasNext()) {
			Transition temporaryTransition = iteratorTransitions.next();
			if (temporaryTransition.getActionName().equals(action))
				return temporaryTransition;
		}
		return null;
	}

	@Override
	public String toString() {
		String msg = new String();

		Set<State> tmpStates = this.transitionSystem.keySet();
		Iterator<State> itStates = tmpStates.iterator();

		msg = createTrasitionList(itStates, msg);

		return msg;
	}

	private String createTrasitionList(Iterator<State> itStates, String msg) {
		while (itStates.hasNext()) {
			State temp = itStates.next();
			List<Transition> transitionList = this.transitionSystem.get(temp);
			if (transitionList != null) {
				Iterator<Transition> itTransitions = transitionList.iterator();
				msg = createMessage(itTransitions, temp, msg);
			}
		}

		return msg;
	}

	private String createMessage(Iterator<Transition> itTransitions,
			State temp, String msg) {
		while (itTransitions.hasNext()) {
			Transition t = itTransitions.next();
			msg += temp.getVariableName()
					+ "="
					+ temp.getIndex()
					+ ((temp.getLabel() != null) ? "(" + temp.getLabel() + ")"
							: "")
					+ " --- "
					+ t.getActionName()
					+ " / "
					+ t.getProbability()
					+ " ---> "
					+ t.getTarget().getVariableName()
					+ "="
					+ t.getTarget().getIndex()
					+ ((t.getTarget().getLabel() != null) ? "("
							+ t.getTarget().getLabel() + ")" : "") + "\n";
		}
		return msg;
	}

	/**
	 * Two FDTMCs are deemed equal whenever: - their states are equal; - their
	 * initial, success, and error states are equal; - the transitions with
	 * concrete values are equal; - the transitions with variable names have
	 * equal source and target states; and - the abstracted interfaces are
	 * equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof FDTMC) {
			FDTMC other = (FDTMC) obj;
			LinkedList<List<Interface>> thisInterfaces = new LinkedList<List<Interface>>(
					interfaces.values());
			LinkedList<List<Interface>> otherInterfaces = new LinkedList<List<Interface>>(
					other.interfaces.values());

			// Variables to test Equality
			final boolean isStateEqual = states.equals(other.states);
			final boolean isInitialStateEqual = getInitialState().equals(
					other.getInitialState());
			final boolean isSuccessStateEqual = getSuccessState().equals(
					other.getSuccessState());
			final boolean isErrorStateEqual = getErrorState().equals(
					other.getErrorState());
			final boolean isTrasitionSystemEqual = transitionSystem
					.equals(other.transitionSystem);
			final boolean isInterfaceEqual = thisInterfaces
					.equals(otherInterfaces);

			return compareBooleanValuesOfVariables(isStateEqual,
					isInitialStateEqual, isSuccessStateEqual,
					isErrorStateEqual, isTrasitionSystemEqual, isInterfaceEqual);

		}
		return false;
	}

	private Boolean compareBooleanValuesOfVariables(Boolean isStateEqual,
			Boolean isInitialStateEqual, Boolean isSuccessStateEqual,
			Boolean isErrorStateEqual, Boolean isTrasitionSystemEqual,
			Boolean isInterfaceEqual) {

		return isStateEqual && isInitialStateEqual && isSuccessStateEqual
				&& isErrorStateEqual && isTrasitionSystemEqual
				&& isInterfaceEqual;
	}

	@Override
	public int hashCode() {
		return states.hashCode() + transitionSystem.hashCode()
				+ interfaces.hashCode();
	}

	public Map<State, List<Transition>> getTransitions() {
		return transitionSystem;
	}

	/**
	 * Inlines the given FDTMCs whenever there is an interface corresponding to
	 * the string in the respective index.
	 * 
	 * @param indexedModels
	 * @return a new FDTMC which represents this one with the ones specified in
	 *         {@code indexedModels} inlined.
	 */
	public FDTMC inline(Map<String, FDTMC> indexedModels) {
		FDTMC inlined = new FDTMC();

		inlined = scrollInterfaceList(inlined, indexedModels, false);

		return inlined;
	}

	private FDTMC scrollInterfaceList(FDTMC inlined,
			Map<String, FDTMC> indexedModels,
			Boolean isInlineInterfaceWithVariability) {

		Map<State, State> statesMapping = copyForInlining(inlined);

		for (Map.Entry<String, List<Interface>> entry : interfaces.entrySet()) {
			String dependencyId = entry.getKey();
			iterateModelsDependencies(inlined, indexedModels,
					isInlineInterfaceWithVariability, statesMapping, entry,
					dependencyId);
		}

		return inlined;
	}

	private void iterateModelsDependencies(FDTMC inlined,
			Map<String, FDTMC> indexedModels,
			Boolean isInlineInterfaceWithVariability,
			Map<State, State> statesMapping,
			Map.Entry<String, List<Interface>> entry, String dependencyId) {
		if (indexedModels.containsKey(dependencyId)) {
			FDTMC fragment = indexedModels.get(dependencyId);
			for (Interface iface : entry.getValue()) {
				checkForInlineInterfaceType(inlined,
						isInlineInterfaceWithVariability, statesMapping,
						fragment, iface);
			}
		}
	}

	private void checkForInlineInterfaceType(FDTMC inlined,
			Boolean isInlineInterfaceWithVariability,
			Map<State, State> statesMapping, FDTMC fragment, Interface iface) {
		if (!isInlineInterfaceWithVariability) {
			inlined.inlineInterface(iface, fragment, statesMapping);
		} else {
			inlined.inlineInterfaceWithVariability(iface, fragment,
					statesMapping);
		}
	}

	/**
	 * Inlines the given FDTMCs whenever there is an interface corresponding to
	 * the string in the respective index.
	 * 
	 * This method maintains the variability notion by using the same
	 * abstraction id of the interface as an encoding of presence (i.e., a
	 * "switch" on whether or not to take the transitions of the inlined model).
	 * 
	 * @param indexedModels
	 * @return a new FDTMC which represents this one with the ones specified in
	 *         {@code indexedModels} inlined.
	 */
	public FDTMC inlineWithVariability(Map<String, FDTMC> indexedModels) {
		FDTMC inlined = new FDTMC();

		inlined = scrollInterfaceList(inlined, indexedModels, true);

		return inlined;
	}

	/**
	 * Prepares {@code destination} FDTMC to be an inlined version of this one.
	 * 
	 * @param destination
	 * @return a mapping from states in this FDTMC to the corresponding states
	 *         in the copied one ({@code destination}).
	 */
	private Map<State, State> copyForInlining(FDTMC destination) {
		destination.variableName = this.getVariableName();

		Map<State, State> statesMapping = destination.inlineStates(this);
		destination.setInitialState(statesMapping.get(this.getInitialState()));
		destination.setSuccessState(statesMapping.get(this.getSuccessState()));
		destination.setErrorState(statesMapping.get(this.getErrorState()));

		destination.inlineTransitions(this, statesMapping);
		return statesMapping;
	}

	/**
	 * Inlines all states from {@code fdtmc} stripped of their labels.
	 * 
	 * @param fdtmc
	 * @return
	 */
	private Map<State, State> inlineStates(FDTMC fdtmc) {
		Map<State, State> statesOldToNew = new HashMap<State, State>();
		for (State state : fdtmc.getStates()) {
			State newState = this.createState();
			statesOldToNew.put(state, newState);
		}
		return statesOldToNew;
	}

	/**
	 * Inlines all transitions from {@code fdtmc} that are not part of an
	 * interface.
	 * 
	 * @param fdtmc
	 * @param statesOldToNew
	 */
	private void inlineTransitions(FDTMC fdtmc, Map<State, State> statesOldToNew) {
		Set<Transition> interfaceTransitions = fdtmc.getInterfaceTransitions();
		for (Map.Entry<State, List<Transition>> entry : fdtmc.getTransitions()
				.entrySet()) {
			State newSource = statesOldToNew.get(entry.getKey());
			List<Transition> transitions = entry.getValue();
			if (transitions != null) {
				for (Transition transition : transitions) {
					if (!interfaceTransitions.contains(transition)) {
						State newTarget = statesOldToNew.get(transition
								.getTarget());
						createTransition(newSource, newTarget,
								transition.getActionName(),
								transition.getProbability());
					}
				}
			}
		}
	}

	private void inlineInterface(Interface iface, FDTMC fragment,
			Map<State, State> statesMapping) {
		chooseInlineInterface(iface, fragment, statesMapping, false);
	}

	private void chooseInlineInterface(Interface iface, FDTMC fragment,
			Map<State, State> statesMapping, Boolean isWithVariability) {

		Map<State, State> fragmentStatesMapping = this.inlineStates(fragment);
		this.inlineTransitions(fragment, fragmentStatesMapping);

		State initialInlined = iface.getInitial();
		State initialFragment = fragment.getInitialState();
		State successInlined = iface.getSuccess();
		State successFragment = fragment.getSuccessState();
		State errorInlined = iface.getError();
		State errorFragment = fragment.getErrorState();
		String ifaceID = "1";

		if (isWithVariability) {
			ifaceID = iface.getAbstractedId();
			this.createTransition(statesMapping.get(initialInlined),
					statesMapping.get(successInlined), "",
					"1 - " + ifaceID);
		}

		this.createTransition(statesMapping.get(initialInlined),
				fragmentStatesMapping.get(initialFragment), "", ifaceID);
		this.createTransition(fragmentStatesMapping.get(successFragment),
				statesMapping.get(successInlined), "", "1");

		if (errorFragment != null) {
			this.createTransition(fragmentStatesMapping.get(errorFragment),
					statesMapping.get(errorInlined), "", "1");
		}
	}

	private void inlineInterfaceWithVariability(Interface iface,
			FDTMC fragment, Map<State, State> statesMapping) {
		chooseInlineInterface(iface, fragment, statesMapping, true);
	}

	private Set<Transition> getInterfaceTransitions() {
        Set<Transition> transitions = new HashSet<Transition>();
        interfaces.values().stream().flatMap(List<Interface>::stream)
                .forEach(iface -> {
                    transitions.add(iface.getSuccessTransition());
                    transitions.add(iface.getErrorTransition());
                });
        return transitions;
    }
}
