package de.uos.inf.ko.skyscrapers;

import java.util.HashSet;
import java.util.Set;

/**
 * A skyscrapers problem instance containing... ... a matrix that represents the game filed with all
 * sub fields. If the value of a subfield is fixed a value > 0 is given, zero indicates that the
 * value of the field is not fixed. ... four arrays with north /south / east / west constraints that
 * indicate the number of visible skyscrapers in this cardinal direction.
 *
 * @author Sven Boge
 */
public class Instance {

  public static final int NO_VALUE = 0;

  /**
   * Game field matrix The first index runs in north south direction, the second index runs in west
   * east direction.
   */
  private int[][] gamefield;

  /**
   * Solution to the given instance
   */
  private int[][] solution;

  /**
   * Is the set solution feasible w.r.t. the instance?
   */
  private boolean feasible;

  /**
   * Arrays refering to the four cardinal directions
   */
  private int[] north;
  private int[] east;
  private int[] south;
  private int[] west;

  /**
   * Constructs an instance for given matrix and arrays.
   *
   * @param matrix game field matrix
   * @param north north constraints
   * @param east east constraints
   * @param south south constraints
   * @param west west constraints
   */
  public Instance(int[][] matrix, int[] north, int[] east, int[] south, int[] west) {
    if (north.length != east.length || east.length != west.length || west.length != south.length || south.length != matrix.length
        || matrix.length != matrix[0].length) {
      throw new IllegalArgumentException("Constraint arrays do not fit game field.");
    }
    this.gamefield = matrix;
    this.north = north;
    this.east = east;
    this.south = south;
    this.west = west;
    this.solution = null;
  }

  /**
   * Get the size of the gamefield.
   */
  public int getGamefieldSize() {
    return this.gamefield.length;
  }

  public int[][] getGamefield() {
    return gamefield;
  }

  public int[] getNorth() {
    return north;
  }

  public int[] getEast() {
    return east;
  }

  public int[] getSouth() {
    return south;
  }

  public int[] getWest() {
    return west;
  }

  public int[][] getSolution() {
    return solution;
  }

  /**
   * Prints the solution if available.
   */
  public void printSolution() {
    if (this.solution == null) {
      System.out.println("Board output omitted, no solution found.");
    }
    printMatrix(this.solution);
  }

  /**
   * Prints the gamefield.
   */
  public void printGamefield() {
    printMatrix(this.gamefield);
  }

  /**
   * Prints a given matrix and adds north, west, east, south constraints.
   *
   * @param matrix the matrix ti print
   */
  private void printMatrix(int[][] matrix) {
    final String lineSep = "\n";
    if (this.getGamefieldSize() > 9) {
      System.out.println("Board output omitted due to size.");
    } else {
      StringBuilder ret = new StringBuilder();
      // append north
      ret.append("  ");
      for (int j = 0; j < this.getGamefieldSize(); j++) {
        ret.append("  ");
        if (this.north[j] != NO_VALUE) {
          ret.append(this.north[j]);
        } else {
          ret.append(" ");
        }
        ret.append(" ");
      }
      ret.append(lineSep);
      String rowSep = "  +";
      for (int i = 0; i < this.getGamefieldSize(); i++)
        rowSep += "---+";
      rowSep += lineSep;
      ret.append(rowSep);
      for (int j = 0; j < this.getGamefieldSize(); j++) {
        if (this.west[j] != NO_VALUE) {
          ret.append(this.west[j] + " ");
        } else {
          ret.append("  ");
        }
        for (int i = 0; i < this.getGamefieldSize(); i++) {
          ret.append("| " + matrix[j][i] + " ");
        }
        ret.append("| ");
        if (this.east[j] != NO_VALUE) {
          ret.append(this.east[j] + " ");
        } else {
          ret.append("  ");
        }
        ret.append(lineSep + rowSep);
      }
      // append south
      ret.append("  ");
      for (int j = 0; j < this.getGamefieldSize(); j++) {
        ret.append("  ");
        if (this.south[j] != NO_VALUE) {
          ret.append(this.south[j]);
        } else {
          ret.append(" ");
        }
        ret.append(" ");
      }
      System.out.printf("%s\n", ret.toString());
    }
  }

  /**
   * Sets a given solution to this instance.
   *
   * @param gamefield the solution to set
   * @return true if the solution was feasible, false else
   */
  public boolean setSolution(int[][] gamefield) {
    this.solution = gamefield;
    if (this.gamefield != null) {
      feasible = this.solutionIsFeasible();
    } else {
      feasible = false;
    }
    return feasible;
  }

  private boolean solutionIsFeasible() {
    // check fixed fields
    for (int i = 0; i < gamefield.length; i++) {
      for (int j = 0; j < gamefield[i].length; j++) {
        if ((gamefield[i][j] > 0 && gamefield[i][j] != solution[i][j])) {
          // fixed number wrong
          return false;
        }
      }
    }
    // check all unequal
    Set<Integer> jRow = new HashSet<Integer>(solution.length);
    for (int i = 0; i < solution.length; i++) {
      jRow.clear();
      for (int j = 0; j < solution[i].length; j++) {
        jRow.add(solution[i][j]);
      }
      if (jRow.size() != solution.length) {
        // not all unequal in jth column
        return false;
      }
    }

    Set<Integer> iRow = new HashSet<Integer>(solution.length);
    for (int j = 0; j < solution[0].length; j++) {
      iRow.clear();
      for (int i = 0; i < solution.length; i++) {
        iRow.add(solution[i][j]);
      }
      if (jRow.size() != solution.length) {
        // not all unequal in ith column
        return false;
      }
    }

    // check visible skyscrapers
    // first index north -> south
    for (int i = 0; i < solution.length; i++) {
      if ((this.getWest()[i] != Instance.NO_VALUE && visibleSkyscrapers(true, solution[i]) != this.getWest()[i])
          || (this.getEast()[i] != Instance.NO_VALUE && visibleSkyscrapers(false, solution[i]) != this.getEast()[i])) {
        // wrong visible skyscrapers number
        return false;
      }
    }

    for (int j = 0; j < solution[0].length; j++) {
      int[] row = new int[solution.length];
      for (int i = 0; i < solution.length; i++) {
        row[i] = solution[i][j];
      }
      if ((this.getNorth()[j] != Instance.NO_VALUE && visibleSkyscrapers(true, row) != this.getNorth()[j])
          || (this.getSouth()[j] != Instance.NO_VALUE && visibleSkyscrapers(false, row) != this.getSouth()[j])) {
        // wrong visible skyscrapers number
        return false;
      }
    }

    return true;
  }

  public static int visibleSkyscrapers(boolean rightSide, int[] row) {
    int visibleCnt = 0;
    int highestHeight = 0;
    if (rightSide) {
      for (int i = 0; i < row.length; i++) {
        if (row[i] > highestHeight) {
          highestHeight = row[i];
          visibleCnt++;
        }
      }
    } else {
      for (int i = row.length - 1; i >= 0; i--) {
        if (row[i] > highestHeight) {
          highestHeight = row[i];
          visibleCnt++;
        }
      }
    }
    return visibleCnt;
  }

  public boolean solvedFeasible() {
    return feasible;
  }
}
