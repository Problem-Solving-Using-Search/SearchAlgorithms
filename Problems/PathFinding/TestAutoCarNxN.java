package Problems.PathFinding;

import General.IState;
import General.Utils;
import org.junit.jupiter.api.Test;

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
                                    test0.CreateOperator(Movements.LU))
                            )
                            )
                    );
            for (int i = 0; i < test_states.length; i++) {
                ArrayList<AutoCarNxNOperator> actual =
                        test0.GetValidOperators(test_states[i]);
                HashSet<AutoCarNxNOperator> expectedSet = expected_results.get(i);
                HashSet<AutoCarNxNOperator> actualSet = new HashSet<>(actual);
                // not working please fix me, im tired
                assertTrue(expectedSet.containsAll(actualSet) && actualSet.containsAll(expectedSet),
                expectedSet + ", " + actualSet);
            }

        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }

    }
}
