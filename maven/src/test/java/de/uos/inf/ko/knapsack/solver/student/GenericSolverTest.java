package de.uos.inf.ko.knapsack.solver.student;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import java.io.IOException;
import org.junit.Test;
import de.uos.inf.ko.knapsack.GenericSolution;
import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.SolverInterface;
import de.uos.inf.ko.knapsack.reader.Reader;

public abstract class GenericSolverTest<SolutionType extends GenericSolution> {

  public final static String KNAPSACK_INSTANCES_PATH = "./src/test/resources/knapsack/";

  protected GenericSolverTest(SolverInterface<SolutionType> solver) {
    this.solver = solver;
  }

  protected SolverInterface<SolutionType> solver;

  protected boolean testIfImplemented() {
    try {
      solver.solve(null);
    } catch (UnsupportedOperationException ex) {
      return false;
    } catch (Exception ex) {
    }

    return true;
  }

  @Test
  public void testFeasiblity() throws IOException {
    assumeTrue(testIfImplemented());

    for (int i = 0; i <= 2; i++) {
      final Instance instance = Reader.readInstance(KNAPSACK_INSTANCES_PATH + "tiny-rucksack-" + i + ".txt");
      final SolutionType solution = solver.solve(instance);
      assertTrue(solution.isFeasible());
    }
  }

}
