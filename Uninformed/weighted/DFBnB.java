package Uninformed.weighted;

import General.*;

import java.util.*;
import java.util.stream.Collectors;

public class DFBnB extends AbstractSearchAlgo {

    @Override
    public AbstractList<IState> run(Problem problem,boolean clockwise, IHeuristic heuristic)
    {
        IState startNode = problem.getInit();
        IState goalNode = problem.getEnd();

        Stack<IState> frontier = new Stack<>();
        Hashtable<String, IState> closed_list = new Hashtable();
        Hashtable<String, IState> open_list = new Hashtable<>();
        double bestPathCost = Double.POSITIVE_INFINITY;
        LinkedList<IState> bestPath = null;
        frontier.add(startNode);
        open_list.put(startNode.toString(), startNode);
        while(!frontier.empty())
        {
            IState farthestNode = frontier.pop();
            ArrayList<Operator> operators = problem.getPossibleOperators(farthestNode,clockwise);
            // maps each operator to operator(farthestNode), then sorts it
            ArrayList<IState> neighbors = operators.stream().
                    map(op -> op.operate(farthestNode)).
                    sorted((o1, o2) -> { // defines a comparator implementation
                double cost1 = o1.getCost() + heuristic.calcH(o1);
                double cost2 = o2.getCost() + heuristic.calcH(o2);
                if (cost1 == cost2) return 0;
                return cost1 < cost2 ? -1 : 1;
            }).collect(Collectors.toCollection(ArrayList::new));
            for(int i=0; i< neighbors.size(); i++)
            {
                IState neighbor = neighbors.get(i);
                double cost = neighbor.getCost() + heuristic.calcH(neighbor);
                IState neighbor_clone = open_list.getOrDefault(neighbor.toString(),null);
                // if we passed the path currently we're on then cut the path of this node
                if(cost >= bestPathCost) {
                    neighbors.removeAll(neighbors.subList(i,neighbors.size()));
                    break;
                }
                else if(neighbor_clone != null && closed_list.contains(neighbor))
                {
                    neighbors.remove(neighbor);
                }
                else if(neighbor_clone != null && !closed_list.contains(neighbor))
                {
                    if(neighbor_clone.getCost() + heuristic.calcH(neighbor_clone) > cost)
                    { // remove that guy he's worse than what we have now
                        frontier.remove(neighbor_clone);
                        open_list.remove(neighbor_clone.toString());
                    }
                    else
                    { // if our dude is worse than we can ignore him, so remove him from neighbors
                        neighbors.remove(neighbor);
                    }
                }
                else if(neighbor.equals(goalNode))
                { // we checked that our neighbor has a better score and it's a goal node, then make it the best path
                    bestPathCost = cost;
                    bestPath = neighbor.GetPath();
                    break;
                }
            }
            frontier.addAll(neighbors);

        }
        return bestPath;
    }


}
