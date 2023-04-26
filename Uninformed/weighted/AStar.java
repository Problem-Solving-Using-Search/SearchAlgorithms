package Uninformed.weighted;

import General.*;
import Problems.PathFinding.AutoCarNxN;

import java.io.*;
import java.util.*;

public class AStar extends AbstractSearchAlgo {
    public static PrintWriter printer;

    static {
        try {
            printer = new PrintWriter(new FileWriter("Outputs/output"), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class IStateComparator implements Comparator<IState> {
        public IHeuristic heuristic;
        public IState goal;
        public IStateComparator(IHeuristic heuristic, IState goal)
        {
            this.heuristic = heuristic;
            this.goal = goal;
        }

        @Override
        public int compare(IState o1, IState o2) {
            double o1cost = o1.getCost() + heuristic.calcH(o1),
            o2cost = o2.getCost() + heuristic.calcH(o2);
            if(o1cost == o2cost)
            { // if costs are equal then just return the one who is a goal node. otherwise make an arbitrary choice with 0
                return o1.equals(goal) ? -1 : 0;
            }
            return o1cost < o2cost? -1 : 1;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof IStateComparator)
            {
                return ((IStateComparator) obj).goal.equals(goal);
            }
            return false;
        }

    }
    public AbstractList<IState> run(Problem problem, boolean clockwise, IHeuristic heuristic)
    {
        PriorityQueue<IState> frontier = new PriorityQueue<>(new IStateComparator(heuristic,
                problem.getEnd())); // log(n) for getting lowest cost, marks the nodes that in the open list with lowest cost first
        Hashtable<String, IState> open_list = new Hashtable<>(); // marks the nodes found yet opened
        Hashtable<String,IState> closed_list = new Hashtable<>(); // marks the nodes found that we visited and extracted all neighbors
        IState End = problem.getEnd(); // marks the goal node
        IState Start = problem.getInit(); // marks the start node
        frontier.add(Start);
        open_list.put(Start.toString(), Start);
        while(!frontier.isEmpty())
        {
            IState closest_node = frontier.poll();
            printer.println(closest_node.toString());
            Operator[] ops = problem.getOperators();
            List<Operator> operators;
            if(clockwise)
            {
                operators = Arrays.asList(ops);
            }
            else {
                operators = AutoCarNxN.ReverseOrder(ops);
            }
            for(Operator operator: operators)
            {
                if(operator.CheckPossible(closest_node))
                {
                    IState successor = operator.operate(closest_node);
                    double successor_cost = successor.getCost() + heuristic.calcH(successor);
                    if(successor.equals(End))
                    { // if we found the goal node then just return it
                        printer.println(successor.toString());
                        printer.println("-");
                        for(IState state : successor.GetPath())
                        {
                            printer.println(state.toString());
                        }
                        return successor.GetPath();
                    }
                    else if(open_list.containsKey(successor.toString()))
                    {
                        IState suc_clone = open_list.get(successor.toString());
                        if(suc_clone.getCost() + heuristic.calcH(suc_clone) > successor_cost)
                        {
                            open_list.put(successor.toString(), successor);
                            frontier.remove(suc_clone);
                            frontier.add(successor);
                        }
                        // else{
                        // node already in the open list that has a lower cost than the current node,
                        // we don't care about the current node so we can skip this one
                    }
                    else if(closed_list.containsKey(successor.toString()))
                    {
                        IState suc_clone_closed = closed_list.get(successor.toString());
                        if(suc_clone_closed.getCost() + heuristic.calcH(suc_clone_closed) > successor_cost )
                        {
                            closed_list.remove(successor.toString()); // remove the node from the closed list
                            open_list.put(successor.toString(), successor); // put the node in the open list
                            frontier.add(successor);
                        }
                    }
                    else
                    {
                        // we didn't see that node yet, add it to the open list
                        open_list.put(successor.toString(), successor);
                        frontier.add(successor);
                    }
                }
            }
            // mark the node as closed, since we extracted all the possible neighbors out of him
            closed_list.put(closest_node.toString(), closest_node);
        }
        // the open list is empty, and we didn't find any goal nodes, then there are no goal nodes reachable from Start
        return null;
    }


}
