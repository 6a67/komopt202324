package de.uos.inf.ko.knapsack.solver.student;

import de.uos.inf.ko.knapsack.FractionalSolution;
import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.SolverInterface;

/**
 * An optimal greedy solver for the fractional knapsack problem.
 *
 * @author
 */
public class FractionalSolver implements SolverInterface<FractionalSolution> {

  @Override
  public FractionalSolution solve(Instance instance) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getName() {
    return "Fractional(s)";
  }
}
