package Problems.PathFinding;

import General.IState;
import General.Operator;
import General.Problem;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * Problem describes a 3 X 3 Cube
 */
public class RubiksCube {

    public Problem problem;
    enum Movements {
        CW
    }
    /**
     * Refering to the sides as ordered from 0 to 5
     * 0 refering to right most right side
     * 1 refering to left most side
     * 2 refering to top most side
     * 3 refering to back most side
     * 4 refering to bottom most side
     * 5 refering to front most side
     */
    enum CubeSides {
        RightSide,
        LeftSide,
        TopSide,
        BottomSide,
        BackSide,
        FrontSide
    }
    static HashMap<CubeSides, CubeSides> opositeSide;
    private static class RubiksCubeProblem extends Problem
    {

        public RubiksCubeProblem(IState init, IState end, Operator[] operators) {
            super(init, end, operators);
        }
    }

    static class CubeState implements IState
    {
        // which-side, y, z
        byte[][][] cube;
        CubeState parent;
        public CubeState(byte[][][] cube)
        {
            this.cube = cube;
            this.parent = this;
        }
        @Override
        public boolean equals(IState state2) {
            byte[][][] c = ((CubeState) state2).cube;
            for (int i = 0; i < cube.length; i++) {
                for (int j = 0; j < cube[0].length; j++) {
                    for (int k = 0; k < cube[0][0].length; k++) {
                        if(c[i][j][k] != cube[i][j][k])
                            return false;
                    }
                }
            }
            return true;
        }

        @Override
        public LinkedList<IState> GetPath() {
            return null;
        }

        @Override
        public void SetParent(IState state) {
            this.parent = (CubeState) state;
        }

        @Override
        public IState GetParent() {
            return null;
        }
    }

    static class CubeOperator implements Operator
    {
        CubeSides side;

        public CubeOperator( CubeSides cubeSide)
        {
            this.side = cubeSide;
        }
        CubeState shiftBySide(CubeSides side, byte[][][] cube)
        {
            /**
             *  11 12 13        31 21 11
             *  21 22 23 ---->  32 22 12
             *  31 32 33        33 23 13
             */
            CubeSides oposite = opositeSide.get(side);
            // CW and side
            byte[][][] cube_new = new byte[6][3][3];
            // oposite side stays the same
            cube_new[oposite.ordinal()] = cube[oposite.ordinal()];
            // current side flips each row to be column
            for (int i = 0; i < cube_new[side.ordinal()].length; i++) {
                for (int j = 0; j < cube_new[side.ordinal()][i].length; j++) {
                    cube_new[side.ordinal()][i][j] = cube[side.ordinal()][cube_new[0].length - 1 - j][i];
                }
            }
            byte[][][] fourCubeSides = new byte[4][3][3];
            byte[][][] fourCubeSides_temp = new byte[4][3][3];
            byte[] cubeSidesIndices = new byte[4];
            int j = 0;
            for (int i = 0; i < cube_new.length; i++) {
                if(i != side.ordinal() && i != oposite.ordinal())
                    // if(j-1 >= 0 && (fourCubeSides[j-1]))
                    fourCubeSides[j++] = cube[i];
            }
            // for each side that is consequent to the side before, rotate it
            for (int i = 0; i < 4; i++) {
                for (j = 0; j < cube[0].length; j++) {
                    for (int k = 0; k < cube[0][0].length; k++) {
                        if(k== cube[0][0].length - 1)
                        // rotate
                        {
                            fourCubeSides_temp
                                    [i]
                                    [j]
                                    [cube[i][j].length -1] =
                                    fourCubeSides
                                            [((i-1) < 0 ? fourCubeSides.length -1: i-1)]
                                            [j]
                                            [cube[i][j].length -1];
                        }
                        else
                        // stays the same
                        {
                            fourCubeSides_temp[i][j][k] = fourCubeSides[i][j][k];
                        }

                    }

                }
            }
            // assign the values back to the cube_new after flipping it
            j = 0;
            for (int i = 0; i < cube_new.length; i++) {
                if(i != side.ordinal() && i != oposite.ordinal())
                {
                    cube_new[i] = fourCubeSides_temp[j++];
                }
            }
            return new CubeState(cube_new);
        }
        @Override
        public IState operate(IState s) {
            CubeState state = shiftBySide(side, ((CubeState)s).cube);
            state.SetParent(s);
            return state;
        }

        @Override
        public boolean CheckPossible(IState s) {
            return true;
        }
    }
    private Operator[] getOperators()
    {
        Operator[] operators = new Operator[6];
        CubeSides[] arr = CubeSides.values();
        for (int i = 0; i < arr.length; i++) {
            operators[i] = new CubeOperator(arr[i]);
        }
        return operators;
    }
    public static void init_opositeSides()
    {
        opositeSide = new HashMap<>();
        CubeSides[] arr = CubeSides.values();
        for(int i=0; i < arr.length - 1; i+=2)
        {
            opositeSide.put(arr[i], arr[i+1]);
            opositeSide.put(arr[i+1], arr[i]);
        }
    }
    public RubiksCube(byte[][][] init_cube)
    {
        init_opositeSides();

        int[] count = {0,0,0,0,0,0};
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    count[init_cube[i][j][k] - 1] += 1;
                }
            }
        }
        for (int i = 0; i < count.length; i++) {
            assert init_cube[i][1][1] == i + 1;
            assert count[i] == 9;
        }
        byte[][][] goal_cube = new byte[][][] {
                {{1,1,1}, {1,1,1}, {1,1,1}},
                {{2,2,2}, {2,2,2}, {2,2,2}},
                {{3,3,3}, {3,3,3}, {3,3,3}},
                {{4,4,4}, {4,4,4}, {4,4,4}},
                {{5,5,5}, {5,5,5}, {5,5,5}},
                {{6,6,6}, {6,6,6}, {6,6,6}}
        };
        CubeState initState = new CubeState(init_cube);
        CubeState goalState = new CubeState(goal_cube);
        problem = new RubiksCubeProblem(initState, goalState, getOperators());
    }

}
