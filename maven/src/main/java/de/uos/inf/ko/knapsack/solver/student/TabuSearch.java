package de.uos.inf.ko.knapsack.solver.student;

import de.uos.inf.ko.knapsack.Solution;
import de.uos.inf.ko.knapsack.SolverInterface;
import de.uos.inf.ko.knapsack.Instance;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TabuSearch is an implementation of the SolverInterface that uses the Tabu Search algorithm to solve the knapsack problem.
 * It generates an initial solution and then iteratively explores the neighborhood of the current solution by flipping one bit at a time.
 * The algorithm keeps track of a tabu list to prevent revisiting previously explored solutions.
 * The search terminates based on a specified termination condition, such as a maximum number of iterations or a limit on the number of iterations without improvement.
 */
public class TabuSearch implements SolverInterface<Solution> {

  private int tabuListSize = 100;

  private final Random random = new Random();

  public enum InitialSolutionType {
    RANDOM, GREEDY
  }

  public enum TerminationCondition {
    ITERATIONS, IMPROVEMENTLIMIT // terminates if no improvement is made for a given number of iterations
  }

  public enum AttributeType {
    SOLUTIONS, INDEX
  }

  public enum AllowedSolutions {
    FEASIBLE, INFEASIBLE
  }

  private final InitialSolutionType initialSolutionType;
  private final TerminationCondition terminationCondition;
  private final AttributeType attributeType;
  private final AllowedSolutions allowedSolutions;

  public TabuSearch() {
    this(InitialSolutionType.GREEDY, TerminationCondition.ITERATIONS, AttributeType.SOLUTIONS, AllowedSolutions.FEASIBLE);
  }

  public TabuSearch(InitialSolutionType initialSolutionType, TerminationCondition terminationCondition, AttributeType attributeType, AllowedSolutions allowedSolutions) {
    super();
    this.initialSolutionType = initialSolutionType;
    this.terminationCondition = terminationCondition;
    this.attributeType = attributeType;
    this.allowedSolutions = allowedSolutions;
  }


  /**
   * Solves the knapsack problem using the Tabu Search algorithm.
   * 
   * @param instance  The instance of the problem.
   * @return  The best solution found.
   */
  @Override
  public Solution solve(Instance instance) {
    // Print configuration
    System.out.println("Initial solution type: " + initialSolutionType);
    System.out.println("Termination condition: " + terminationCondition);
    System.out.println("Attribute type: " + attributeType);
    System.out.println("Allowed solutions: " + allowedSolutions);

    // Values for different variants of the algorithm
    final int improvementLimit = 100;
    int lastImprovement = 0;
    final int maxIterations = 1000;

    // Initialize the current solution
    Solution currentSolution = generateInitialSolution(instance, initialSolutionType);

    // Initialize the best solution
    Solution bestSolution = currentSolution;

    // Initialize the tabu list for solutions
    List<Solution> tabuList = new ArrayList<>();

    // Initialize the tabu list for indices
    List<Integer> tabuListIndices = new ArrayList<>();

    // Changed index of the current solution
    int currentIndex = -1;

    // Start the tabu search
    int iteration = 0;
    boolean stop = false;
    while (!stop) {
      // Generate the neighborhood solutions
      List<Tuple> neighborhood = generateNeighborhood(currentSolution, instance);

      // Find the best non-tabu solution in the neighborhood
      Solution bestNeighbor = null;
      for (Tuple neighbor : neighborhood) {
        Solution solution = neighbor.solution;
        int index = neighbor.index;

        // Remove infeasible solutions if necessary
        if (allowedSolutions == AllowedSolutions.FEASIBLE && !solution.isFeasible()) {
          continue;
        }

        int solValue = solution.getValue();

        if(!solution.isFeasible()) {
          double factor = 1.0 - (0.5 * ((solution.getWeight() - instance.getCapacity())) / (double) instance.getCapacity());
          solValue = (int) (solValue * factor);
        }

        // Check if the solution is tabu and update the best neighbor if necessary depending on the attribute type
        switch (attributeType) {
          case SOLUTIONS:
            if (!tabuList.contains(solution)
                && (bestNeighbor == null || solValue > bestNeighbor.getValue())) {
              bestNeighbor = solution;
              currentIndex = index;
            }
            break;
          case INDEX:
            if (!tabuListIndices.contains(index)
                && (bestNeighbor == null || solValue > bestNeighbor.getValue())) {
              bestNeighbor = solution;
              currentIndex = index;
            }
            break;
          default:
            throw new IllegalArgumentException("Unknown attribute type");
        }
      }

      // To continue the procedure, delete the oldest entry in the tabu list until a feasible neighbor exists
      if (bestNeighbor == null) {
        tabuList.remove(0);
        tabuListIndices.remove(0);
        continue;
      }

      // Update the current solution
      currentSolution = bestNeighbor;

      // Update the best solution if necessary and if it is feasible
      if (currentSolution.getValue() > bestSolution.getValue() && currentSolution.isFeasible()) {
        bestSolution = currentSolution;
        lastImprovement = iteration;
      }

      // Add the current solution to the tabu list
      tabuList.add(currentSolution);
      tabuListIndices.add(currentIndex);

      // Remove the oldest solution from the tabu list if it exceeds the tabu list size
      if (tabuList.size() > tabuListSize) {
        tabuList.remove(0);
      }

      // Remove the oldest index from the tabu list if it exceeds the tabu list size
      if (tabuListIndices.size() > tabuListSize) {
        tabuListIndices.remove(0);
      }

      iteration++;

      // Check the termination condition
      switch (terminationCondition) {
        case ITERATIONS:
          if (iteration >= maxIterations) {
            stop = true;
          }
          break;
        case IMPROVEMENTLIMIT:
          if (iteration - lastImprovement >= improvementLimit) {
            stop = true;
          }
          break;
        default:
          throw new IllegalArgumentException("Unknown termination condition");
      }
    }

    return bestSolution;
  }

  @Override
  public String getName() {
    return "Tabu(s)";
  }

  /**
   * Generates an initial solution.
   * 
   * @param instance  The instance of the problem.
   * @param type  The type of the initial solution.
   * @return  The generated solution.
   */
  private Solution generateInitialSolution(Instance instance, InitialSolutionType type) {
    Solution initialSolution = null;

    switch (type) {
      case RANDOM:
        initialSolution = generateRandomSolution(instance);
        break;
      case GREEDY:
        initialSolution = new GreedyHeuristic().solve(instance);
        break;
      default:
        throw new IllegalArgumentException("Unknown initial solution");
    }

    return initialSolution;
  }

  /**
   * Generates a random solution.
   * 
   * @param instance  The instance of the problem.
   * @return  The generated solution.
   */
  private Solution generateRandomSolution(Instance instance) {
    // random permutation of indices of items
    int[] indices = new int[instance.getWeightArray().length];
    for (int i = 0; i < indices.length; i++) {
      indices[i] = i;
    }
    for (int i = 0; i < indices.length; i++) {
      int j = random.nextInt(indices.length);
      int tmp = indices[i];
      indices[i] = indices[j];
      indices[j] = tmp;
    }

    // generate solution
    Solution solution = new Solution(instance);

    for (int i = 0; i < indices.length; i++) {
      int index = indices[i];

      if (Math.random() < 0.5) {
        continue;
      }

      if (solution.getWeight() + instance.getWeight(index) <= instance.getCapacity()) {
        solution.set(index, 1);
      }
    }

    return solution;
  }

  /**
   * Generates the neighborhood of a given solution by flipping one bit at a time.
   * Each neighbor is represented by a Tuple object containing the modified solution and the index of the flipped bit.
   *
   * @param solution  The current solution.
   * @param instance  The instance of the problem.
   * @return  The list of neighbors generated.
   */
  private List<Tuple> generateNeighborhood(Solution solution, Instance instance) {
    List<Tuple> neighborhood = new ArrayList<>();

    // generate all neighbors by flipping one bit
    for (int i = 0; i < solution.getIntegerArray().length; i++) {
      Solution neighbor = new Solution(solution);
      neighbor.set(i, 1 - neighbor.get(i));
      Tuple tuple = new Tuple(neighbor, i);
      neighborhood.add(tuple);
    }

    return neighborhood;
  }

  /**
   * Represents a tuple consisting of a solution and an index.
   */
  private class Tuple {
    public Solution solution;
    public int index;

    /**
     * Constructs a Tuple object with the given solution and index.
     * 
     * @param solution the solution
     * @param index the index
     */
    public Tuple(Solution solution, int index) {
      this.solution = solution;
      this.index = index;
    }
  }
}
