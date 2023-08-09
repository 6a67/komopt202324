package de.uos.inf.ko.knapsack;

import java.io.IOException;
import de.uos.inf.ko.knapsack.reader.Reader;
import de.uos.inf.ko.utils.Logger;

public class Runner {
  private static final String usage = "Usage: [-v|--verbose] <filename>";

  public static <SolutionType extends GenericSolution<?>> void runSolver(SolverInterface<SolutionType> solver, Instance instance, boolean binary) {
    System.out.println("=== " + solver.getClass().getName() + " ===");
    long start = System.currentTimeMillis();
    SolutionType solution = solver.solve(instance);
    long end = System.currentTimeMillis();
    if (instance.getSize() <= 60) {
      System.out.println("solution = " + solution);
    }
    System.out.println("value = " + solution.getValue());
    System.out.printf("time = %.3fs\n", (end - start) / 1000.0);
    assert solution.getInstance() == instance : "Solution is for another instance!";
    assert solution.isFeasible() : "Solution is not feasible!";
    if (binary) {
      assert solution.isBinary() : "Solution is not binary!";
    }
  }

  public static <SolutionType extends GenericSolution<?>> void runSolver(SolverInterface<SolutionType> solver, Instance instance) {
    runSolver(solver, instance, true);
  }

  public static void main(String[] args) throws IOException {
    switch (args.length) {
      case 2:
        if (args[0].equals("-v") || args[0].equals("--verbose")) {
          Logger.enable();
        } else {
          throw new IllegalArgumentException(usage);
        }
      case 1:
        Instance instance = Reader.readInstance(args[args.length - 1]);

        System.out.println("###############################################");
        System.out.println("# Instance file: " + args[0]);
        System.out.println("# Number of items: " + instance.getSize());
        System.out.println("# Capacity of knapsack: " + instance.getCapacity());
        System.out.println("###############################################");
        System.out.println();
        // TODO: runSolver(new YourSolverHere(), instance);
        break;
      default:
        throw new IllegalArgumentException(usage);
    }
  }
}
