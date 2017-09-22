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
	
	/* This function is designed for creating a valid FDTMC for 
	 * testSingletonFDTMC() method, below.
	 * Input: None
	 * Output: A simple singleton FDTMC 
	 */
	
	private FDTMC getNewSingletonFDTMC() {
		FDTMC singletonFDTMC = new FDTMC();
		singletonFDTMC.setVariableName("s");
		State s = singletonFDTMC.createState();
		singletonFDTMC.createTransition(s, s, null, "1");
		
		return singletonFDTMC;
	}

	@Test
	public void testSingletonFDTMC() {
		FDTMC singletonFDTMC = getNewSingletonFDTMC();

		String expectedModule =
				"dtmc\n"
				+ "\n\n"
				+ "module dummyModule\n"
				+ "	s : [0..1] init 0;\n"
				+ "	[] s=0 -> (1) : (s'=0);\n"
				+ "endmodule\n\n";

		assertEquals(expectedModule, paramWrapper.fdtmcToParam(singletonFDTMC));
	}
	
	/* This function is designed for creating a valid FDTMC for 
	 * testSingletonFDTMCWithLabel() method, below.
	 * Input: None
	 * Output: A singleton FDTMC with Labels
	 */
	private FDTMC getNewFDTMCLabel() {
		FDTMC singletonFDTMC = new FDTMC();
		
		singletonFDTMC.setVariableName("s");
		State s = singletonFDTMC.createState("success");
		singletonFDTMC.createTransition(s, s, null, "1");
		
		return singletonFDTMC;
	}

	@Test
	public void testSingletonFDTMCWithLabel() {
		FDTMC singletonFDTMC = this.getNewFDTMCLabel();

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
	
	/* This function is designed for creating a valid FDTMC for 
	 * testSimpleFDTMCWithParameters() method, below.
	 * Input: None
	 * Output: A FDTMC with Parameters
	 */
	private FDTMC getNewFDTMCParamters() {
		FDTMC fdtmc = new FDTMC();
		fdtmc.setVariableName("s");
		State s0 = fdtmc.createState();
		State s1 = fdtmc.createState();
		fdtmc.createTransition(s0, s0, null, "rLoop");
		fdtmc.createTransition(s0, s1, null, "1-rLoop");
		fdtmc.createTransition(s1, s1, null, "1");
		
		return fdtmc;
	}

	@Test
	public void testSimpleFDTMCWithParameters() {
		FDTMC fdtmc = this.getNewFDTMCParamters();

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

	/* This function is designed for creating a valid FDTMC for 
	 * testSimpleFDTMCWithParametersAndLabels() method, below.
	 * Input: None
	 * Output: A FDTMC with Parameters and Labels
	 */
	private FDTMC getNewFDTMCParamtersLabels() {
		FDTMC fdtmc = new FDTMC();
		
		fdtmc.setVariableName("s");
		
		State s0 = fdtmc.createState();
		State s1 = fdtmc.createState("success");
		State s2 = fdtmc.createState("success");
		State s3 = fdtmc.createState("error");
		
		fdtmc.createTransition(s0, s0, null, "rLoop");
		fdtmc.createTransition(s0, s1, null, "1-rLoop");
		fdtmc.createTransition(s1, s2, null, "1-rFail");
		fdtmc.createTransition(s1, s3, null, "rFail");
		fdtmc.createTransition(s2, s2, null, "1");
		fdtmc.createTransition(s3, s3, null, "1");
		
		return fdtmc;
	}

	@Test
	public void testSimpleFDTMCWithParametersAndLabels() {
		FDTMC fdtmc = this.getNewFDTMCParamtersLabels();

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

	// Many states with one label
}
