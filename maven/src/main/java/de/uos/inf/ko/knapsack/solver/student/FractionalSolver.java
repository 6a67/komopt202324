package de.uos.inf.ko.knapsack.solver.student;

import java.util.Arrays;
import java.util.Comparator;
import de.uos.inf.ko.knapsack.FractionalSolution;
import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.SolverInterface;

/**
 * An optimal fractional solver (greedy)
 *
 * @author Stephan Beyer
 */
public class FractionalSolver implements SolverInterface<FractionalSolution> {
  private Instance instance;

  private double getRatio(int i) {
    return (double) instance.getValue(i) / instance.getWeight(i);
  }

  public FractionalSolution solve(Instance inst) {
    instance = inst;

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

    // make solution
    FractionalSolution solution = new FractionalSolution(instance);
    for (int i : perm) {
      final double remaining = (double) instance.getCapacity() -
          solution.getWeight();
      if ((double) instance.getWeight(i) <= remaining) {
        solution.set(i, 1.0);
      } else {
        final double part = remaining / instance.getWeight(i);
        solution.set(i, part);
        break;
      }
    }

    return solution;
  }

  @Override
  public String getName() {
    return "Frac(l)";
  }
}
