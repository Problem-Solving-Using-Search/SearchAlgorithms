import General.IState;
import General.Problem;
import Problems.PathFinding.RubiksCube;
import Problems.PathFinding.nPuzzle;
import Uninformed.Unweighted.DFID;

import java.util.AbstractList;

public class Main {
    public static void main(String[] args)
    {
        run_rubiks();
    }

    private static void run_nPuzzle()
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
        DFID algo = new DFID();
        AbstractList<IState> path = algo.run(threePuzzle,false);
        for(IState state : path)
        {
            System.out.println(state);
        }
    }
    private static void run_rubiks()
    {
        byte[][][] init_cube = new byte[][][]
                { // each color should have 9 cells
                        {{1,6,5}, {4,1,1}, {3,2,3}}, // 1;4 , 2;1, 3;1 , 4;1, 5;1, 6;1
                        {{2,5,4}, {3,2,2}, {4,3,4}}, // 2;1, 3;2, 4;2, 5;1, 6;1, 2;1
                        {{3,4,3}, {2,3,3}, {5,4,5}},
                        {{4,3,2}, {1,4,4}, {6,5,6}},
                        {{5,2,1}, {5,5,5}, {1,6,1}},
                        {{6,1,6}, {6,6,6}, {2,1,2}},
                };
        Problem rubiksCubeProblem = new RubiksCube(init_cube).problem;
        DFID algo = new DFID();
        AbstractList<IState> path = algo.run(rubiksCubeProblem, false);
        for(IState state : path)
        {
            System.out.println(state);
        }
    }
}
