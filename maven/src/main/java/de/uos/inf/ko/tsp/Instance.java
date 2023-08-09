package de.uos.inf.ko.tsp;

/**
 * A TSP problem instance containing distances between a set of cities. The 'n' cities are labeled
 * 0, ..., n - 1.
 *
 */
public class Instance {

  private final int n;
  private final double[][] distances;

  /**
   * Initializes a TSP instance with 'n' cities and sets all distances to infinity.
   * 
   * @param n number of cities
   */
  public Instance(int n) {
    this.n = n;
    this.distances = new double[n][n];

    for (int v = 0; v < n; v++) {
      for (int w = 0; w < n; w++) {
        this.distances[v][w] = Double.MAX_VALUE;
      }
    }
  }

  /**
   * Gets the number of cities in the TSP instance
   * 
   * @return number of cities
   */
  public int getNumCities() {
    return this.n;
  }

  /**
   * Gets the distance matrix of the TSP instance
   * 
   * @return two-dimensional array of distances
   */
  public double[][] getDistances() {
    return this.distances;
  }

  /**
   * Gets the distance between two cities.
   * 
   * @param i First city
   * @param j Second city
   * @return distance from the first city to the second city
   */
  public double getDistance(int i, int j) {
    return this.distances[i][j];
  }

  /**
   * Sets the distance between two cities
   * 
   * @param i First city
   * @param j Second city
   * @param distance Distance from the first city to the second city
   */
  public void setDistance(int i, int j, double distance) {
    this.distances[i][j] = distance;
  }
}
