package Problems.PathFinding;

import General.IState;
import General.Operator;
import General.Problem;

import java.util.LinkedList;


/**
 * Problem describes a 3 X 3 Cube
 */
public class RubiksCube {

    public Problem problem;
    enum Movements {
        CW,
        CCW
    }
    /**
     * Refering to the sides as ordered from 0 to 5
     * 0 refering to right most right side
     * 1 refering to back most side
     * 2 refering to top most side
     * 3 refering to left most side
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
        public CubeState(byte[][][] cube)
        {
            this.cube = cube;
        }
        @Override
        public boolean compare(IState state2) {
            return false;
        }

        @Override
        public LinkedList<IState> GetPath() {
            return null;
        }

        @Override
        public void SetParent(IState state) {

        }
    }

    static class CubeOperator implements Operator
    {
        Movements movement;
        CubeSides side;

        public CubeOperator(Movements movement, CubeSides cubeSide)
        {
            this.movement = movement;
            this.side = cubeSide;
        }
        static CubeState shiftBySide(Movements movement, CubeSides side)
        {

            switch (movement)
            {
                case CW:
                    switch(side)
                    {
                        case RightSide:
                            byte[][][] cube = new byte[6][3][3];

                            break;
                        case LeftSide:
                            break;
                        case TopSide:
                            break;
                        case BottomSide:
                            break;
                        case BackSide:
                            break;
                        case FrontSide:
                            break;
                    }
                    break;
                case CCW:
                    switch (side)
                    {
                        case RightSide:
                            break;
                        case LeftSide:
                            break;
                        case TopSide:
                            break;
                        case BottomSide:
                            break;
                        case BackSide:
                            break;
                        case FrontSide:
                            break;
                    }
                    break;
            }
            return null;
        }
        @Override
        public IState operate(IState s) {
            return null;
        }

        @Override
        public boolean CheckPossible(IState s) {
            return true;
        }
    }

    public RubiksCube()
    {

    }

}
