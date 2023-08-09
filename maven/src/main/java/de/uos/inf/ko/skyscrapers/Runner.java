package de.uos.inf.ko.skyscrapers;

import de.uos.inf.ko.skyscrapers.reader.Reader;
import de.uos.inf.ko.skyscrapers.solver.student.CSPSolver;

import java.io.IOException;

/**
 * A runner class that runs instances with {@link CSPSolver}.
 *
 * @author Sven Boge
 */
public class Runner {

  public static void main(String args[]) throws IOException {
    if (args != null && args.length > 0) {
      System.out.println("Read file " + args[0] + ".");
      Instance instance = Reader.readSkyscInstance(args[0]);
      System.out.println("Gamefield:");
      instance.printGamefield();
      long start = System.currentTimeMillis();
      CSPSolver.solve(instance);
      long end = System.currentTimeMillis();
      System.out.printf("time = %.3fs\n", (end - start) / 1000.0);
    } else {
      System.out.println("Please enter a skyscrapers file.");
    }
  }
}
