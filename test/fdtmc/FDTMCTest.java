package fdtmc;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
public class FDTMCTest {

	FDTMC fdtmc1;
	private final int MAX_STATE_AMOUNT = 6;
	private State[] states = new State[MAX_STATE_AMOUNT];
	private String[] messages = {"init", "success", "error"};

	private void createStateWithMessage(int aux) {
		for (int i = 0; i < aux; i++) {
			states[i] = fdtmc1.createState(messages[i]);
		}
	}
	
	@Before
	public void setUp() throws Exception {
		fdtmc1 = new FDTMC();
	}

	@Test
	public void testEmptyFDTMC() {
		Assert.assertTrue(fdtmc1.getStates().isEmpty());
		Assert.assertNull(fdtmc1.getInitialState());
		Assert.assertEquals(0, fdtmc1.getVariableIndex());
	}

	/**
	 * This test ensures if a state is created accordingly. The returned state must be
	 * different than null, it must be inside the FDTMC's states set, it must have the
	 * index equals to 0 (it is the first state) and it variable name must be equals to
	 *  name defined.
	 */
	@Test
	public void testCreateState() {
		fdtmc1.setVariableName("x");
		states[0] = fdtmc1.createState();
		Assert.assertNotNull(states[0]);
		Assert.assertTrue(fdtmc1.getStates().contains(states[0]));
		Assert.assertEquals(0, states[0].getIndex());
		Assert.assertEquals("x", states[0].getVariableName());
		Assert.assertEquals(states[0], fdtmc1.getInitialState());
	}

	/**
	 * This test is similar to the test of creation a single state. However it ensures
	 * states created in sequence will have index value in sequence.
	 */
	@Test
	public void testCreateLotsOfStates() {
		fdtmc1.setVariableName("x");
	
		for (int i = 0; i < 6; i++) {
			states[i] = fdtmc1.createState();
			
			Assert.assertNotNull(states[i]);
			Assert.assertTrue(fdtmc1.getStates().contains(states[i]));
			Assert.assertEquals(i, states[i].getIndex());
		}
		
		Assert.assertEquals(states[0], fdtmc1.getInitialState());
	}
	
	/**
	 * This test ensures we can set a label to a state. It doesn't do too much,
	 * but it was useful to create the labeling function for states.
	 */
	@Test
	public void testCreateLabeledState() {
		fdtmc1.setVariableName("x");

		createStateWithMessage(3);
		
		for (int i = 0; i < 3; i++) {
			Assert.assertEquals(messages[i], states[i].getLabel());
		}

		Assert.assertEquals(states[0], fdtmc1.getInitialState());
	}

	/**
	 * This test ensures we can create transitions between FDTMC's states, passing the states,
	 * transition name and probability value as parameters.
	 */
	@Test
	public void testCreateTransition() {
		createStateWithMessage(3);
		
		Assert.assertNotNull(fdtmc1.createTransition(states[0], states[1], "alpha", Double.toString(0.95)));
		Assert.assertNotNull(fdtmc1.createTransition(states[0], states[2], "alpha", Double.toString(0.05)));
	}
	
	/**
	 * This test is similar to the test above (testCreateTransition), however it test if the
	 * creation of transitions with parameters instead of real values works accordingly.
	 */
	@Test
	public void testCreateTransitionWithParameter() {
		createStateWithMessage(3);
		
		Assert.assertNotNull(fdtmc1.createTransition(states[0], states[1], "alpha", "rAlpha"));
		Assert.assertNotNull(fdtmc1.createTransition(states[0], states[2], "alpha", "1-rAlpha"));
	}
	
	/**
	 * This test ensures a created state can be recovered by its label.
	 */
	@Test
	public void testGetStateByLabel() {
		createStateWithMessage(3);

		states[3] = fdtmc1.getStateByLabel("init");
		states[4] = fdtmc1.getStateByLabel("success");
		states[5] = fdtmc1.getStateByLabel("error");
		
		Assert.assertSame(states[3], states[0]);
		Assert.assertSame(states[4], states[1]);
		Assert.assertSame(states[5], states[2]);
	}
	/**
	 * This test ensures it is possible to recover a transition (and all of its information like
	 * probability and source and target states) by using its name.
	 */
	@Test
	public void testGetTransitionByActionName() {			
		createStateWithMessage(3);

		assertCreateTransitionByActionName(states[0], states[1], states[2]);
		assertTransitionByActionName1(states[0], states[1]); 
		assertTransitionByActionName2(states[0], states[2]);		
	}

	public void assertCreateTransitionByActionName(State s0, State s1, State s2) {
		Transition createTransition1, createTransition2;
		
		createTransition1 = fdtmc1.createTransition(s0, s1, "alpha", "rAlpha");
		createTransition2 = fdtmc1.createTransition(s0, s2, "alpha_error", "1-rAlpha");
		
		Assert.assertNotNull(createTransition1);
		Assert.assertNotNull(createTransition2);
	}
	
	public void assertTransitionByActionName1(State s0, State s1) {
		Transition t1;
		t1 = fdtmc1.getTransitionByActionName("alpha");
		
		Assert.assertNotNull(t1);
		Assert.assertEquals("alpha", t1.getActionName());
		Assert.assertEquals("rAlpha", t1.getProbability());
		Assert.assertSame(s0, t1.getSource());
		Assert.assertSame(s1, t1.getTarget());

	}

	public void assertTransitionByActionName2(State s0, State s2) {
		Transition t2;
		t2 = fdtmc1.getTransitionByActionName("alpha_error");

		Assert.assertNotNull(t2);
		Assert.assertEquals("alpha_error", t2.getActionName());
		Assert.assertEquals("1-rAlpha", t2.getProbability());
		Assert.assertSame(s0, t2.getSource());
		Assert.assertSame(s2, t2.getTarget());

	}
	/**
	 * This test must ensure that the FDTMC will be printed (or builded) considering the order
	 * the states and transitions were build.
	 */
	@Test
	public void testPrintOrderedFDTMC (){
		FDTMC fdtmc = new FDTMC();
		fdtmc.setVariableName("sSqlite");
		State init = fdtmc.createState("init"),
			  success = fdtmc.createState("success"),
			  error = fdtmc.createState("fail"),
			  source,
			  target;

		source = init;
		target = fdtmc.createState();
		
		assertTestPrintOrderedFDTMC(fdtmc, source, target, error);
		
		source = target;
		target = success;
				
		assertTestPrintOrderedFDTMC1(fdtmc, source, target, error, success);
		
		String expectedAnswer = "sSqlite=0(init) --- persist / 0.999 ---> sSqlite=3" + '\n'
				+ "sSqlite=0(init) --- persist / 0.001 ---> sSqlite=2(fail)" + '\n'
				+ "sSqlite=1(success) ---  / 1.0 ---> sSqlite=1(success)" + '\n'
				+ "sSqlite=2(fail) ---  / 1.0 ---> sSqlite=2(fail)" + '\n'
				+ "sSqlite=3 --- persist_return / 0.999 ---> sSqlite=1(success)" + '\n'
				+ "sSqlite=3 --- persist_return / 0.001 ---> sSqlite=1(success)" + '\n';

		Assert.assertEquals(expectedAnswer, fdtmc.toString());
	}

	public void assertTestPrintOrderedFDTMC(FDTMC fdtmc, State source, State target, State error) {
		Assert.assertNotNull(fdtmc.createTransition(source, target, "persist", "0.999"));
		Assert.assertNotNull(fdtmc.createTransition(source, error, "persist", "0.001"));

	}
	
	public void assertTestPrintOrderedFDTMC1(FDTMC fdtmc, State source, State target, 
											State error, State success) {
		Assert.assertNotNull(fdtmc.createTransition(source, target, "persist_return", "0.999"));
		Assert.assertNotNull(fdtmc.createTransition(source, target, "persist_return", "0.001"));

		Assert.assertNotNull(fdtmc.createTransition(success, success, "", "1.0"));
		Assert.assertNotNull(fdtmc.createTransition(error, error, "", "1.0"));

	}
	
	/**
	 * This test aims to ensure if the DOT file for a specific RDG node is being created accordingly.
	 */
	@Test
	public void testCreateDotFile() {
		fail("Not yet implemented");
	}	
	
	@Test
	public void testEquivalentFDTMCs() {
	    FDTMC fdtmc1 = new FDTMC();
        fdtmc1.setVariableName("s");
        
        states[0] = fdtmc1.createInitialState(); // init1
        states[1] = fdtmc1.createSuccessState(); // success1
        states[2] = fdtmc1.createErrorState(); // error1
        
        /*
         * Use states[3] = source
         * Use states[4] = target
         * Use states[5] = interface_error
         * */
   
//      createTransitionAssist(states[3], states[4], states[2], states[0], "persist");
        
        states[3] = states[0];
        states[4] = fdtmc1.createState();
        fdtmc1.createTransition(states[3], states[4], "persist", "0.999");
        fdtmc1.createTransition(states[3], states[2], "!persist", "0.001");

//      createStateAssist("F", fdtmc1, states[2]);
        
        states[3] = states[4];
        states[4] = fdtmc1.createState();
        states[5] = fdtmc1.createState();
        fdtmc1.createInterface("F", states[3], states[4], states[5]);

        fdtmc1.createTransition(states[5], states[2], "error_ground", "1");

        states[3] = states[4];
        states[4] = states[1];
        fdtmc1.createTransition(states[3], states[4], "persist_return", "0.999");
        fdtmc1.createTransition(states[3], states[2], "!persist_return", "0.001");

        FDTMC fdtmc2 = new FDTMC();
        fdtmc2.setVariableName("v");
        State init2 = fdtmc2.createInitialState(),
              success2 = fdtmc2.createSuccessState(),
              error2 = fdtmc2.createErrorState();

        states[3] = init2;
        states[4] = fdtmc2.createState();
        fdtmc2.createTransition(states[3], states[4], "msg", "0.999");
        fdtmc2.createTransition(states[3], error2, "!msg", "0.001");

        states[3] = states[4];
        states[4] = fdtmc2.createState();
        states[5] = fdtmc2.createState();
        fdtmc2.createInterface("G", states[3], states[4], states[5]);

        fdtmc2.createTransition(states[5], error2, "error_ground", "1");

        states[3] = states[4];
        states[4] = success2;
        fdtmc2.createTransition(states[3], states[4], "msg_return", "0.999");
        fdtmc2.createTransition(states[3], error2, "!msg_return", "0.001");
        
        assertTestEquivalentFDTMCs(states[0], init2, states[1], error2, fdtmc1, fdtmc2);

	}
	
	private void createTransitionAssist(State source, State target, State error, State other, String message) {
		source = other;
        target = fdtmc1.createState();
        fdtmc1.createTransition(source, target, message, "0.999");
        fdtmc1.createTransition(source, error, "!" + message, "0.001");
	}
	
	private void createStateAssist(String letter, FDTMC fdtmc, State error) {
		states[3] = states[4];
        states[4] = fdtmc.createState();
        states[5] = fdtmc.createState();
        fdtmc.createInterface(letter, states[3], states[4], states[5]);

        fdtmc.createTransition(states[5], error, "error_ground", "1");
	}
	
	public void assertTestEquivalentFDTMCs(State init1, State init2, State success1, 
										   State error2, FDTMC fdtmc1, FDTMC fdtmc2) {
		Assert.assertEquals("FDTMCs' states should be compared disregarding variable names",
                init1, init2);
        Assert.assertNotEquals("FDTMCs' states should be compared disregarding variable names",
                success1, error2);

        Assert.assertEquals("FDTMCs' states should be compared disregarding variable names",
                fdtmc1.getStates(), fdtmc2.getStates());

        Assert.assertEquals("FDTMCs should be compared disregarding actions' names, interfaces' names and variable names",
                fdtmc1, fdtmc2);
	}

}
