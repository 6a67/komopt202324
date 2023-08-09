package de.uos.inf.ko.skyscrapers.reader;

import de.uos.inf.ko.skyscrapers.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;/**
 * A reader for instance files for skyscrapers problems
 * 
 * @author Sven Boge
 */
public class Reader {
  /**
   * Read skyscrapers instance from given file.
   *
   * @param filename The filename of the file to read
   * @return the read instance
   * @throws IOException
   */
  public static Instance readSkyscInstance(String filename) {
    Instance instance = null;
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      final int prevLines = 5;
      String line;
      String[] tokens;
      int lineCnt = 0;
      int northSouthSize = 0;
      int westEastSize = 0;
      int[] north = null;
      int[] east = null;
      int[] south = null;
      int[] west = null;
      int[][] gamefield = null;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.startsWith("#"))
          continue;

        tokens = line.split(" ");
        switch (lineCnt) {
          case 0:
            northSouthSize = Integer.parseInt(tokens[0]);
            westEastSize = Integer.parseInt(tokens[1]);
            break;
          case 1:
            north = new int[northSouthSize];
            for (int i = 0; i < northSouthSize; ++i) {
              north[i] = Integer.parseInt(tokens[i]);
            }
            break;
          case 2:
            east = new int[westEastSize];
            for (int i = 0; i < westEastSize; ++i) {
              east[i] = Integer.parseInt(tokens[i]);
            }
            break;
          case 3:
            south = new int[northSouthSize];
            for (int i = 0; i < westEastSize; ++i) {
              south[i] = Integer.parseInt(tokens[i]);
            }
            break;
          case 4:
            west = new int[westEastSize];
            for (int i = 0; i < westEastSize; ++i) {
              west[i] = Integer.parseInt(tokens[i]);
            }
            break;
          case 5:
            gamefield = new int[northSouthSize][westEastSize];
          default:
            for (int i = 0; i < northSouthSize; ++i) {
              gamefield[lineCnt - prevLines][i] = Integer.parseInt(tokens[i]);
            }
            break;
        }
        ++lineCnt;
      }
      instance = new Instance(gamefield, north, east, south, west);
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println(">>>>>>>File does not exist or could not be read.<<<<<<<");
    }
    return instance;
  }
}
