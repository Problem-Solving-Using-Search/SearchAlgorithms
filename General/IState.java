package General;

import Problems.PathFinding.nPuzzle;

import java.util.LinkedList;

/**
 * The state of the current algorithm, means that the current node working on
 */
public interface IState {

    boolean equals(IState state2);

    /**
     *
     * @return returns the path from the current node to the node which his parent is himself
     */
    default LinkedList<IState> GetPath()
    {
        LinkedList<IState> linkedList = new LinkedList<>();
        IState state = this;
        // check if parent not equals son
        while(!state.equals(state.GetParent()))
        {
            linkedList.add(this);
            state = state.GetParent();
        }
        if(linkedList.size() == 0) // if a path contains one node
            linkedList.add(state);
        return linkedList;
    }
     void SetParent(IState state);
     IState GetParent();
     String toString();

}

