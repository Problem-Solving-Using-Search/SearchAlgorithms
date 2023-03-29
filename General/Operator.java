package General;

/**
 * just a function from one state to another state
 */
public interface Operator {

    public IState operate(IState s);

    /**
     * Checks if it's possible to operate this operator on this state
     * @param s
     * @return
     */
    public boolean CheckPossible(IState s);

}
