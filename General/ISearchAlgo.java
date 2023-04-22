package General;

import java.util.AbstractList;

public interface ISearchAlgo {

    AbstractList<IState> run(Problem problem, boolean clockwise);

}
