package Problems.PathFinding;

import General.IState;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    /**
     * Test N Puzzle
     */
    @Test
    public void testGridState()
    {
        int[][] grid = {
                {1,2,3},
                {4,5,6},
                {7,8,-1}
        };
        int[][] grid2 = {
                {4,5,6},
                {7,8,9},
                {1,2,-1}
        };
        nPuzzle.GridState state = new nPuzzle.GridState(grid);
        nPuzzle.GridState state2 = new nPuzzle.GridState(grid2);
        /**
         * Compare funct
        * */
        assertFalse(state.equals(state2));
        assertTrue(state.equals(state));
        assertFalse(state2.equals(state));
        /** Get Path */
        nPuzzle.GridOperator left = new nPuzzle.GridOperator(nPuzzle.Movements.Left);
        nPuzzle.GridOperator right = new nPuzzle.GridOperator(nPuzzle.Movements.Right);
        nPuzzle.GridOperator up = new nPuzzle.GridOperator(nPuzzle.Movements.Up);
        nPuzzle.GridOperator down = new nPuzzle.GridOperator(nPuzzle.Movements.Down);
        nPuzzle.GridState temp_state = new nPuzzle.GridState(grid);
        Stack<IState> new_path = new Stack<>();
        /** go left */
        new_path.add(temp_state);
        nPuzzle.GridState temp_state1 = (nPuzzle.GridState) left.operate(temp_state);
        assertTrue(temp_state1.getParent().equals(temp_state));
        assertSame(temp_state1.getLastOperation(), nPuzzle.Movements.Left);

        new_path.add(temp_state1);
        nPuzzle.GridState temp_state2 = (nPuzzle.GridState) left.operate(temp_state1);
        assertTrue(temp_state2.getParent().equals(temp_state1));
        assertSame(temp_state2.getLastOperation(), nPuzzle.Movements.Left);

        new_path.add(temp_state2);
        nPuzzle.GridState temp_state3 = (nPuzzle.GridState) up.operate(temp_state2);
        assertTrue(temp_state3.getParent().equals(temp_state2));
        assertSame(temp_state3.getLastOperation(), nPuzzle.Movements.Up);

        new_path.add(temp_state3);
        nPuzzle.GridState temp_state4 = (nPuzzle.GridState) up.operate(temp_state3);
        assertTrue(temp_state4.getParent().equals(temp_state3));
        assertSame(temp_state4.getLastOperation(), nPuzzle.Movements.Up);

        new_path.add(temp_state4);
        LinkedList<IState> path = temp_state4.GetPath();
        while(!path.isEmpty()) {
            nPuzzle.GridState t1 = (nPuzzle.GridState) path.pop();
            nPuzzle.GridState t2 = (nPuzzle.GridState) new_path.pop();
//            System.out.println(t1);
//            System.out.println(t2);
            assertTrue(t1.equals(t2));
        }

    }

    @Test
    public void TestRubicCube()
    {
        RubiksCube.init_opositeSides();
        byte[][][] goal_cube = new byte[][][] {
                {{1,1,1}, {1,1,1}, {1,1,1}},
                {{2,2,2}, {2,2,2}, {2,2,2}},
                {{3,3,3}, {3,3,3}, {3,3,3}},
                {{4,4,4}, {4,4,4}, {4,4,4}},
                {{5,5,5}, {5,5,5}, {5,5,5}},
                {{6,6,6}, {6,6,6}, {6,6,6}}
        };
        byte[][][] init_cube = new byte[][][]
        { // each color should have 9 cells
                {{1,6,5}, {4,1,1}, {3,2,3}}, // 1;4 , 2;1, 3;1 , 4;1, 5;1, 6;1
                {{2,5,4}, {3,2,2}, {4,3,4}}, // 2;1, 3;2, 4;2, 5;1, 6;1, 2;1
                {{3,4,3}, {2,3,3}, {5,4,5}},
                {{4,3,2}, {1,4,4}, {6,5,6}},
                {{5,2,1}, {5,5,5}, {1,6,1}},
                {{6,1,6}, {6,6,6}, {2,1,2}},
        };

        RubiksCube.CubeState init_state = new RubiksCube.CubeState(init_cube);
        RubiksCube.CubeState goal_state = new RubiksCube.CubeState(goal_cube);
        assertTrue(init_state.equals(init_state));
        assertFalse(init_state.equals(goal_state));
        assertFalse(goal_state.equals(init_state));
        assertTrue(init_state.equals(init_state.parent));
        assertTrue(goal_state.equals(goal_state.parent));

        byte[][][] front_shifted_goal_cube = new byte[][][] {
                {{1,1,4}, {1,1,4}, {1,1,4}}, // left
                {{2,2,1}, {2,2,1}, {2,2,1}}, // right
                {{3,3,2}, {3,3,2}, {3,3,2}}, // top
                {{4,4,3}, {4,4,3}, {4,4,3}}, // bottom
                {{5,5,5}, {5,5,5}, {5,5,5}}, // back
                {{6,6,6}, {6,6,6}, {6,6,6}} // front
        };

        RubiksCube.CubeState front_shifted_goal_state = new RubiksCube.CubeState(front_shifted_goal_cube);
        RubiksCube.CubeOperator shiftFront = new RubiksCube.CubeOperator(RubiksCube.CubeSides.FrontSide);
        assertTrue(shiftFront.operate(goal_state).equals(front_shifted_goal_state));

        byte[][][] right_shifted_goal_cube = new byte[][][] {
                {{1,1,1}, {1,1,1}, {1,1,1}}, // left
                {{2,2,2}, {2,2,2}, {2,2,2}}, // right
                {{3,3,6}, {3,3,6}, {3,3,6}}, // top
                {{4,4,3}, {4,4,3}, {4,4,3}}, // bottom
                {{5,5,4}, {5,5,4}, {5,5,4}}, // back
                {{6,6,5}, {6,6,5}, {6,6,5}} // front
        };

        RubiksCube.CubeState right_shifted_goal_state = new RubiksCube.CubeState(right_shifted_goal_cube);
        RubiksCube.CubeOperator shiftRight = new RubiksCube.CubeOperator(RubiksCube.CubeSides.RightSide);
        RubiksCube.CubeState shiftedRightState = (RubiksCube.CubeState) shiftRight.operate(goal_state);
        assertTrue(shiftedRightState.equals(right_shifted_goal_state));

        byte[][][] bottom_shifted_cube = new byte[][][] {
                {{6,6,6}, {1,1,1}, {1,1,1}},// left
                {{1,1,1}, {2,2,2}, {2,2,2}},// right
                {{3,3,3}, {3,3,3}, {3,3,3}},// top
                {{4,4,4}, {4,4,4}, {4,4,4}},// bottom
                {{2,2,2}, {5,5,5}, {5,5,5}},// back
                {{5,5,5}, {6,6,6}, {6,6,6}} // front
        };

        RubiksCube.CubeState bottom_shifted_goal_state = new RubiksCube.CubeState(bottom_shifted_cube);
        RubiksCube.CubeOperator shiftBottom = new RubiksCube.CubeOperator(RubiksCube.CubeSides.BottomSide);
        RubiksCube.CubeState shiftedBottomState = (RubiksCube.CubeState) shiftBottom.operate(goal_state);
        assertTrue(shiftedBottomState.equals(bottom_shifted_goal_state));


    }
}
