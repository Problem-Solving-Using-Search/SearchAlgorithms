package Uninformed.Unweighted;

import General.IState;
import General.Operator;
import General.Problem;

import java.util.LinkedList;
import java.util.Stack;

public class DFS {
    /**
     * Note, there is no loop avoidance in this function, meaning that this might run forever
     * @param problem
     * @return
     */
    public static LinkedList<IState> run(Problem problem)
    {
        Stack<IState> path = new Stack<>();
        path.add(problem.getInit());
        IState end = problem.getEnd();
        while(!path.isEmpty())
        {
            IState cur_state = path.pop();
            if(cur_state.compare(end))
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
                    if(resulted_state.compare(end))
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
