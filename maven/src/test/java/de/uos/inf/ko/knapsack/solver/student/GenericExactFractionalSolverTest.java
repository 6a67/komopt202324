package de.uos.inf.ko.knapsack.solver.student;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import java.io.IOException;
import org.junit.Test;
import de.uos.inf.ko.knapsack.GenericSolution;
import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.SolverInterface;
import de.uos.inf.ko.knapsack.reader.Reader;

public abstract class GenericExactFractionalSolverTest<SolutionType extends GenericSolution> extends GenericSolverTest<SolutionType> {

  public GenericExactFractionalSolverTest(SolverInterface<SolutionType> solver) {
    super(solver);
  }

  @Test
  public void testOptimality() throws IOException {
    assumeTrue(testIfImplemented());

    double[] optValues = new double[] {6.333333333333333, 12.0, 157.71428571428572};
    for (int i = 0; i <= 2; i++) {
      final Instance instance = Reader.readInstance(KNAPSACK_INSTANCES_PATH + "tiny-rucksack-" + i + ".txt");
      final SolutionType solution = solver.solve(instance);
      System.out.println(solution.getValue());
      assertEquals(optValues[i], solution.getValue().doubleValue(), 0.00001);
    }
  }

}
