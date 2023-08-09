package de.uos.inf.ko.knapsack.solver.student;

import de.uos.inf.ko.knapsack.Solution;

public class BranchAndBoundTest extends GenericExactSolverTest<Solution> {
  public BranchAndBoundTest() {
    super(new BranchAndBound());
  }
}
