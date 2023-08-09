package de.uos.inf.ko.knapsack;

/**
 * An interface for knapsack problem solvers
 *
 * @author Stephan Beyer
 */
public interface SolverInterface<SolutionType> {
  /**
   * Compute a solution for the given instance
   *
   * @param instance The given knapsack instance
   * @return The solution
   */
  SolutionType solve(Instance instance);

  /**
   * Gives an solver name to identify the solver.
   *
   * @return the solver name
   */
  String getName();
}
