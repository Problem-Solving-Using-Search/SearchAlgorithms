package Problems.PathFinding;

import General.IState;
import General.Operator;
import General.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AutoCarNxN {
    public Problem problem;
    public RoadType[][] surface;
    enum Movements {
        R,
        RD,
        D,
        LD,
        L,
        LU,
        U,
        RU
    }
    enum RoadType {
        D, // dirt track , כביש עפר
        R, // Regular road
        H, // Sand
        X, // צוק
        S, // Starting point
        G // End Point
    }


    private static class AutoCarNxNProblem extends Problem
    {
        public AutoCarNxNProblem(IState init, IState end, Operator[] operators) {
            super(init, end, operators);
        }
    }
    public ArrayList<AutoCarNxNOperator> GetValidOperators(AutoCarNxNState state)
    {
        ArrayList<AutoCarNxNOperator> valid_operators = new ArrayList<>();
        for (Movements movement :
                Movements.values()) {
            AutoCarNxNOperator operator = new AutoCarNxNOperator(movement);
            if(operator.CheckPossible(state))
                valid_operators.add(operator);
        }
        return valid_operators;
    }
    public AutoCarNxNOperator CreateOperator(Movements movement)
    {
        return new AutoCarNxNOperator(movement);
    }

    class AutoCarNxNOperator implements Operator
    {
        Movements movement;
        AutoCarNxNOperator(Movements movement)
        {
            this.movement = movement;
        }

        int[] GetNewIndices(AutoCarNxNState state)
        {
            int[] new_cell_indices = new int[2];
            new_cell_indices[0] = state.indices[0];
            new_cell_indices[1] = state.indices[1];
            switch (movement) {
                case D -> new_cell_indices[0] += 1;
                case LD -> {
                    new_cell_indices[0] += 1;
                    new_cell_indices[1] -= 1;
                }
                case RD -> {
                    new_cell_indices[0] += 1;
                    new_cell_indices[1] += 1;
                }
                case U -> new_cell_indices[0] -= 1;
                case RU -> {
                    new_cell_indices[0] -= 1;
                    new_cell_indices[1] += 1;
                }
                case R -> new_cell_indices[1] += 1;
                case LU -> {
                    new_cell_indices[0] -= 1;
                    new_cell_indices[1] -= 1;
                }
            }
            return new_cell_indices;
        }
        private int GetCostEvaluation(AutoCarNxNState state)
        {
            int cost = switch (state.road_type) {
                case D -> state.cost + 1;
                case R -> state.cost + 3;
                case H, G -> state.cost + 5;
                case S -> state.cost;
                default -> 0;
                // cost evaluation
            };
            return cost;
        }
        @Override
        public IState operate(IState s) {
            AutoCarNxNState state = (AutoCarNxNState) s;
            if(!this.CheckPossible(s))
                return null;
            int[] new_cell_indices = GetNewIndices(state);
            AutoCarNxNState new_state = new AutoCarNxNState(new_cell_indices, surface);
            new_state.cost = state.cost + GetCostEvaluation(new_state);
            new_state.SetParent(s);
            return new_state;
        }

        @Override
        public boolean CheckPossible(IState s) {
            AutoCarNxNState state = (AutoCarNxNState) s;
            int[] indices = GetNewIndices(state);
            if(indices[0] > surface.length
                    || indices[1] > surface[0].length)
                return false;
            try
            {
                // check that the operator's result will not be the parent
                AutoCarNxNState parentState = (AutoCarNxNState) s.GetParent();
                if(parentState.equals(new AutoCarNxNState(indices, surface)))
                {
                    return false;
                }
                // don't go back to init state and don't go to צוק
                RoadType rt = surface[indices[0]][indices[1]];
                return rt != RoadType.S &&
                        rt != RoadType.X;
            }
            catch (Exception e)
            {
                return false;
            }
        }
        @Override
        public boolean equals(Object o)
        {
            if(o instanceof AutoCarNxNOperator operator)
            {
                return operator.movement == this.movement;
            }
            return false;
        }
        @Override
        public String toString()
        {
            return movement.name();
        }
    }
    static class AutoCarNxNState implements IState
    {
        /** the node which created this state*/
        AutoCarNxNState parent;
        RoadType road_type;
        /** 2 indices, first is row, and second is col of this state*/
        int[] indices;
        int cost;
        /** Use me to init the cost as preferred*/
        AutoCarNxNState(int[] indices, RoadType road_type, AutoCarNxNState parent, int cost)
        {
            this(indices,road_type,parent);
            this.cost = cost;
        }
        /** Use me if you want to init the parent node as preferred*/
        AutoCarNxNState(int[] indices, RoadType road_type, AutoCarNxNState parent)
        {
            this(indices,road_type);
            this.parent = parent;
            this.cost = 0;
        }
        /** Use me if you want to init the parent as this node*/
        AutoCarNxNState(int[] indices, RoadType road_type)
        {
            this.indices = indices;
            this.road_type = road_type;
            this.parent = this;
            this.cost = 0;
        }
        AutoCarNxNState(int[] indices, RoadType[][] surface)
        {
            this(indices,surface,0);
        }
        AutoCarNxNState(int[] indices, RoadType[][] surface, int cost)
        {
            this.indices = indices;
            this.road_type = surface[indices[0]][indices[1]];
            this.parent = this;
            this.cost = cost;
        }

        @Override
        public boolean equals(IState state2) {
            if(state2 instanceof AutoCarNxNState)
            {
                AutoCarNxNState s2 = ((AutoCarNxNState) state2);
                return road_type == s2.road_type
                        && indices[0] == s2.indices[0]
                        && indices[1] == s2.indices[1];
            }
            return false;

        }
        @Override
        public void SetParent(IState state) {
            this.parent = (AutoCarNxNState) state;
        }

        @Override
        public IState GetParent() {
            return this.parent;
        }
    }
    private static RoadType[][] convertCharArrToRT(char[][] char_surface)
    {
        int cols = char_surface[0].length;
        int rows = char_surface.length;
        /* assert for valid values of the surface*/
        RoadType[][] rt_surface = new RoadType[char_surface.length][char_surface[0].length];
        for (int i=0; i < rows;i++)
            for (int j = 0; j < cols; j++)
                rt_surface[i][j]=RoadType.valueOf("" + char_surface[i][j]);
        return rt_surface;
    }
    public AutoCarNxN(char[][] char_surface, int[] init_indices, int[] end_indices) throws Exception {
        this(convertCharArrToRT(char_surface),
                init_indices,
                end_indices);
    }
    public AutoCarNxN(RoadType[][] surface, int[] init_indices, int[] end_indices) throws Exception {
        if(surface.length == 0)
            throw new Exception("n_rows x n_cols must not be 0");
        int rows = surface.length;
        int cols = surface[0].length;
        /* Checks for normal input  */
        if(rows != cols)
            throw new Exception("Rows must equal columns in NxN mat");
        if(init_indices.length > 2 || end_indices.length > 2)
            throw new Exception("init indices or end indices larger than 2");
        if(init_indices[0] >= rows || end_indices[0] >= rows)
            throw new Exception("cannot index row larger than number of rows in surface in init indices or end indices");
        if(init_indices[1] >= cols || end_indices[1] >= cols)
            throw new Exception("cannot index col larger than number of cols in surface in init indices or end indices");
        this.surface = surface;
        RoadType rtinit, rtend;
        rtinit = RoadType.valueOf("" + surface[init_indices[0]][init_indices[1]]); // if not found throws exception
        rtend = RoadType.valueOf("" + surface[end_indices[0]][end_indices[1]]); // if not found throws exception

        /* declare init and end states */
        AutoCarNxNState init_state = new AutoCarNxNState(init_indices, rtinit);
        AutoCarNxNState end_state = new AutoCarNxNState(end_indices, rtend);
        /* declare operators */
        Movements[] movementsValues = Movements.values();
        AutoCarNxNOperator[] operators = new AutoCarNxNOperator[movementsValues.length];
        int i=0;
        for (Movements movement : movementsValues) { /* putting the operators in clockwise order
        i.e R RD D LD L LU U RU, for contrast ccw would be R RU U LU L LD D RD */
            operators[i++] = new AutoCarNxNOperator(movement);
        }
        this.problem = new AutoCarNxNProblem(init_state,end_state, operators);
    }

    /**
     *
     * @param operators
     * @return
     */
    public static List<Operator> ReverseOrder(Operator[] operators)
    {
       List<Operator> ops = Arrays.asList(operators);
       Operator first = ops.remove(0); // remove the
       Collections.reverse(ops); // reverse order of the remaining elements
        ops.add(0,first);
        return ops;
    }


}
