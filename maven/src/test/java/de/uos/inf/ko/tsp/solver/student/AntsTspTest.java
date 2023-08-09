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
      Ants ants = new Ants();
      final List<Integer> tour = ants.solve(instance);
      assertFeasibility(instance, tour);
    }
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
