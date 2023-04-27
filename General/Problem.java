package General;

import Problems.PathFinding.AutoCarNxN;

import java.util.ArrayList;
import java.util.Arrays;

public class Problem {

    private IState init;
    private IState end;
    private Operator[] operators;

    public Problem(IState init, IState end, Operator[] operators)
    {
        this.init = init;
        this.end = end;
        this.operators = operators;
    }

    public IState getInit()
    {
        return init;
    }
    public IState getEnd()
    {
        return end;
    }
    public Operator[] getOperators()
    {
        return operators;
    }
    public ArrayList<Operator> getPossibleOperators(IState state, boolean clockwise)
    {
        ArrayList<Operator> arr;
        if(clockwise)
            arr = new ArrayList<>(Arrays.asList(getOperators()));
        else
            arr = new ArrayList<>(AutoCarNxN.ReverseOrder(getOperators()));
        arr.removeIf(operator -> !operator.CheckPossible(state));
        return arr;
    }

}
