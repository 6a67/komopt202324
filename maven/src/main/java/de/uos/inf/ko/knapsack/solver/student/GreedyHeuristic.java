package de.uos.inf.ko.knapsack.solver.student;

import java.util.PriorityQueue;
import de.uos.inf.ko.knapsack.FractionalSolution;
import de.uos.inf.ko.knapsack.Instance;
import de.uos.inf.ko.knapsack.Solution;
import de.uos.inf.ko.knapsack.SolverInterface;

/**
 * A sorting-based heuristic for the binary knapsack problem.
 *
 * @author
 */
public class GreedyHeuristic implements SolverInterface<Solution> {

  /**
   * Solves the given knapsack instance using a greedy heuristic that selects items with the highest
   * value per weight ratio until the knapsack is full.
   *
   * @param instance the knapsack instance to solve
   * @return a binary solution to the knapsack instance
   */
  @Override
  public Solution solve(Instance instance) {
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

    // create binary solution
    Solution binarySolution = new Solution(instance);

    // add items to solution
    while (!itemQueue.isEmpty()) {
      Item item = itemQueue.poll();
      if (binarySolution.getWeight() + item.getWeight() <= instance.getCapacity()) {
        binarySolution.set(item.getIndex(), 1);
      }
    }
    return binarySolution;
  }

  @Override
  public String getName() {
    return "Greedy(s)";
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
