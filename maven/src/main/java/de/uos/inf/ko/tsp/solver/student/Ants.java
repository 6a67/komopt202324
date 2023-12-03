package de.uos.inf.ko.tsp.solver.student;

import java.util.List;
import de.uos.inf.ko.tsp.Instance;

import java.util.ArrayList;
import java.util.Random;

/**
 * The Ants class represents an implementation of the Ant Colony Optimization algorithm for solving the Traveling Salesman Problem (TSP).
 * It uses pheromone trails and visibility information to guide the construction of tours by a group of ants.
 * The algorithm iteratively constructs tours and updates the pheromone matrix based on the tour lengths.
 */
public class Ants {
  private double[][] tau; // pheromone matrix
  private double[][] eta; // visibility matrix
  private double[][] delta; // pheromone update matrix

  private double alpha; // pheromone importance factor
  private double beta; // visibility importance factor
  private double rho; // pheromone evaporation rate

  private int numAnts; // number of ants
  private int numIterations; // number of iterations

  private Random random = new Random();

  public Ants() {
    this(1.0, 5.0, 0.5, 100, 100);
  }

  public Ants(int numAnts, int numIterations) {
    this(1.0, 5.0, 0.5, numAnts, numIterations);
  }

  public Ants(double alpha, double beta, double rho, int numAnts, int numIterations) {
    this.alpha = alpha;
    this.beta = beta;
    this.rho = rho;
    this.numAnts = numAnts;
    this.numIterations = numIterations;
  }

  /**
   * Solves a given TSP instance with the Ants algorithm.
   * 
   * @param instance TSP instance
   * @return TSP tour described as a list of cities
   */
  public List<Integer> solve(Instance instance) {
    int numCities = instance.getNumCities();
    initializePheromoneMatrix(numCities);
    initializeVisibilityMatrix(instance);

    List<Integer> bestTour = null;
    double bestTourCost = Double.POSITIVE_INFINITY;

    for (int iteration = 0; iteration < numIterations; iteration++) {
      List<List<Integer>> antTours = new ArrayList<>();

      for (int ant = 0; ant < numAnts; ant++) {
        List<Integer> tour = constructTour(instance);
        antTours.add(tour);

        double tourCost = computeCost(instance, tour);
        if (tourCost < bestTourCost) {
          bestTour = tour;
          bestTourCost = tourCost;
        }
      }

      updatePheromoneMatrix(instance, antTours);
    }

    return bestTour;
  }

  /**
   * Initializes the pheromone matrix with initial values.
   * 
   * @param numCities the number of cities in the problem instance
   */
  private void initializePheromoneMatrix(int numCities) {
    tau = new double[numCities][numCities];
    double initialPheromone = 1.0 / numCities;

    for (int i = 0; i < numCities; i++) {
      for (int j = 0; j < numCities; j++) {
        tau[i][j] = initialPheromone;
      }
    }
  }

  private void initializeVisibilityMatrix(Instance instance) {
    int numCities = instance.getNumCities();
    eta = new double[numCities][numCities];

    for (int i = 0; i < numCities; i++) {
      for (int j = 0; j < numCities; j++) {
        if (i != j) {
          eta[i][j] = 1.0 / instance.getDistance(i, j);
        }
      }
    }
  }

  /**
   * Constructs a tour for the given instance using the Ant Colony Optimization algorithm.
   * 
   * @param instance the instance of the problem
   * @return a list of integers representing the tour
   */
  private List<Integer> constructTour(Instance instance) {
    int numCities = instance.getNumCities();
    List<Integer> tour = new ArrayList<>();
    boolean[] visited = new boolean[numCities];
    int startCity = random.nextInt(numCities);

    tour.add(startCity);
    visited[startCity] = true;

    for (int i = 1; i < numCities; i++) {
      int currentCity = tour.get(i - 1);
      int nextCity = selectNextCity(instance, currentCity, visited);
      tour.add(nextCity);
      visited[nextCity] = true;
    }

    return tour;
  }

  /**
   * Selects the next city to visit based on the given parameters.
   *
   * @param instance The instance of the problem.
   * @param currentCity The current city being visited.
   * @param visited An array indicating which cities have been visited.
   * @return The index of the next city to visit.
   * @throws IllegalStateException If unable to select the next city.
   */
  private int selectNextCity(Instance instance, int currentCity, boolean[] visited) {
    int numCities = instance.getNumCities();
    double[] probabilities = new double[numCities];
    double totalProbability = 0.0;

    // Calculate the probabilities for unvisited cities
    for (int city = 0; city < numCities; city++) {
      if (!visited[city]) {
        double pheromone = Math.pow(tau[currentCity][city], alpha);
        double visibility = Math.pow(eta[currentCity][city], beta);
        probabilities[city] = pheromone * visibility;
        totalProbability += probabilities[city];
      }
    }

    // Select a random value within the total probability
    double randomValue = random.nextDouble() * totalProbability;
    double cumulativeProbability = 0.0;

    // Find the city corresponding to the selected random value
    for (int city = 0; city < numCities; city++) {
      if (!visited[city]) {
        cumulativeProbability += probabilities[city];
        if (randomValue <= cumulativeProbability) {
          return city;
        }
      }
    }

    throw new IllegalStateException("Unable to select next city.");
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

  /**
   * Updates the pheromone matrix based on the ant tours.
   * 
   * @param instance The instance of the problem.
   * @param antTours The list of ant tours.
   */
  private void updatePheromoneMatrix(Instance instance, List<List<Integer>> antTours) {
    int numCities = instance.getNumCities();
    delta = new double[numCities][numCities];

    // Update delta matrix based on the tour lengths
    for (List<Integer> tour : antTours) {
      double tourLength = computeCost(instance, tour);

      for (int i = 0; i < numCities; i++) {
        int currentCity = tour.get(i);
        int nextCity = tour.get((i + 1) % numCities);
        delta[currentCity][nextCity] += 1.0 / tourLength;
      }
    }

    // Update tau matrix based on the evaporation and deposition of pheromones
    for (int i = 0; i < numCities; i++) {
      for (int j = 0; j < numCities; j++) {
        tau[i][j] = (1 - rho) * tau[i][j] + delta[i][j];
      }
    }
  }
}
