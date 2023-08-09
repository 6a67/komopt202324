package de.uos.inf.ko.knapsack.solver.student;

import de.uos.inf.ko.knapsack.GenericSolution;
import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.SolverInterface;
import de.uos.inf.ko.knapsack.reader.Reader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

public abstract class ExtendedGenericExactSolverTest<SolutionType extends GenericSolution> extends GenericSolverTest<SolutionType> {

  public ExtendedGenericExactSolverTest(SolverInterface<SolutionType> solver) {
    super(solver);
  }

  @Test
  public void testOptimality() throws IOException {
    assumeTrue(testIfImplemented());

    int[] optValues = new int[] {6, 12, 132};
    for (int i = 0; i <= 2; i++) {
      final Instance instance = Reader.readInstance(KNAPSACK_INSTANCES_PATH + "tiny-rucksack-" + i + ".txt");
      final SolutionType solution = solver.solve(instance);
      assertEquals(optValues[i], solution.getValue().intValue());
    }

    if (!solver.getName().contains("Enum")) {
      int[] greaterOptValues = new int[] {254, 337, 133, 428, 307, 375};
      String[] instNames = new String[] {"10-1", "15-1", "20-1", "20-2", "30-1", "40-1"};
      for (int i = 0; i <= 5; i++) {
        final Instance instance = Reader.readInstance(KNAPSACK_INSTANCES_PATH + "rucksack000" + instNames[i] + ".txt");
        final SolutionType solution = solver.solve(instance);
        assertEquals(greaterOptValues[i], solution.getValue().intValue());
      }
    }

  }

}
