package Problems.PathFinding;

import General.IState;
import Problems.PathFinding.nPuzzle;
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
        assertFalse(state.compare(state2));
        assertTrue(state.compare(state));
        assertFalse(state2.compare(state));
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
        assertTrue(temp_state1.getParent().compare(temp_state));
        assertSame(temp_state1.getLastOperation(), nPuzzle.Movements.Left);

        new_path.add(temp_state1);
        nPuzzle.GridState temp_state2 = (nPuzzle.GridState) left.operate(temp_state1);
        assertTrue(temp_state2.getParent().compare(temp_state1));
        assertSame(temp_state2.getLastOperation(), nPuzzle.Movements.Left);

        new_path.add(temp_state2);
        nPuzzle.GridState temp_state3 = (nPuzzle.GridState) up.operate(temp_state2);
        assertTrue(temp_state3.getParent().compare(temp_state2));
        assertSame(temp_state3.getLastOperation(), nPuzzle.Movements.Up);

        new_path.add(temp_state3);
        nPuzzle.GridState temp_state4 = (nPuzzle.GridState) up.operate(temp_state3);
        assertTrue(temp_state4.getParent().compare(temp_state3));
        assertSame(temp_state4.getLastOperation(), nPuzzle.Movements.Up);

        new_path.add(temp_state4);
        LinkedList<IState> path = temp_state4.GetPath();
        while(!path.isEmpty()) {
            nPuzzle.GridState t1 = (nPuzzle.GridState) path.pop();
            nPuzzle.GridState t2 = (nPuzzle.GridState) new_path.pop();
//            System.out.println(t1);
//            System.out.println(t2);
            assertTrue(t1.compare(t2));
        }

    }
}
