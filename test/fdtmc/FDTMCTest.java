 package fdtmc;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDTMCTest {

	FDTMC fdtmc;

	@Before
	public void setUp() throws Exception {
		fdtmc = new FDTMC();
	}

	@Test
	public void testEmptyFDTMC() {
		Assert.assertTrue(fdtmc.getStates().isEmpty());
		Assert.assertNull(fdtmc.getInitialState());
		Assert.assertEquals(0, fdtmc.getVariableIndex());
	}

	/**
	 * This test ensures if a state is created accordingly. The returned state must be
	 * different than null, it must be inside the FDTMC's states set, it must have the
	 * index equals to 0 (it is the first state) and it variable name must be equals to
	 *  name defined.
	 */
	@Test
	public void testCreateState() {
		fdtmc.setVariableName("x");
		State temp = fdtmc.createState();
		Assert.assertNotNull(temp);
		Assert.assertTrue(fdtmc.getStates().contains(temp));
		Assert.assertEquals(0, temp.getIndex());
		Assert.assertEquals("x", temp.getVariableName());
		Assert.assertEquals(temp, fdtmc.getInitialState());
	}

	/**
	 * This test is similar to the test of creation a single state. However it ensures
	 * states created in sequence will have index value in sequence.
	 */
	@Test
	public void testCreateLotsOfStates() {
		fdtmc.setVariableName("x");
		State s0, s1, s2, s3, s4, s5;
		s0 = fdtmc.createState();
		s1 = fdtmc.createState();
		s2 = fdtmc.createState();
		s3 = fdtmc.createState();
		s4 = fdtmc.createState();
		s5 = fdtmc.createState();

		Assert.assertNotNull(s0);
		Assert.assertNotNull(s1);
		Assert.assertNotNull(s2);
		Assert.assertNotNull(s3);
		Assert.assertNotNull(s4);
		Assert.assertNotNull(s5);

		Assert.assertTrue(fdtmc.getStates().contains(s0));
		Assert.assertTrue(fdtmc.getStates().contains(s1));
		Assert.assertTrue(fdtmc.getStates().contains(s2));
		Assert.assertTrue(fdtmc.getStates().contains(s3));
		Assert.assertTrue(fdtmc.getStates().contains(s4));
		Assert.assertTrue(fdtmc.getStates().contains(s5));

		Assert.assertEquals(0, s0.getIndex());
		Assert.assertEquals(1, s1.getIndex());
		Assert.assertEquals(2, s2.getIndex());
		Assert.assertEquals(3, s3.getIndex());
		Assert.assertEquals(4, s4.getIndex());
		Assert.assertEquals(5, s5.getIndex());

		Assert.assertEquals(s0, fdtmc.getInitialState());
	}


	/**
	 * This test ensures we can set a label to a state. It doesn't do too much,
	 * but it was useful to create the labeling function for states.
	 */
	@Test
	public void testCreateLabeledState() {
		fdtmc.setVariableName("x");
		State s0, s1, s2;

		s0 = fdtmc.createState("init");
		s1 = fdtmc.createState("sucess");
		s2 = fdtmc.createState("error");

		Assert.assertEquals("init", s0.getLabel());
		Assert.assertEquals("sucess", s1.getLabel());
		Assert.assertEquals("error", s2.getLabel());

		Assert.assertEquals(s0, fdtmc.getInitialState());
	}


	/**
	 * This test ensures we can create transitions between FDTMC's states, passing the states,
	 * transition name and probability value as parameters.
	 */
	@Test
	public void testCreateTransition() {
		State s0, s1, s2;
		s0 = fdtmc.createState("init");
		s1 = fdtmc.createState("success");
		s2 = fdtmc.createState("error");

		Assert.assertNotNull(fdtmc.createTransition(s0, s1, "alpha", Double.toString(0.95)));
		Assert.assertNotNull(fdtmc.createTransition(s0, s2, "alpha", Double.toString(0.05)));
	}


	/**
	 * This test is similar to the test above (testCreateTransition), however it test if the
	 * creation of transitions with parameters instead of real values works accordingly.
	 */
	@Test
	public void testCreateTransitionWithParameter() {
		State s0, s1, s2;
		s0 = fdtmc.createState("init");
		s1 = fdtmc.createState("success");
		s2 = fdtmc.createState("error");

		Assert.assertNotNull(fdtmc.createTransition(s0, s1, "alpha", "rAlpha"));
		Assert.assertNotNull(fdtmc.createTransition(s0, s2, "alpha", "1-rAlpha"));
	}


	/**
	 * This test ensures a created state can be recovered by its label.
	 */
	@Test
	public void testGetStateByLabel() {
		State s0, s1, s2;
		s0 = fdtmc.createState("init");
		s1 = fdtmc.createState("success");
		s2 = fdtmc.createState("error");

		State t0, t1, t2;
		t0 = fdtmc.getStateByLabel("init");
		t1 = fdtmc.getStateByLabel("success");
		t2 = fdtmc.getStateByLabel("error");

		Assert.assertSame(t0, s0);
		Assert.assertSame(t1, s1);
		Assert.assertSame(t2, s2);
	}


	/**
	 * This test ensures it is possible to recover a transition (and all of its information like
	 * probability and source and target states) by using its name.
	 */
	@Test
	public void testGetTransitionByActionName() {
		State s0, s1, s2;
		s0 = fdtmc.createState("init");
		s1 = fdtmc.createState("sucess");
		s2 = fdtmc.createState("error");

		Assert.assertNotNull(fdtmc.createTransition(s0, s1, "alpha", "rAlpha"));
		Assert.assertNotNull(fdtmc.createTransition(s0, s2, "alpha_error", "1-rAlpha"));

		Transition t1, t2;
		t1 = fdtmc.getTransitionByActionName("alpha");
		t2 = fdtmc.getTransitionByActionName("alpha_error");

		Assert.assertNotNull(t1);
		Assert.assertEquals("alpha", t1.getActionName());
		Assert.assertEquals("rAlpha", t1.getProbability());
		Assert.assertSame(s0, t1.getSource());
		Assert.assertSame(s1, t1.getTarget());

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
		Assert.assertNotNull(fdtmc.createTransition(source, target, "persist", "0.999"));
		Assert.assertNotNull(fdtmc.createTransition(source, error, "persist", "0.001"));

		source = target;
		target = success;
		Assert.assertNotNull(fdtmc.createTransition(source, target, "persist_return", "0.999"));
		Assert.assertNotNull(fdtmc.createTransition(source, target, "persist_return", "0.001"));

		Assert.assertNotNull(fdtmc.createTransition(success, success, "", "1.0"));
		Assert.assertNotNull(fdtmc.createTransition(error, error, "", "1.0"));


		String expectedAnswer = "sSqlite=0(init) --- persist / 0.999 ---> sSqlite=3" + '\n'
				+ "sSqlite=0(init) --- persist / 0.001 ---> sSqlite=2(fail)" + '\n'
				+ "sSqlite=1(success) ---  / 1.0 ---> sSqlite=1(success)" + '\n'
				+ "sSqlite=2(fail) ---  / 1.0 ---> sSqlite=2(fail)" + '\n'
				+ "sSqlite=3 --- persist_return / 0.999 ---> sSqlite=1(success)" + '\n'
				+ "sSqlite=3 --- persist_return / 0.001 ---> sSqlite=1(success)" + '\n';

		Assert.assertEquals(expectedAnswer, fdtmc.toString());
	}





	/**
	 * This test aims to ensure if the DOT file for a specific RDG node is being created accordingly.
	 */
	@Test
	public void testCreateDotFile() {
		fail("Not yet implemented");


	}
	
	private ArrayList<State> testingFDTMC(FDTMC fdtmc, String interfaceId){
        State init = fdtmc.createInitialState(),
              success = fdtmc.createSuccessState(),
              error = fdtmc.createErrorState(),
              source,
              target,
              interface_error;

        source = init;
        target = fdtmc.createState();
        fdtmc.createTransition(source, target, "persist", "0.999");
        fdtmc.createTransition(source, error, "!persist", "0.001");

        source = target;
        target = fdtmc.createState();
        interface_error = fdtmc.createState();
        fdtmc.createInterface(interfaceId, source, target, interface_error);

        fdtmc.createTransition(interface_error, error, "error_ground", "1");

        source = target;
        target = success;
        fdtmc.createTransition(source, target, "persist_return", "0.999");
        fdtmc.createTransition(source, error, "!persist_return", "0.001");
        
        ArrayList<State> states = new ArrayList<State>();
        
        states.add(init);
        states.add(success);
        states.add(error);
        states.add(source);
        states.add(target);
        states.add(interface_error);
        
		return states;
	}

	@Test
	public void testEquivalentFDTMCs() {
		
	    FDTMC firstFdtmc = new FDTMC();
        firstFdtmc.setVariableName("s");
        
        ArrayList<State> firstFdtmcStates = testingFDTMC(firstFdtmc, "F");
        
        FDTMC secondFdtmc = new FDTMC();
        secondFdtmc.setVariableName("v");
        
        ArrayList<State> secondFdtmcStates = testingFDTMC(secondFdtmc, "G");

        Assert.assertEquals("FDTMCs' states should be compared disregarding variable names",
                firstFdtmcStates.get(0), secondFdtmcStates.get(0));
        Assert.assertNotEquals("FDTMCs' states should be compared disregarding variable names",
        		firstFdtmcStates.get(1), secondFdtmcStates.get(2));

        Assert.assertEquals("FDTMCs' states should be compared disregarding variable names",
                firstFdtmc.getStates(), secondFdtmc.getStates());

        Assert.assertEquals("FDTMCs should be compared disregarding actions' names, interfaces' names and variable names",
                firstFdtmc, secondFdtmc);
	}

}
