package fdtmc;

public class InitialStateInlineInterface {

  private Interface iface;
  private FDTMC fragment;

  public State getInitialInlined() {
    return this.iface.getInitial();
  }

  public State getInitialFragment() {
    return this.fragment.getInitialState();
  }
  public State getSuccessInlined() {
    return this.iface.getSuccess();
  }
  public State getSuccessFragment() {
    return this.fragment.getSuccessState();
  }
  public State getErrorInlined() {
    return this.iface.getError();
  }
  public State getErrorFragment() {
    return this.fragment.getErrorState();
  }

  public InitialStateInlineInterface(Interface iface, FDTMC fragment) {
    this.iface = iface;
    this.fragment = fragment;
  }

}
