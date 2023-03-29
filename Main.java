import General.IState;
import General.State;
import Problems.nPuzzle;
import Uninformed.Unweighted.BFS;

import java.util.AbstractList;

public class Main {
    public static void main(String[] args)
    {
        int[][] end_grid =
                      {
                              {1,2,3},
                              {4,5,6},
                              {7,8,-1}
                      };
        int[][] init_grid =
                      {
                              {-1 ,8, 7},
                              {6, 5, 4},
                              {3, 2, 1}
                      };
        AbstractList<IState> path = BFS.run(new nPuzzle(3, init_grid, end_grid).getProblem());
        for(IState state : path)
        {
            System.out.println(state.toString());
        }
    }
}
