package de.uos.inf.ko.knapsack.solver.student;

import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.Solution;
import de.uos.inf.ko.knapsack.SolverInterface;

/**
 * Solver for the binary knapsack problem based on a genetic algorithm.
 *
 * @author
 */
public class GeneticAlgorithm implements SolverInterface<Solution> {

  @Override
  public Solution solve(Instance instance) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getName() {
    return "GA(s)";
  }
}
