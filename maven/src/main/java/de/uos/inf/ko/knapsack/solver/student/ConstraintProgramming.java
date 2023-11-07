package de.uos.inf.ko.knapsack.solver.student;

import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.Solution;
import de.uos.inf.ko.knapsack.SolverInterface;

/**
 * A constraint-based solver for the binary knapsack problem.
 *
 * @author
 */
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import static org.chocosolver.solver.search.strategy.Search.inputOrderLBSearch;
import static org.chocosolver.solver.search.strategy.Search.inputOrderUBSearch;
import java.util.Arrays;


public class ConstraintProgramming implements SolverInterface<Solution> {
  /**
   * Solves the given knapsack instance using constraint programming.
   *
   * @param instance the knapsack instance to solve
   * @return the solution to the knapsack instance
   */
  public Solution solve(Instance instance) {
    // 1. create model
    Model model = new Model("Binary Knapsack");

    // 2. create variables
    int n = instance.getSize();
    int[] weights = instance.getWeightArray();
    int[] values = instance.getValueArray();
    int capacity = instance.getCapacity();

    IntVar[] items = new IntVar[n];
    for (int i = 0; i < n; i++) {
      items[i] = model.intVar("item_" + (i + 1), 0, 1);
    }

    // set maximum value
    IntVar value = model.intVar("value", 0, Arrays.stream(values).max().orElse(999) * n);

    IntVar weight = model.intVar("weight", 0, capacity);

    // 3. add constraints
    model.knapsack(items, weight, value, weights, values).post();

    // 4. get solver and solve model
    model.setObjective(Model.MAXIMIZE, value);
    Solver solver = model.getSolver();
    solver.setSearch(inputOrderLBSearch(value), inputOrderUBSearch(items));

    // 5. put variable values in solution
    Solution solution = new Solution(instance);
    while (solver.solve()) {
      for (int i = 0; i < items.length; i++) {
        solution.set(i, items[i].getValue());
      }
    }

    return solution;
  }


  @Override
  public String getName() {
    return "CP(s)";
  }
}
