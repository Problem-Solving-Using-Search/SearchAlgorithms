package Uninformed.Unweighted;

import General.ISearchAlgo;
import General.IState;
import General.Operator;
import General.Problem;

import java.util.AbstractList;
import java.util.LinkedList;
import java.util.Stack;

public class DFS implements ISearchAlgo {
    /**
     * Note, there is no loop avoidance in this function, meaning that this might run forever
     * @param problem
     * @return
     */
    public AbstractList<IState> run(Problem problem, boolean clockwise)
    {
        Stack<IState> path = new Stack<>();
        path.add(problem.getInit());
        IState end = problem.getEnd();
        while(!path.isEmpty())
        {
            IState cur_state = path.pop();
            if(cur_state.equals(end))
            // if we found the goal
            {
                return cur_state.GetPath();
            }
            // If we didn't find the goal, expand the current node
            for(Operator op: problem.getOperators())
            {
                // check possibility of deriving this state
                if(op.CheckPossible(cur_state))
                {
                    IState resulted_state = op.operate(cur_state);
                    if(resulted_state.equals(end))
                    // if we found the goal
                    {
                        return resulted_state.GetPath();
                    }
                    path.add(resulted_state);
                }
            }
        }
        // if we haven't found any goal in the graph
        return null;
    }



}
