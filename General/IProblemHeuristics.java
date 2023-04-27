package General;

public interface IProblemHeuristics {
    public Problem getProblem();
    public double CalcManhettenDistance(IState state1, IState state2);
}
