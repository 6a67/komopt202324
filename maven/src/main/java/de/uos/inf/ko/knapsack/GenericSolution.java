package de.uos.inf.ko.knapsack;

import java.util.ArrayList;

/**
 * Solution of a generic knapsack problem
 *
 * @author Stephan Beyer
 */
public abstract class GenericSolution<QuantityType extends Number> {
  protected Instance instance;
  protected ArrayList<QuantityType> sol;
  protected QuantityType solValue;
  protected QuantityType solWeight;

  public GenericSolution(Instance instance) {
    this.instance = instance;
    solValue = zero();
    solWeight = zero();
    sol = new ArrayList<QuantityType>();
    for (int i = 0; i < instance.getSize(); ++i) {
      sol.add(zero());
    }
  }

  /**
   * Copy a solution (copy constructor)
   */
  public GenericSolution(GenericSolution<QuantityType> solution) {
    this(solution.getInstance());
    for (int i = 0; i < instance.getSize(); ++i) {
      set(i, solution.get(i));
    }
  }

  /**
   * Returns the instance
   */
  public Instance getInstance() {
    return instance;
  }

  /**
   * Assign a quantity to an item
   *
   * @param item The index of the item
   * @param quantity The quantity to be assigned
   */
  public abstract void set(int item, QuantityType quantity);

  /**
   * Get the quantity of an item
   *
   * @param item The index of the item
   */
  public QuantityType get(int item) {
    return sol.get(item);
  }

  /**
   * Get the array (integer) of all quantities
   */
  public int[] getIntegerArray() {
    int[] array = new int[sol.size()];
    for (int i = 0; i < sol.size(); ++i) {
      array[i] = (Integer) sol.get(i);
    }
    return array;
  }

  /**
   * Get the solution value
   */
  public QuantityType getValue() {
    return solValue;
  }

  /**
   * Get the solution weight
   */
  public QuantityType getWeight() {
    return solWeight;
  }

  /**
   * Check if the solution is feasible
   */
  public abstract boolean isFeasible();

  /**
   * Check if the solution is a binary solution
   */
  public abstract boolean isBinary();

  /**
   * Return zero for the given quantity type
   */
  protected abstract QuantityType zero();

  @Override
  public String toString() {
    String str = "";
    for (QuantityType quantity : sol) {
      str += quantity + " ";
    }
    return str.trim();
  }
}
