package de.uos.inf.ko.skyscrapers.solver.student;

import de.uos.inf.ko.skyscrapers.Instance;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
import java.util.List;

/**
 * A skyscrapers solver.
 *
 * @author
 */
public class CSPSolver {
  public static Instance solve(Instance instance) {
    if (true) {
      throw new UnsupportedOperationException();
    }

    // 1. create model

    // 2. create varaibles
    IntVar x[][] = null;

    // 3. add constraints

    // 4. get solver and solve model
    List<Solution> solutions = null;

    // 5. print solutions
    int size = instance.getGamefieldSize();
    System.out.println("Number of solutions: " + solutions.size());
    int cnt = 1;
    for (Solution solution : solutions) {
      int[][] solutionArray = new int[size][size];
      for (int i = 0; i < size; ++i) {
        for (int j = 0; j < size; ++j) {
          solutionArray[i][j] = solution.getIntVal(x[i][j]);
        }
      }
      if (cnt == 1) {
        instance.setSolution(solutionArray);
      }
      System.out.println("------- solution number " + cnt + "-------");
      instance.printSolution();

      ++cnt;
    }

    return instance;
  }
}
