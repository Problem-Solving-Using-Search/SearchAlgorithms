package Uninformed.Unweighted;
import General.AbstractSearchAlgo;
import General.Operator;
import General.Problem;
import General.IState;
import Problems.PathFinding.AutoCarNxN;

import java.util.*;

public class BFS extends AbstractSearchAlgo {

    Hashtable<String, IState> closed_list;
    Hashtable<String, IState> open_list;

    /**
     * assumes that problem gives the operations in clockwise order
     * @param problem
     * @param clockwise if to generate the nodes clockwise for true or counter-clockwise otherwise
     * @return
     */
    public AbstractList<IState> run(Problem problem, boolean clockwise)
    {
        LinkedList<IState> path = new LinkedList<>();
        closed_list = new Hashtable<>();
        open_list = new Hashtable<>();

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
            else
            // If we didn't find the goal, expand the current node
            {
                List<Operator> operators;
                if(clockwise)
                    operators = Arrays.asList(problem.getOperators());
                else // if we want counter clockwise then reverse the operators's places
                    operators = AutoCarNxN.ReverseOrder(problem.getOperators());
                for(Operator op: operators)
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
        }
        // if we haven't found any goal in the graph
        return null;
    }

}
