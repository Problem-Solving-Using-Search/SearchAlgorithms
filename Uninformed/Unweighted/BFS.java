package Uninformed.Unweighted;
import General.Operator;
import General.Problem;
import General.IState;

import java.util.AbstractList;
import java.util.Stack;

public class BFS {

    public static AbstractList<IState> run(Problem problem)
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
            else
            // If we didn't find the goal, expand the current node
            {

                for(Operator op: problem.getOperators())
                {
                    if(op.CheckPossible(cur_state))
                        path.add(op.operate(cur_state));
                }
            }
        }
        // if we haven't found any goal in the graph
        return null;
    }


}
