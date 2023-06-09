package Problems.PathFinding;

import General.IState;
import General.Operator;
import General.Problem;

import java.util.LinkedList;

import static General.Utils.deepCopy;

public class nPuzzle
{
    static Puzzle puzzle;
    enum Movements
    {
        Up,
        Down,
        Left,
        Right
    }
    static class Puzzle extends Problem
    {
        private int N;
        public Puzzle(IState init, IState end, Operator[] operators) {
            super(init, end, operators);
        }

        public void setN(int N)
        {
            this.N = N;
        }

    }
    static class GridState implements IState
    {
        private Movements lastOperation;
        private GridState Parent;
        private int[][] grid;
        public GridState(int[][] grid, IState parent)
        {
            this.grid = grid;
            this.Parent = (GridState) parent;
        }
        public GridState(int[][] grid)
        {
            this.grid = grid;
            this.Parent = this;
        }
        public Movements getLastOperation()
        {
            return lastOperation;
        }
        public void setLastOperation(Movements movement)
        {
            this.lastOperation = movement;
        }
        public GridState getParent()
        {
            return this.Parent;
        }
        @Override
        public boolean equals(IState state2) {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if(grid[i][j] != ((GridState)state2).grid[i][j])
                        return false;
                }
            }
            return true;
        }

        @Override
        public LinkedList<IState> GetPath() {
            LinkedList<IState> linkedList = new LinkedList<>();
            linkedList.add(this);
            GridState gridState = this;
            // check if parent not equals son
            while(!gridState.equals(gridState.Parent))
            {
                gridState = (GridState) linkedList.getLast();
                if(!gridState.Parent.equals(gridState))
                    linkedList.add(gridState.Parent);
            }
            return linkedList;
        }

        @Override
        public double getCost() {
            return 0;
        }

        @Override
        public void setCost(double cost) {

        }

        @Override
        public void SetParent(IState state) {
            this.Parent = (GridState) state;
        }

        @Override
        public IState GetParent() {
            return null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("");
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    builder.append(String.format(" | %d |  ", grid[i][j]));
                }
                builder.append("\n");
            }
            return builder.toString();
        }
    }

    static class GridOperator implements Operator
    {
        Movements movement;
        private int[] find_gap(int[][] grid)
        {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    // if we found the gap
                    if(grid[i][j] == -1)
                    {
                        return new int[] {i,j};
                    }
                }
            }
            return null;
        }
        GridOperator(Movements movement)
        {
            this.movement = movement;
        }
        @Override
        public IState operate(IState s) {
            int[][] grid = deepCopy(((GridState)s).grid);
            int[] gap_indices = find_gap(grid);
            assert gap_indices != null;
            int row_ind = gap_indices[0], col_ind = gap_indices[1];
            // initialize index and is row
            boolean isrow = movement == Movements.Up || movement==Movements.Down;
            int i = movement == Movements.Left || movement == Movements.Up ? -1 : 1;
            // asign values to the grid
            if(isrow)
            // if it's a row operator i.e up/down
            {
                grid[row_ind][col_ind] = grid[row_ind + i][col_ind];
                grid[row_ind + i][col_ind] = -1;
            }
            else
            {
                grid[row_ind][col_ind] = grid[row_ind][col_ind + i];
                grid[row_ind][col_ind + i] = -1;
            }
            GridState state = new GridState(grid, s);
            state.setLastOperation(this.movement);
            return state;
        }

        @Override
        public boolean CheckPossible(IState s) {
            GridState state = ((GridState) s);
            int[][] grid = state.grid;
            int[] gap_indices = find_gap(grid);
            assert gap_indices != null;
            return switch (movement) {
                case Up -> !(gap_indices[0] - 1 < 0) && state.getLastOperation() != Movements.Down;
                case Down -> !(gap_indices[0] + 1 >= puzzle.N) && state.getLastOperation() != Movements.Up;
                case Left -> !(gap_indices[1] - 1 < 0) && state.getLastOperation() != Movements.Right;
                default -> !(gap_indices[1] + 1 >= puzzle.N) && state.getLastOperation() != Movements.Left;
            };
        }
    }


    private Operator[] get_operators()
    {
        Operator[] operators = new Operator[4];
        GridOperator leftOperator = new GridOperator(Movements.Left);
        GridOperator rightOperator = new GridOperator(Movements.Right);
        GridOperator upOperator = new GridOperator(Movements.Up);
        GridOperator downOperator = new GridOperator(Movements.Down);
        operators[0] = leftOperator;
        operators[1] = rightOperator;
        operators[2] = upOperator;
        operators[3] = downOperator;
        return operators;
    }

    /** N = 3
     * Example inputs
     * int[][] end_grid =
     *                 {
     *                         {1,2,3},
     *                         {4,5,6},
     *                         {7,8,-1}
     *                 };
     * int[][] init_grid =
     *                  {
     *                          {-1 ,8, 7},
     *                          {6, 5, 4},
     *                          {3, 2, 1}
     *                  };
     * @param N
     * @param init_grid
     * @param end_grid
     */
    public nPuzzle(int N, int[][] init_grid, int[][] end_grid)

    {
        assert N > 1;
        assert init_grid.length == N;
        assert init_grid[0].length == N;
        assert  end_grid[0].length == N;
        assert  end_grid.length == N;

        Operator[] operators = get_operators();
        GridState init_state = new GridState(init_grid);
        GridState end_state = new GridState(end_grid);

        puzzle = new Puzzle(init_state, end_state, operators);
        puzzle.setN(N);

    }
    public Problem getProblem()
    {
        return this.puzzle;
    }
}
