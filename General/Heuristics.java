package General;

import Problems.PathFinding.AutoCarNxN;

public class Heuristics {

    public static class ManhettenHeuristic implements IHeuristic
    {
        IProblemHeuristics problemHeuristics;
        public ManhettenHeuristic(IProblemHeuristics heuristics) {
            this.problemHeuristics = heuristics;
        }
        @Override
        public double calcH(IState node) {
            return problemHeuristics.CalcManhettenDistance(node,problemHeuristics.getProblem().getEnd());
        }
    }

}
