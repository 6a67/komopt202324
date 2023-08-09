package de.uos.inf.ko.knapsack.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import de.uos.inf.ko.knapsack.Instance;

/**
 * A reader for instance files for knapsack problems
 * 
 * @author Jana Lehnfeld, Stephan Beyer, Sven Boge
 */
public class Reader {
  /**
   * Read knapsack instance from given file.
   *
   * @param filename The filename of the file to read
   * @throws IOException
   */
  public static Instance readInstance(String filename) throws IOException {

    Instance instance = null;

    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      int n = Integer.parseInt(reader.readLine().trim());
      instance = new Instance(n);

      for (int i = 0; i < n; i++) {
        String[] line = reader.readLine().trim().split("\\s+");
        if (line.length != 2) {
          throw new IOException("Item format invalid");
        }
        instance.set(i, Integer.parseInt(line[0]), Integer.parseInt(line[1]));
      }

      instance.setCapacity(Integer.parseInt(reader.readLine().trim()));
    }
    return instance;
  }
}
