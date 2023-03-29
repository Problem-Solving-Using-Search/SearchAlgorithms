import General.IState;
import General.State;
import Problems.nPuzzle;
import Uninformed.Unweighted.BFS;

import java.util.AbstractList;

public class Main {
    public static void main(String[] args)
    {
        int[][] end_grid3 =
                      {
                              {1,2,3},
                              {4,5,6},
                              {7,8,-1}
                      };
        int[][] init_grid3 =
                      {
                              {-1 ,8, 7},
                              {6, 5, 4},
                              {3, 2, 1}
                      };
        int[][] end_grid2 =
                {
                        {-1,1},
                        {2,3},

                };
        int[][] init_grid2 =
                {
                        {3,2},
                        {1, -1}
                };
        AbstractList<IState> path = BFS.run(new nPuzzle(2, init_grid2, end_grid2).getProblem());
        for(IState state : path)
        {
            System.out.println(state.toString());
        }
    }
}
