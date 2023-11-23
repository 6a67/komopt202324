package de.uos.inf.ko.knapsack.solver.student;

import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.Solution;
import de.uos.inf.ko.knapsack.SolverInterface;
import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.Item;
import de.uos.inf.ko.knapsack.Solution;
import de.uos.inf.ko.knapsack.SolverInterface;
import java.util.ArrayList;
import java.util.List;

import java.util.Random;
import org.apache.commons.lang3.tuple.Pair;

public class TabuSearch implements SolverInterface<Solution> {

  private int tabuListSize = 10;

  private final Random random = new Random();

  private enum InitialSolutionType {
    RANDOM, GREEDY
  }

  private enum TerminationCondition {
    ITERATIONS, IMPROVEMENTLIMIT // terminates if no improvement is made for a given number of iterations
  }

  private enum AttributeType {
    SOLUTIONS, INDEX
  }

  private enum AllowedSolutions {
    FEASIBLE, INFEASIBLE
  }

  @Override
  public Solution solve(Instance instance) {
    // Values for different variants of the algorithm
    InitialSolutionType initialSolutionType = InitialSolutionType.GREEDY;
    TerminationCondition terminationCondition = TerminationCondition.ITERATIONS;
    final int improvementLimit = 100;
    int lastImprovement = 0;
    final int maxIterations = 1000;
    AttributeType attributeType = AttributeType.INDEX;
    AllowedSolutions allowedSolutions = AllowedSolutions.FEASIBLE;

    // If attribute type is index, the tabu list size must be smaller than the number of items
    if (attributeType == AttributeType.INDEX && tabuListSize >= instance.getSize()) {
      tabuListSize = instance.getSize() - 1;
    }

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

        // Check if the solution is tabu and update the best neighbor if necessary depending on the attribute type
        switch (attributeType) {
          case SOLUTIONS:
            if (!tabuList.contains(solution)
                && (bestNeighbor == null || solution.getValue() > bestNeighbor.getValue())) {
              bestNeighbor = solution;
              currentIndex = index;
            }
            break;
          case INDEX:
            if (!tabuListIndices.contains(index)
                && (bestNeighbor == null || solution.getValue() > bestNeighbor.getValue())) {
              bestNeighbor = solution;
              currentIndex = index;
            }
            break;
          default:
            throw new IllegalArgumentException("Unknown attribute type");
        }
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

  private class Tuple {
    public Solution solution;
    public int index;

    public Tuple(Solution solution, int index) {
      this.solution = solution;
      this.index = index;
    }
  }
}
