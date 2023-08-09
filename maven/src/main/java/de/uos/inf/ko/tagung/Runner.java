package de.uos.inf.ko.tagung;

import de.uos.inf.ko.tagung.solver.Utils;
import de.uos.inf.ko.tagung.solver.student.Tagung;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;

/**
 * A runner class that runs {@link Tagung}.
 *
 * @author Sven Boge
 */
public class Runner {

  public static void main(String args[]) {
    long start = System.currentTimeMillis();
    final Utils.TagungSolutions result = Tagung.runModel();
    long end = System.currentTimeMillis();
    System.out.println("Number of solutions = " + result.solutions.size());
    System.out.print("Solution format: (");
    for (int i = 0; i < result.variables.length; i++) {
      System.out.print(result.variables[i].getName() + (i+1 == result.variables.length ? "" : ","));
    }
    System.out.println(")");

    printSolutions(result.solutions, result.variables);

    System.out.printf("Loesungszeit = %.3fs\n", (end - start) / 1000.0);
  }

  /**
   * This helper method prints all given solutions.
   *
   * @param solutions solutions of the CSP
   * @param x variables of the CSP
   */
  private static void printSolutions(List<Solution> solutions, IntVar[] x) {
    int cnt = 1;
    for (Solution solution : solutions) {
      StringBuilder builder = new StringBuilder();
      builder.append(cnt + ". Solution: (");
      for(int i = 0; i < x.length; ++i) {
        builder.append(solution.getIntVal(x[i]) + (i+1 == x.length ? "" : ","));
      }
      builder.append(")");
      System.out.println(builder.toString());
      ++cnt;
    }
  }
}
