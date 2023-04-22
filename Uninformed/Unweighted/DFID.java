package Uninformed.Unweighted;

import General.ISearchAlgo;
import General.IState;
import General.Operator;
import General.Problem;
import Problems.PathFinding.AutoCarNxN;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DFID implements ISearchAlgo {
    public LinkedList<IState> run(Problem problem, boolean clockwise)
    {
        for(int i=1; i< 1000; i++)
        {
            HashMap<String,Boolean> hash = new HashMap<>();
            hash.put(problem.getInit().toString(),true);
            LinkedList<IState> path = LimitedDFSLA(problem, i, problem.getInit(), hash, clockwise);
            if(path == null || !path.isEmpty())
            {
                return path;
            }
        }
        return null;
    }

    /**
     * Limited DFS with loop avoidance
     * @param problem
     * @param limit
     * @param cur_node
     * @param hash
     * @return
     */
    private static LinkedList<IState> LimitedDFSLA(Problem problem, int limit,
                                                   IState cur_node, HashMap<String, Boolean> hash,
                                                   boolean clockwise)
    {
        boolean cutoff = false;
        if(cur_node.equals(problem.getEnd()))
        {
            // if we reached the goal
            return cur_node.GetPath();
        }
        else if(limit == 0)
        {
            // if we reached the cutoff
            return new LinkedList<>();
        }
        hash.put(cur_node.toString(), true);
        List<Operator> operators;
        if(!clockwise)
            operators = AutoCarNxN.ReverseOrder(problem.getOperators());
        else
            operators = Arrays.asList(problem.getOperators());
        // else
        for(Operator op : operators)
        {
            if(op.CheckPossible(cur_node))
            {
                IState gen_state = op.operate(cur_node);
                if(hash.containsKey(gen_state.toString()))
                {
                    continue;
                }
                LinkedList<IState> ret = LimitedDFSLA(problem,limit-1, gen_state , hash,clockwise);
                if(ret != null && !ret.isEmpty())
                {
                    return ret;
                }
                else if(ret != null)
                    cutoff = true;
            }
        }
        hash.remove(cur_node.toString());
        if(cutoff)
        {
            // cutoff
            return new LinkedList<>();
        }
        // couldn't find any
        return null;
    }
    private static LinkedList<IState> LimitedDFS(Problem problem, int limit, IState cur_node)
    {
        if(cur_node.equals(problem.getEnd()))
        {
            // if we reached the goal
            return cur_node.GetPath();
        }
        // else
        for(Operator op : problem.getOperators())
        {
            if(op.CheckPossible(cur_node))
            {
                LinkedList<IState> ret = LimitedDFS(problem,limit-1, op.operate(cur_node));
                if(ret != null && ret.size() != 0)
                {
                    return ret;
                }
            }
        }
        if(limit == 0)
        {
            // cutoff
            return new LinkedList<>();
        }
        // couldn't find any
        return null;
    }
}
