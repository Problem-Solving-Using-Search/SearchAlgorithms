package General;

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

}
