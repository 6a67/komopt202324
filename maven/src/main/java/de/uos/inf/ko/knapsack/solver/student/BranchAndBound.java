package de.uos.inf.ko.knapsack.solver.student;

import java.util.Arrays;
import java.util.Comparator;
import de.uos.inf.ko.knapsack.FractionalSolution;
import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.Solution;
import de.uos.inf.ko.knapsack.SolverInterface;
import de.uos.inf.ko.utils.Logger;

/**
 * A branch-and-bound algorithm for the binary knapsack problem.
 *
 * @author
 */
public class BranchAndBound implements SolverInterface<Solution> {
  private int cStar = 0;
  private Solution sStar = null;

  private Instance instance;

  // logging variable
  private int generatedSolutions = 0;

  /**
   * Calculates the value-to-weight ratio for a given item.
   * 
   * @param i the index of the item to calculate the ratio for
   * @return the value-to-weight ratio for the given item
   */
  private double getRatio(int i) {
    return (double) instance.getValue(i) / instance.getWeight(i);
  }

  /**
   * This class implements the Branch and Bound algorithm to solve the Knapsack problem.
   * It generates an initial solution using the Greedy Heuristic and then sorts the indices
   * of the items by their cost-per-weight ratio in descending order. It then branches on each
   * item in the sorted order and prunes the branch if the lower bound of the solution is less
   * than the current best solution. The algorithm returns the best solution found.
   *
   * @param instance the instance of the Knapsack problem to be solved
   * @return the best solution found by the algorithm
   */
  @Override
  public Solution solve(Instance instance) {
    // reset values
    generatedSolutions = 0;

    // generate initial solution
    Solution solution = (new GreedyHeuristic()).solve(instance);
    generatedSolutions++;
    cStar = solution.getValue();
    sStar = solution;
    this.instance = instance;

    // make array for index permutation
    Integer[] perm = new Integer[instance.getSize()];
    for (int i = 0; i < perm.length; ++i) {
      perm[i] = i;
    }

    // sort it by cost-per-weight ratio in descending order
    Arrays.sort(perm, new Comparator<Integer>() {
      public int compare(Integer o1, Integer o2) {
        double ratio1 = getRatio(o1);
        double ratio2 = getRatio(o2);

        if (ratio1 == ratio2) {
          return 0;
        }
        if (ratio1 < ratio2) {
          return 1;
        }
        return -1;
      }
    });

    // array has order of indices in descending order of cost-per-weight ratio
    branch(new Solution(instance), perm.clone());

    System.out.println("Instance: " + instance.getFilename());
    System.out.println("Generated solutions: " + generatedSolutions);
    System.out.println("#############################################");

    return sStar;
  }

  /**
   * This method performs the branch and bound algorithm recursively by branching on the given solution S and array of items.
   * It pops the first element from the array and branches for item i not in solution and item i in solution.
   * For each branch, it creates a new solution variant and checks if it is feasible.
   * If the solution is feasible, it calculates the upper bound and checks if it is greater than the current best solution cStar.
   * If the solution is complete, it updates the current best solution cStar and sStar.
   * If the solution is not complete, it recursively calls the branch method with the new solution and the remaining items in the array.
   * @param S the current solution
   * @param array the array of remaining items
   */
  private void branch(Solution S, Integer[] array) {
    // pop first element from array
    int i = array[0];
    Integer[] undecidedArray = Arrays.copyOfRange(array, 1, array.length);

    // branch for item i not in solution and item i in solution
    for (int b = 1; b >= 0; b--) {
      // create solution variant
      Solution newSolution = new Solution(S);
      newSolution.set(i, b);
      generatedSolutions++;

      // check if solution is feasible
      if (!newSolution.isFeasible()) {
        continue;
      }

      // fractional solver solution as upper bound
      Instance fracInstance = new Instance(undecidedArray.length);
      for (int j = 0; j < undecidedArray.length; j++) {
        fracInstance.set(j, instance.getValue(undecidedArray[j]),
            instance.getWeight(undecidedArray[j]));
      }

      fracInstance.setCapacity(instance.getCapacity() - newSolution.getWeight());

      double fracSol = new FractionalSolver().solve(fracInstance).getValue();
      double upperBound = newSolution.getValue() + fracSol;

      if (upperBound > cStar) {
        if (undecidedArray.length == 0) { // if solution is complete array has no remaining elements
          if (newSolution.getValue() > cStar) {
            cStar = newSolution.getValue();
            sStar = newSolution;
          }
        } else {
          branch(newSolution, undecidedArray.clone());
        }
      }
    }
  }

  @Override
  public String getName() {
    return "BB(s)";
  }
}
