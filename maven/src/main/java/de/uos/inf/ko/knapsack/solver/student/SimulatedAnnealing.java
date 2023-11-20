package de.uos.inf.ko.knapsack.solver.student;

import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.Solution;
import de.uos.inf.ko.knapsack.SolverInterface;

/**
 * A solver for the binary knapsack problem based on simulated annealing.
 *
 * @author
 */
import java.util.Random;

public class SimulatedAnnealing implements SolverInterface<Solution> {

  private final Random random = new Random();
  private Instance instance;


  private enum InitialSolution {
    RANDOM, GREEDY
  }

  private enum AnnealingSchedule {
    LINEAR, INVERSE
  }

  private enum BreakCondition {
    ITERATIONS, TEMPERATURE
  }

  private enum Reheat {
    NONE, CONST
  }


  private Solution generateRandomSolution(Instance instance) {
    Solution solution = new Solution(instance);
    int[] weights = instance.getWeightArray();
    int capacity = instance.getCapacity();

    for (int i = 0; i < weights.length; i++) {
      if (random.nextBoolean()) {
        if (solution.getWeight() + weights[i] <= capacity) {
          solution.set(i, 1);
        }
      }
    }
    return solution;
  }


  private Solution flipRandomBit(Solution solution) {
    Solution newSolution = new Solution(solution);

    while (true) {
      int index = random.nextInt(newSolution.getIntegerArray().length);
      int value = newSolution.get(index);

      if (value == 0) {
        if (newSolution.getWeight() + instance.getWeight(index) <= instance.getCapacity()) {
          newSolution.set(index, 1);
          break;
        }
      } else {
        newSolution.set(index, 0);
        break;
      }
    }
    return newSolution;
  }

  double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
}

  @Override
  public Solution solve(Instance instance) {
    this.instance = instance;

    InitialSolution initialSolution = InitialSolution.RANDOM;
    AnnealingSchedule annealingSchedule = AnnealingSchedule.INVERSE;
    BreakCondition breakCondition = BreakCondition.ITERATIONS;
    Reheat reheat = Reheat.NONE;

    // Values for BreakCondition.ITERATIONS and BreakCondition.TEMPERATURE
    final int maxIterations = 10000;
    final double minTemp = 0.0001;

    // Initial temperature
    double temperature = 1000;

    int i = 0;
    Solution s;

    switch (initialSolution) {
      case RANDOM:
        s = generateRandomSolution(instance);
        break;
      case GREEDY:
        s = new GreedyHeuristic().solve(instance);
        break;
      default:
        throw new IllegalArgumentException("Unknown initial solution");
    }

    double c = s.getValue();

    Solution sStar = s;
    double cStar = sStar.getValue();

    boolean stop = false;

    do {
      Solution sT = flipRandomBit(s);
      double cT = sT.getValue();
      // System.out.println("cT:" + cT);

      switch (annealingSchedule) {
        case LINEAR:
          temperature = 0.9999 * temperature;
          break;
        case INVERSE:
          temperature = temperature / (i * 0.000000001 + 1);
          break;
        default:
          throw new IllegalArgumentException("Unknown annealing schedule");
      }


      // System.out.println("Double ranmdom:" + random.nextDouble());
      // System.out.println("Math.exp(-(cT - c) / temperature):" + Math.exp(-(cT - c) / temperature));
      // System.out.println("Temperature:" + temperature);

      double expValue = clamp(Math.exp(-(cT - c) / temperature), 0.0, 1.0);


      if ((cT >= c) || (random.nextDouble() < expValue)) {
        s = sT;
        c = cT;
        if (cT > cStar) {
          sStar = sT;
          cStar = cT;

          switch (reheat) {
            case NONE:
              break;
            case CONST:
              temperature = 10;
              break;
            default:
              throw new IllegalArgumentException("Unknown reheat");
          }

        }
      }
      i++;

      switch (breakCondition) {
        case ITERATIONS:
          stop = i >= maxIterations;
          break;
        case TEMPERATURE:
          stop = temperature <= minTemp;
          break;
        default:
          throw new IllegalArgumentException("Unknown break condition");
      }

    } while (!stop);

    return sStar;
  }


  @Override
  public String getName() {
    return "SA(s)";
  }
}
