package de.uos.inf.ko.knapsack.solver.student;

import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.Solution;
import de.uos.inf.ko.knapsack.SolverInterface;

/**
 * A solver for the binary knapsack problem based on simulated annealing.
 *
 * @author
 */
public class SimulatedAnnealing implements SolverInterface<Solution> {

  @Override
  public Solution solve(Instance instance) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getName() {
    return "SA(s)";
  }
}
