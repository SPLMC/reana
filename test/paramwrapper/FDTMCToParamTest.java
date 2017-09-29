package paramwrapper;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;import org.junit.experimental.max.MaxCore;

import fdtmc.FDTMC;
import fdtmc.State;
import paramwrapper.ParamWrapper;

public class FDTMCToParamTest {
    private static final String PARAM_PATH = "/opt/param-2-3-64";
    private FDTMC fdtmc;
    private State[] states;
    private final int MAX_STATES = 3;

  ParamWrapper paramWrapper;

  @Before
  public void setUp() throws Exception {
    paramWrapper = new ParamWrapper(PARAM_PATH);
    fdtmc = new FDTMC();
    states = new State[MAX_STATES];
  }

  @Test
  public void testSingletonFDTMC() {
    fdtmc.setVariableName("s");
    states[0] = fdtmc.createState();
    fdtmc.createTransition(states[0], states[0], null, "1");

    String expectedModule =
        "dtmc\n"
        + "\n\n"
        + "module dummyModule\n"
        + "  s : [0..1] init 0;\n"
        + "  [] s=0 -> (1) : (s'=0);\n"
        + "endmodule\n\n";

    assertEquals(expectedModule, paramWrapper.fdtmcToParam(fdtmc));
  }

  @Test
  public void testSingletonFDTMCWithLabel() {
    fdtmc.setVariableName("s");
    State s = fdtmc.createState("success");
    fdtmc.createTransition(s, s, null, "1");

    String expectedModule =
        "dtmc\n"
        + "\n\n"
        + "module dummyModule\n"
        + "  s : [0..1] init 0;\n"
        + "  [] s=0 -> (1) : (s'=0);\n"
        + "endmodule\n"
        + "\n"
        + "label \"success\" = s=0;\n";

    assertEquals(expectedModule, paramWrapper.fdtmcToParam(fdtmc));
  }

  @Test
  public void testSimpleFDTMCWithParameters() {
    fdtmc.setVariableName("s");
    State s0 = fdtmc.createState();
    State s1 = fdtmc.createState();
    fdtmc.createTransition(s0, s0, null, "rLoop");
    fdtmc.createTransition(s0, s1, null, "1-rLoop");
    fdtmc.createTransition(s1, s1, null, "1");

    String expectedModule =
        "dtmc\n"
        + "\n"
        + "param double rLoop;\n"
        + "\n"
        + "module dummyModule\n"
        + "  s : [0..1] init 0;\n"
        + "  [] s=0 -> (rLoop) : (s'=0) + (1-rLoop) : (s'=1);\n"
        + "  [] s=1 -> (1) : (s'=1);\n"
        + "endmodule\n\n";

    assertEquals(expectedModule, paramWrapper.fdtmcToParam(fdtmc));
  }


  @Test
  public void testSimpleFDTMCWithParametersAndLabels() {
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

    String expectedModule =
        "dtmc\n"
        + "\n"
        + "param double rFail;\n"
        + "param double rLoop;\n"
        + "\n"
        + "module dummyModule\n"
        + "  s : [0..3] init 0;\n"
        + "  [] s=0 -> (rLoop) : (s'=0) + (1-rLoop) : (s'=1);\n"
        + "  [] s=1 -> (1-rFail) : (s'=2) + (rFail) : (s'=3);\n"
        + "  [] s=2 -> (1) : (s'=2);\n"
        + "  [] s=3 -> (1) : (s'=3);\n"
        + "endmodule\n"
        + "\n"
        + "label \"error\" = s=3;\n"
        + "label \"success\" = s=1 | s=2;\n";

    assertEquals(expectedModule, paramWrapper.fdtmcToParam(fdtmc));
  }

  // Many states with one label
}