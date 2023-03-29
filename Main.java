import General.IState;
import General.Problem;
import General.State;
import Problems.nPuzzle;
import Uninformed.Unweighted.BFS;
import Uninformed.Unweighted.DFID;
import Uninformed.Unweighted.DFS;

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
                              {6 ,2, 8},
                              {3, 4, 1},
                              {-1, 7, 5}
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
        Problem threePuzzle = new nPuzzle(3, init_grid3, end_grid3).getProblem();
//        AbstractList<IState> path = BFS.run(threePuzzle);
//        AbstractList<IState> path = DFS.run(threePuzzle);
        AbstractList<IState> path = DFID.run(threePuzzle);
        for(IState state : path)
        {
            System.out.println(state);
        }
    }
}
