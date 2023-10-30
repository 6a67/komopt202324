package de.uos.inf.ko.knapsack.solver.student;

import java.util.PriorityQueue;
import de.uos.inf.ko.knapsack.FractionalSolution;
import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.SolverInterface;

/**
 * An optimal greedy solver for the fractional knapsack problem.
 *
 * @author
 */
public class FractionalSolver implements SolverInterface<FractionalSolution> {

  /**
   * This method solves the given knapsack instance using a fractional approach.
   * It creates a priority queue of items sorted by value per weight, and adds items to the solution
   * until the knapsack's capacity is reached. If an item cannot be added completely, it is added
   * fractionally. The method returns a fractional solution.
   *
   * @param instance the knapsack instance to be solved
   * @return a fractional solution to the given knapsack instance
   */
  @Override
  public FractionalSolution solve(Instance instance) {
    // iterate over values in instance, calculate value per weight and sort
    int[] weightArray = instance.getWeightArray();
    int[] valueArray = instance.getValueArray();

    // create a priority queue of items sorted by value per weight
    PriorityQueue<Item> itemQueue =
        new PriorityQueue<Item>(valueArray.length, new ItemComparator().reversed());

    // add items to queue
    for (int i = 0; i < weightArray.length; i++) {
      itemQueue.add(new Item(i, valueArray[i], weightArray[i]));
    }

    // create fractional solution
    FractionalSolution fractionalSolution = new FractionalSolution(instance);

    // add items to solution
    while (!itemQueue.isEmpty()) {
      Item item = itemQueue.poll();
      if (fractionalSolution.getWeight() + item.getWeight() <= instance.getCapacity()) {
        fractionalSolution.set(item.getIndex(), 1.0);
      } else {
        fractionalSolution.set(item.getIndex(),
            (instance.getCapacity() - fractionalSolution.getWeight()) / item.getWeight());
        break;
      }
    }
    return fractionalSolution;
  }

  @Override
  public String getName() {
    return "Fractional(s)";
  }

  /**
   * Represents an item in the knapsack problem, with its index, value, weight, and value per weight ratio.
   */
  private class Item {
    private int index;
    private int value;
    private int weight;
    private double valuePerWeight;

    public Item(int index, int value, int weight) {
      this.index = index;
      this.value = value;
      this.weight = weight;
      this.valuePerWeight = (double) value / weight;
    }

    public int getIndex() {
      return index;
    }

    public int getValue() {
      return value;
    }

    public int getWeight() {
      return weight;
    }

    public double getValuePerWeight() {
      return valuePerWeight;
    }

    public String toString() {
      return "Item: " + value + " " + weight + " " + valuePerWeight;
    }
  }


  /**
   * This private class implements the Comparator interface to compare two items based on their value per weight ratio.
   * If the value per weight ratio of the first item is greater than the second item, it returns 1.
   * If the value per weight ratio of the first item is less than the second item, it returns -1.
   * If the value per weight ratio of both items are equal, it returns 0.
   */
  private class ItemComparator implements java.util.Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
      if (item1.getValuePerWeight() > item2.getValuePerWeight()) {
        return 1;
      } else if (item1.getValuePerWeight() < item2.getValuePerWeight()) {
        return -1;
      } else {
        return 0;
      }
    }
  }
}
