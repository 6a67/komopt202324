package de.uos.inf.ko.skyscrapers.solver.student;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assume.assumeTrue;
import java.util.HashSet;
import java.util.Set;

import de.uos.inf.ko.skyscrapers.Instance;
import de.uos.inf.ko.skyscrapers.reader.Reader;
import org.junit.BeforeClass;
import org.junit.Test;

public class SkyscrapersSolverTest {

  public final static String SKYSCRAPERS_INSTANCES_PATH = "./src/test/resources/skyscrapers/";

  @BeforeClass
  public static void testIfImplemented() {
    try {
      CSPSolver.solve(null);
    } catch (UnsupportedOperationException ex) {
      assumeTrue(false);
    } catch (Exception ex) {
    }
  }

  @Test
  public void testFeasiblity() {
    final Instance instance = Reader.readSkyscInstance(SKYSCRAPERS_INSTANCES_PATH + "skyscrapers_5x5_easy_001.sav");
    CSPSolver.solve(instance);
    feasiblity(instance);
  }

  private static void feasiblity(Instance instance) {
    // check fixed fields
    for (int i = 0; i < instance.getGamefield().length; i++) {
      for (int j = 0; j < instance.getGamefield()[i].length; j++) {
        if (instance.getGamefield()[i][j] > 0) {
          assertEquals(instance.getGamefield()[i][j], instance.getSolution()[i][j]);
        }
      }
    }
    // check all unequal
    Set<Integer> jRow = new HashSet<Integer>(instance.getSolution().length);
    for (int i = 0; i < instance.getSolution().length; i++) {
      jRow.clear();
      for (int j = 0; j < instance.getSolution()[i].length; j++) {
        jRow.add(instance.getSolution()[i][j]);
      }
      assertEquals(jRow.size(), instance.getSolution().length);
    }

    Set<Integer> iRow = new HashSet<Integer>(instance.getSolution().length);
    for (int j = 0; j < instance.getSolution()[0].length; j++) {
      iRow.clear();
      for (int i = 0; i < instance.getSolution().length; i++) {
        iRow.add(instance.getSolution()[i][j]);
      }
      assertEquals(jRow.size(), instance.getSolution().length);
    }

    // check visible skyscrapers
    // first index north -> south
    for (int i = 0; i < instance.getSolution().length; i++) {
      if (instance.getWest()[i] != Instance.NO_VALUE) {
        assertEquals(Instance.visibleSkyscrapers(true, instance.getSolution()[i]), instance.getWest()[i]);
      }
      if (instance.getEast()[i] != Instance.NO_VALUE) {
        assertEquals(Instance.visibleSkyscrapers(false, instance.getSolution()[i]), instance.getEast()[i]);
      }
    }

    for (int j = 0; j < instance.getSolution()[0].length; j++) {
      int[] row = new int[instance.getSolution().length];
      for (int i = 0; i < instance.getSolution().length; i++) {
        row[i] = instance.getSolution()[i][j];
      }
      if (instance.getNorth()[j] != Instance.NO_VALUE) {
        assertEquals(Instance.visibleSkyscrapers(true, row), instance.getNorth()[j]);
      }
      if (instance.getSouth()[j] != Instance.NO_VALUE) {
        assertEquals(Instance.visibleSkyscrapers(false, row), instance.getSouth()[j]);
      }
    }
  }
}
