package de.uos.inf.ko.tsp.solver.student;

import java.util.List;
import de.uos.inf.ko.tsp.Instance;

public class Ants {
  /**
   * Solves a given TSP instance with the Ants algorithm.
   * 
   * @param instance TSP instance
   * @return TSP tour described as a list of cities
   */
  public List<Integer> solve(Instance instance) {
    throw new UnsupportedOperationException();
  }

  /**
   * Computes the length of a given tour. Note that this method does not check whether every city is
   * visited.
   * 
   * @param instance TSP instance
   * @param tour List of cities
   * @return total distance of the tour
   */
  private double computeCost(Instance instance, List<Integer> tour) {
    double result = 0;

    for (int v : tour) {
      int w = tour.get((tour.indexOf(v) + 1) % tour.size());
      result += instance.getDistance(v, w);
    }

    return result;
  }
}
