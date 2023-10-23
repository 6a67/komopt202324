package de.uos.inf.ko.knapsack.solver.student;

import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.Solution;
import de.uos.inf.ko.knapsack.SolverInterface;
import de.uos.inf.ko.utils.Logger;

/**
 * A full enumeration algorithm for the binary knapsack problem.
 * 
 * @author
 */
public class Enumeration implements SolverInterface<Solution> {

  @Override
  public Solution solve(Instance instance) {
    int numOptimalSolutions = 0;
    int numAllowableSolutions = 0;

    Solution bestSolution = null;
    int bestValue = Integer.MIN_VALUE;
    int numItems = instance.getSize();
    int numSolutions = (int) Math.pow(2, numItems);

    // iterate over all possible solutions
    for (int i = 0; i < numSolutions; i++) {
      Solution solution = new Solution(instance);
      // generate possible solution for the current iteration
      
      //  i = 5 =  101
      //           001 first iteration step (j = 0) => 101 & 001 = 001 != 0 => solution.set(0, 1)
      //           010 second iteration step (j = 1) => 101 & 010 = 000 == 0 => solution.set(1, 0)
      //           100 third iteration step (j = 2) => 101 & 100 = 100 != 0 => solution.set(2, 1)
      //  solution: [1, 0, 1]
       
      //  i = 6 = 110
      //  solution: [0, 1, 1]
      for (int j = 0; j < numItems; j++) {
        if ((i & (1 << j)) != 0) {
          solution.set(j, 1);
        } else {
          solution.set(j, 0);
        }
      }

      if (solution.isFeasible()) {
        numAllowableSolutions++;
      }

      int value = solution.getValue();
      int weight = solution.getWeight();

      if (value > bestValue && solution.isFeasible()) {
        bestSolution = solution;
        bestValue = value;
        numOptimalSolutions = 1;
      } else if (value == bestValue && solution.isFeasible()) {
        numOptimalSolutions++;
      }

      Logger.println("Objective function value: " + value);
      Logger.println("Weight: " + weight);
      Logger.println("Feasible status: " + solution.isFeasible());
    }

    Logger.println("Number of all solutions: " + numSolutions);
    Logger.println("Number of optimal solutions: " + numOptimalSolutions);
    Logger.println("Number of allowable solutions: " + numAllowableSolutions);

    return bestSolution;
  }

  @Override
  public String getName() {
    return "Enum(s)";
  }
}
