package paramwrapper;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fdtmc.FDTMC;
import fdtmc.State;
import paramwrapper.ParamWrapper;

public class FDTMCToParamTest {
    private static final String PARAM_PATH = "/opt/param-2-3-64";

	ParamWrapper paramWrapper;

	@Before
	public void setUp() throws Exception {
		paramWrapper = new ParamWrapper(PARAM_PATH);
	}

	@Test
	public void testSingletonFDTMC() {
		FDTMC singletonFDTMC = getFDTMCObject();

		String[] labels = {null};
		State[] s = getStates(labels, singletonFDTMC);

		singletonFDTMC.createTransition(s[0], s[0], null, "1");

		String expectedModule =
				"dtmc\n"
				+ "\n\n"
				+ "module dummyModule\n"
				+ "	s : [0..1] init 0;\n"
				+ "	[] s=0 -> (1) : (s'=0);\n"
				+ "endmodule\n\n";

		assertEquals(expectedModule, paramWrapper.fdtmcToParam(singletonFDTMC));
	}

	@Test
	public void testSingletonFDTMCWithLabel() {
		FDTMC singletonFDTMC = getFDTMCObject();

		String[] labels = {"success"};
		State[] s = getStates(labels, singletonFDTMC);

		singletonFDTMC.createTransition(s[0], s[0], null, "1");


		String expectedModule =
				"dtmc\n"
				+ "\n\n"
				+ "module dummyModule\n"
				+ "	s : [0..1] init 0;\n"
				+ "	[] s=0 -> (1) : (s'=0);\n"
				+ "endmodule\n"
				+ "\n"
				+ "label \"success\" = s=0;\n";

		assertEquals(expectedModule, paramWrapper.fdtmcToParam(singletonFDTMC));
	}

	@Test
	public void testSimpleFDTMCWithParameters() {
		FDTMC fdtmc = getFDTMCObject();
		
		String[] labels = {null, null};
		State[] s = getStates(labels, fdtmc);

		fdtmc.createTransition(s[0], s[0], null, "rLoop");
		fdtmc.createTransition(s[0], s[1], null, "1-rLoop");
		fdtmc.createTransition(s[1], s[1], null, "1");

		String expectedModule =
				"dtmc\n"
				+ "\n"
				+ "param double rLoop;\n"
				+ "\n"
				+ "module dummyModule\n"
				+ "	s : [0..1] init 0;\n"
				+ "	[] s=0 -> (rLoop) : (s'=0) + (1-rLoop) : (s'=1);\n"
				+ "	[] s=1 -> (1) : (s'=1);\n"
				+ "endmodule\n\n";

		assertEquals(expectedModule, paramWrapper.fdtmcToParam(fdtmc));
	}


	@Test
	public void testSimpleFDTMCWithParametersAndLabels() {
		FDTMC fdtmc = getFDTMCObject();
		
		String[] labels = {null, "success", "success", "error"};
		State[] s = getStates(labels, fdtmc);

		fdtmc.createTransition(s[0], s[0], null, "rLoop");
		fdtmc.createTransition(s[0], s[1], null, "1-rLoop");
		fdtmc.createTransition(s[1], s[2], null, "1-rFail");
		fdtmc.createTransition(s[1], s[3], null, "rFail");
		fdtmc.createTransition(s[2], s[2], null, "1");
		fdtmc.createTransition(s[3], s[3], null, "1");

		String expectedModule =
				"dtmc\n"
				+ "\n"
				+ "param double rFail;\n"
				+ "param double rLoop;\n"
				+ "\n"
				+ "module dummyModule\n"
				+ "	s : [0..3] init 0;\n"
				+ "	[] s=0 -> (rLoop) : (s'=0) + (1-rLoop) : (s'=1);\n"
				+ "	[] s=1 -> (1-rFail) : (s'=2) + (rFail) : (s'=3);\n"
				+ "	[] s=2 -> (1) : (s'=2);\n"
				+ "	[] s=3 -> (1) : (s'=3);\n"
				+ "endmodule\n"
				+ "\n"
				+ "label \"error\" = s=3;\n"
				+ "label \"success\" = s=1 | s=2;\n";

		assertEquals(expectedModule, paramWrapper.fdtmcToParam(fdtmc));
	}
	
	private FDTMC getFDTMCObject() {
		FDTMC fdtmc = new FDTMC();
		fdtmc.setVariableName("s");
		return fdtmc;
	}
	
	private State[] getStates(String[] labels, FDTMC fdtmc) {
		State[] states = new State[labels.length];
		
		for (int i = 0; i < labels.length; i++) {
			if (labels[i] == null) {
				states[i] = fdtmc.createState();
			} else {
				states[i] = fdtmc.createState(labels[i]);						
			}
		}
		
		return states;
	}

}
