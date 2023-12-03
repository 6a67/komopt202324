package de.uos.inf.ko.tsp.solver.student;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assume.assumeTrue;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import de.uos.inf.ko.tsp.Instance;
import org.junit.BeforeClass;
import org.junit.Test;
import de.uos.inf.ko.tsp.reader.Reader;
import de.uos.inf.ko.tsp.solver.student.Ants;

import java.text.DecimalFormat;

public class AntsTspTest {

  private static final List<String> FILENAMES = Arrays.asList("tsp1.txt", "tsp2.txt", "tsp3.txt");

  @BeforeClass
  public static void testIfImplemented() {
    final Instance instance = new Instance(1);
    try {
      new Ants().solve(instance);
    } catch (UnsupportedOperationException ex) {
      assumeTrue(false);
    } catch (Exception ex) {
    }
  }

  @Test
  public void testAllInstances() throws IOException {
    for (String filename : FILENAMES) {
      Instance instance = Reader.readInstance("src/test/resources/tsp/" + filename);

      int[] numAnts = {10, 20, 50, 100};
      int[] numIt = {10, 50, 100};

      for (int i = 0; i < numAnts.length; i++) {
        for (int j = 0; j < numIt.length; j++) {
          Ants ants = new Ants(numAnts[i], numIt[j]);

          long startTime = System.currentTimeMillis();
          final List<Integer> tour = ants.solve(instance);
          long endTime = System.currentTimeMillis();
          long elapsedTime = endTime - startTime;

          System.out.println("instance: " + filename);
          System.out.println("numAnts: " + numAnts[i]);
          System.out.println("numIt: " + numIt[j]);

          // System.out.println("tour: " + tour);
          System.out.println("Elapsed time: " + elapsedTime + "ms");

          double cost = calculateTourCost(instance, tour);
          System.out.println("cost: " + formatCost(cost));
          System.out.println();

          assertFeasibility(instance, tour);
        }
      }
    }
  }

  private double calculateTourCost(Instance instance, List<Integer> tour) {
    double cost = 0;
    for (int i = 0; i < tour.size() - 1; i++) {
      cost += instance.getDistance(tour.get(i), tour.get(i + 1));
    }
    cost += instance.getDistance(tour.get(tour.size() - 1), tour.get(0));
    return cost;
  }

  private String formatCost(double cost) {
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    return decimalFormat.format(cost);
  }

  private static void assertFeasibility(Instance instance, List<Integer> tour) {
    final int n = instance.getNumCities();
    final boolean[] visited = new boolean[n];

    assertEquals("every city needs to be visited exactly once", n, tour.size());

    for (final Integer city : tour) {
      assertFalse("every city must be visited only once", visited[city]);
      visited[city] = true;
    }
  }

}
