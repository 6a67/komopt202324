package de.uos.inf.ko.knapsack.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;
import org.junit.Test;
import de.uos.inf.ko.knapsack.Instance;

public class ReaderTest {

  @Test
  public void test() {
    try {
      Instance instance = Reader.readInstance("./src/test/resources/knapsack/tiny-rucksack-0.txt");
      assertEquals(10, instance.getCapacity());
      assertEquals(3, instance.getSize());

      assertEquals(5, instance.getWeight(0));
      assertEquals(3, instance.getWeight(1));
      assertEquals(4, instance.getWeight(2));

      assertEquals(4, instance.getValue(0));
      assertEquals(1, instance.getValue(1));
      assertEquals(2, instance.getValue(2));
    } catch (IOException e) {
      fail("could not load instance 'tiny-rucksack-0.txt'");
    }
  }

}
