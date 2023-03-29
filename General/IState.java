package General;

import java.util.LinkedList;

/**
 * The state of the current algorithm, means that the current node working on
 */
public interface IState {

    public boolean compare(IState state2);
    public LinkedList<IState> GetPath();
    public void SetParent(IState state);
    public String toString();

}

