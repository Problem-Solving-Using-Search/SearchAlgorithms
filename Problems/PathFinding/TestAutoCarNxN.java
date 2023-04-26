package Problems.PathFinding;

import General.IHeuristic;
import General.IState;
import General.Utils;
import Uninformed.Unweighted.BFS;
import Uninformed.Unweighted.DFID;
import Uninformed.weighted.AStar;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import Problems.PathFinding.AutoCarNxN.*;
import static General.Utils.ConvertStringToMatrix;
import static org.junit.jupiter.api.Assertions.*;


public class TestAutoCarNxN {
    private static class TestCase
    {

        char[][] surface;
        int[] init_indices;
        int[] end_indices;
        TestCase(String surfaceString, int[] init_indices, int[] end_indices) throws Exception {
            this.surface = ConvertStringToMatrix(surfaceString);
            this.init_indices = init_indices;
            this.end_indices = end_indices;
        }
    }
    ArrayList<TestCase> cases;
    int currentCase;
    public static PrintWriter printer;

    static {
        try {
            printer = new PrintWriter(new FileWriter("Inputs/Input"), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TestAutoCarNxN() throws Exception {
        cases = new ArrayList<>();
        cases.add(new TestCase(
                """
                        XXXXXXXXXX
                        XSRRRRXXXX
                        XDXXXXXXXX
                        HDHHHHHHHH
                        HDHHHHHHHH
                        HDRRHHHHHH
                        XXHHHGHHHH
                        XXHHHHHHHH
                        XHHHHHHHHH
                        XXHHHHHHHH""",
                new int[]{1,1}, // sub 1
                new int[]{6,5}) // sub 1
        );
        cases.add(new TestCase(
                """
                        XXXXXXXXXX
                        XSRRRRXXXX
                        XDXXXXXXXX
                        HDHHHHHHHH
                        HDHHHHHHHH
                        HGRRHHHHHH
                        XXHHHHHHHH
                        XXHHHHHHHH
                        XHHHHHHHHH
                        XXHHHHHHHH""",new int[]{1,1},
                                        new int[]{5,1}
        ));
        this.currentCase = 1;
        for (char[] c : cases.get(currentCase).surface) {
            StringBuilder line = new StringBuilder("");
            line.append(c);
            printer.println(line);
        }

    }
    /**
     * Testing AutoCarNxN
     */
    @Test
    public void testConstructorAutoCarNxN() {
        for (TestCase test_case : cases) {
            char[][] surface = test_case.surface;
            int[] init_indices = test_case.init_indices;
            int[] end_indices = test_case.end_indices;
            try {
                RoadType rtinit1 =
                        AutoCarNxN.RoadType.valueOf("" + surface[init_indices[0]][init_indices[1]]);
                RoadType rtend1 =
                        AutoCarNxN.RoadType.valueOf("" + surface[end_indices[0]][end_indices[1]]);
                AutoCarNxN auto1 = new AutoCarNxN(surface, init_indices, end_indices);
                AutoCarNxNState actualInit1 = (AutoCarNxNState) auto1.problem.getInit();
                AutoCarNxNState actualEnd1 = (AutoCarNxNState) auto1.problem.getEnd();
                AutoCarNxNOperator[] actualOperators1 = (AutoCarNxNOperator[]) auto1.problem.getOperators();
                AutoCarNxNState checkInit1 = new AutoCarNxN.AutoCarNxNState(init_indices, rtinit1);
                AutoCarNxNState checkEnd1 = new AutoCarNxN.AutoCarNxNState(end_indices, rtend1);
                assertTrue(actualInit1.equals(checkInit1));
                assertTrue(actualEnd1.equals(checkEnd1));
                for (AutoCarNxNOperator autoCarNxNOperator : actualOperators1) {
                    try {
                        Movements.valueOf(autoCarNxNOperator.movement.name());
                    } catch (Exception e) {
                        fail(e.getMessage());
                    }
                }
            } catch (Exception e) {
                fail("Caught Exception from auto1: " + e.getMessage());
            }
        }
    }
    @Test
    public void testStateAutoCarNxN()
    {
        /* Testing GetPath*/
        TestCase case0 = cases.get(0);
        try{
            AutoCarNxN test0 = new AutoCarNxN(case0.surface,case0.init_indices,case0.end_indices);
            AutoCarNxNOperator right = test0.CreateOperator(Movements.R);
            AutoCarNxNOperator down = test0.CreateOperator(Movements.D);

            AutoCarNxNState state = (AutoCarNxNState) test0.problem.getInit();
            AutoCarNxNState stateActual1 = (AutoCarNxNState) down.operate(state);
            int[] new_indices1 = case0.init_indices;

            new_indices1[0] +=1;
            AutoCarNxNState state1 = new AutoCarNxNState(new_indices1,test0.surface,1);
            assertTrue(state1.equals(stateActual1));

            AutoCarNxNState stateActual2 = (AutoCarNxNState) down.operate(stateActual1);
            new_indices1[0] +=1;
            AutoCarNxNState state2 = new AutoCarNxNState(new_indices1,test0.surface,2);
            assertTrue(stateActual2.equals(state2));

            AutoCarNxNState stateActual3 = (AutoCarNxNState) right.operate(stateActual2);
            new_indices1[1] +=1 ;
            AutoCarNxNState state3 = new AutoCarNxNState(new_indices1, test0.surface, 7);
            assertTrue(stateActual3.equals(state3));

            LinkedList<IState> expectedPath = new LinkedList<>();
            expectedPath.add(stateActual3);
            expectedPath.add(stateActual2);
            expectedPath.add(stateActual1);
            expectedPath.add(state);
            LinkedList<IState> path = stateActual3.GetPath();
            for(int i=0; i<path.size(); i++)
            {
                assertTrue(path.get(i).equals(expectedPath.get(i)));
            }

        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }


    }
    @Test
    public void testOperatorAutoCarNxN()
    {
        /**/
        TestCase case0 = cases.get(0);
        try {
            AutoCarNxN test0 = new AutoCarNxN(case0.surface, case0.init_indices, case0.end_indices);
            AutoCarNxNState[] test_states =
                    {
                        (AutoCarNxNState) test0.problem.getInit(),
                        (AutoCarNxNState) test0.problem.getEnd()
                    };
            ArrayList<HashSet<AutoCarNxNOperator>> expected_results =
                    new ArrayList<>(
                            List.of(
                            new HashSet<>(
                                    Arrays.asList(
                                    test0.CreateOperator(Movements.R),
                                    test0.CreateOperator(Movements.D))
                            ),
                            new HashSet<>(
                                Arrays.asList(
                                    test0.CreateOperator(Movements.U),
                                    test0.CreateOperator(Movements.L),
                                    test0.CreateOperator(Movements.LU),
                                    test0.CreateOperator(Movements.D),
                                    test0.CreateOperator(Movements.R),
                                    test0.CreateOperator(Movements.LD),
                                    test0.CreateOperator(Movements.RU),
                                    test0.CreateOperator(Movements.RD))
                            )
                            )
                    );
            for (int i = 0; i < test_states.length; i++) {
                ArrayList<AutoCarNxNOperator> actual =
                        test0.GetValidOperators(test_states[i]);
                HashSet<AutoCarNxNOperator> expectedSet = expected_results.get(i);
                HashSet<AutoCarNxNOperator> actualSet = new HashSet<>(actual);

                // i don't want to mess with hashcodes, so naive implementation as follows
                for(boolean isExpected : new boolean[]{true, false})
                {
                    HashSet<AutoCarNxNOperator> firstSet = isExpected ? expectedSet : actualSet;
                    HashSet<AutoCarNxNOperator> secondSet = isExpected ? actualSet : expectedSet;
                    for (AutoCarNxNOperator operator :
                            firstSet) {
                        boolean isIn = false;
                        for (AutoCarNxNOperator operator2 :
                                secondSet) {
                            if (operator.equals(operator2)) {
                                isIn = true;
                            }
                        }
                        if (!isIn)
                            fail("Cannot find operator in actual set, " + actualSet + expectedSet + " iteration: " + i);
                    }
                }
            }

        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }

    }

    /**
     * Generates a path from S using operators[i] each activates times[i] times, where i=0 is starting point
     * @param operators Operators to use at a certain point
     * @param times number of times to activate the operator[i]
     * @return
     */
    private AbstractList<IState> generateExpectedPath(AutoCarNxNOperator[] operators, int[] times,
                                                      TestCase c)
    {

        AutoCarNxN auto0 = getProblemInstance(c);
        if(auto0 == null)
            return null;
        AutoCarNxNState state = (AutoCarNxNState) auto0.problem.getInit();
        if(operators.length != times.length)
        {
            fail("Cannot generate expected path, operators and times have two different lengths");
            return null;
        }
        AbstractList<IState> expectedPath = new ArrayList<>();
        expectedPath.add(state);
        int n =operators.length;
        for (int i = 0; i < n; i++) {
            for(int j=0; j < times[i]; j++)
            {
                state = (AutoCarNxNState) operators[i].operate(state);
                expectedPath.add(state);
            }
        }
        return expectedPath;

    }
    public AutoCarNxN getProblemInstance(TestCase c)
    {
        try
        {
            return new AutoCarNxN(c.surface, c.init_indices, c.end_indices);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
            return null;
        }
    }
    @Test
    public void testBFS() {
        TestCase case0 = cases.get(0);
        AutoCarNxN auto0 = getProblemInstance(case0);
        BFS algo = new BFS();
        General.Support.resetNumNodesGenerated();
        AbstractList<IState> path = algo.run(auto0.problem, true);
        AutoCarNxNOperator down = auto0.CreateOperator(Movements.D);
        AutoCarNxNOperator rightDown = auto0.CreateOperator(Movements.RD);
        AbstractList<IState> pathExpected = generateExpectedPath(
                new AutoCarNxNOperator[]{down, rightDown},
                new int[]{1,4},
                case0
        );
        assert pathExpected != null;
        Collections.reverse(pathExpected);
        assert path != null;
        assertEquals(path.size(), pathExpected.size());
        for (int i = 0; i < pathExpected.size(); i++) {
            assertTrue(path.get(i).equals(pathExpected.get(i)));
        }
        // TODO: Requires to assert the number of nodes generated
    }
    @Test
    public void testDFID()
    {
        TestCase case0 = cases.get(0);
        AutoCarNxN auto0 = getProblemInstance(case0);
        DFID algo = new DFID();
        AbstractList<IState> path = algo.run(auto0.problem,false);
        // TODO: implement me, seems to work but needs to be understood
    }
    @Test
    public void testAStar()
    {
        TestCase case0 = cases.get(currentCase);
        AutoCarNxN auto0 = getProblemInstance(case0);
        AStar algo = new AStar();
        LinkedList<IState> list = (LinkedList<IState>) algo.run(auto0.problem, true, new IHeuristic() {
            @Override
            public double calcH(IState node) {
                return auto0.CalcManhetenDistance((AutoCarNxNState) node,(AutoCarNxNState) auto0.problem.getEnd());
            }
        });
        assertNotNull(null, list.toString());
    }
}
