package Uninformed.weighted;

import General.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;
import java.lang.Math;

public class IDAStar extends AbstractSearchAlgo {
    public static PrintWriter printer;

    static {
        try {
            printer = new PrintWriter(new FileWriter("Outputs/output"), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static class Node{
        public IState state;
        public double cost;
        public ArrayList<Operator> operators;
        public Node(IState state, double cost, ArrayList<Operator> operators)
        {
            this.state = state;
            this.cost = cost;
            this.operators = operators;
        }
    }
    @Override
    public AbstractList<IState> run(Problem problem, boolean clockwise, IHeuristic heuristic)
    {
        // TODO: change solution to as seen in the book
        Hashtable<String, Node> open_list = new Hashtable<>(); // I.E H
        Hashtable<String, Boolean> closed_list = new Hashtable<>();
        // starting node
        IState startNode = problem.getInit();
        // end goal node
        IState goalNode = problem.getEnd();
        // front stack representing nodes that needs to be explored ordered first to last by priority
        Stack<Node> frontier = new Stack<>();
        // first bound from start to the end node
        double bound = heuristic.calcH(startNode);
        // the costs of the farthest nodes that were explored
        double minFarthestCost;
        // tells if the successor exceeds the bound limit
        boolean reached_limit;
        // loop until we didn't bound any, meaning we searched all nodes possible
        while(bound != Double.POSITIVE_INFINITY)
        {
            // initializes the min farthest with the highest value
            minFarthestCost = Double.POSITIVE_INFINITY;
            // add all possible operations with cost 0 as start from the starting node
            Node start = new Node(startNode,0, problem.getPossibleOperators(startNode,clockwise));
            frontier.removeAllElements();
            open_list.clear();
            closed_list.clear();
            frontier.add(start);
            open_list.put(start.state.toString(), start);
            while(!frontier.isEmpty())
            {
                // get the fathest node in the path
                Node farthestNode = frontier.peek();
                if (farthestNode.operators.isEmpty())
                {
                    frontier.pop(); // if we finished extending the farthest node, then pop it out of the frontier
                    closed_list.put(farthestNode.state.toString(), true); // put it in the closed list
                    continue;
                }
                printer.println(farthestNode.state.toString());
                // remove the most relevant operator
                Operator extend = farthestNode.operators.remove(0);
                // make the next node's values
                IState successor_state = extend.operate(farthestNode.state);
                double cost = successor_state.getCost() + heuristic.calcH(successor_state); // h(n) + g(n)
                reached_limit = cost > bound;
                Node possible_suc_clone = open_list.getOrDefault( successor_state.toString(),null);
                if(possible_suc_clone != null &&
                    possible_suc_clone.cost > cost)
                {
                    // removes this node, and afterwards we put our node in
                    // the open list and in the frontier
                    frontier.remove(possible_suc_clone);
                }
                else if(possible_suc_clone != null &&
                        possible_suc_clone.cost <= cost)
                {
                    // skip current node, no need to work on him if we have him on smaller cost
                    continue;
                }
                // Check that this node is not in the closed list, if it is then we will NOT FIND THE GOAL,
                // since all it's possible paths have already been covered.
                else if(possible_suc_clone != null &&
                        closed_list.contains(possible_suc_clone.toString()))
                {
                    // SKIP THIS NODE, loop avoidance
                    continue;
                }
                // in case we found the goal, return the path found
                if(successor_state.equals(goalNode)) {
                    LinkedList<IState> path = successor_state.GetPath();
                    printer.println("-");
                    for(IState s : path)
                        printer.println(s.toString());
                    return path;
                }
                // in case we reached the bound limit then add the cost and do not add the successor back to the stack
                else if(reached_limit) {
                    minFarthestCost = Math.min(minFarthestCost, cost);
                }
                else
                {// if we did not reach the limit, continue extending the successor node
                    // add the next child/successor as the first to explore next.
                    Node successor_node = new Node(successor_state,
                            cost , // h(n) + g(n)
                            problem.getPossibleOperators(successor_state, clockwise));
                    frontier.push(successor_node);
                    open_list.put(successor_state.toString(), successor_node);
                }
            }
            // extends the search bound
            bound = minFarthestCost;
        }
        return null;
    }

}
