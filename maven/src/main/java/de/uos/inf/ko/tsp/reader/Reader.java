package de.uos.inf.ko.tsp.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import de.uos.inf.ko.tsp.Instance;

/**
 * A reader for symmetric TSP instance files. The first line of a file describes the number of cities, the
 * remaining lines contain triples (v, w, d) that describe the distance 'd' between cities 'v' and
 * 'w'. Every entry that is not explicitly defined will be treated as Double.MAX_VALUE (basically
 * infinite).
 *
 */
public class Reader {
  /**
   * Reads a symmetric TSP instance from a given file
   * 
   * @param filename Name of file to be read
   * @return TSP instance
   * @throws IOException if an IO error occurs or the instance is not defined properly
   */
  public static Instance readInstance(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(filename));
    int n = Integer.parseInt(lines.remove(0));

    final Instance instance = new Instance(n);
    double[][] result = instance.getDistances();

    for (String line : lines) {
      String[] tokens = line.split("\\s+");

      if (tokens.length != 3) {
        throw new IOException("malformed line encountered: " + line);
      }

      try {
        int v = Integer.parseInt(tokens[0]);
        int w = Integer.parseInt(tokens[1]);
        double c = Double.parseDouble(tokens[2]);

        if (result[v - 1][w - 1] != Double.MAX_VALUE) {
          throw new IOException("duplicate entry encountered: " + v + "," + w);
        }

        if (v < 1 || v > n) {
          throw new IOException("invalid node index encountered: " + v);
        }

        if (w < 1 || w > n) {
          throw new IOException("invalid node index encountered: " + w);
        }

        if (v >= w) {
          throw new IOException("invalid node pair encountered: " + v + "," + w);
        }

        result[v - 1][w - 1] = result[w - 1][v - 1] = c;
      } catch (NumberFormatException e) {
        throw new IOException(e);
      }
    }

    return instance;
  }
}
