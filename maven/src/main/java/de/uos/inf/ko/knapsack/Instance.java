package de.uos.inf.ko.knapsack;

/**
 * A knapsack problem instance containing values and weights for each item, and a weight limit of
 * the knapsack.
 *
 * @author Stephan Beyer
 */
public class Instance {
  /**
   * Array of values
   */
  private int[] c;

  /**
   * Array of weights
   */
  private int[] w;

  /**
   * Weight limit
   */
  private int W;

  /**
   * Filename of the instance file
   */
  private String filename;

  /**
   * Constructs an instance for given item number
   *
   * @param number of items in the instance
   */
  public Instance(int number) {
    c = new int[number];
    w = new int[number];
    this.filename = "";
  }

  /**
   * Constructs a new instance of the knapsack problem with the given number of items and capacity.
   *
   * @param number the number of items in the instance
   * @param capacity the capacity of the knapsack
   */
  public Instance(int number, int capacity) {
    c = new int[number];
    w = new int[number];
    this.filename = "";
    this.W = capacity;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return this.filename;
  }

  /**
   * Set value and weight for an item.
   *
   * @param item Item index
   * @param value Value of item to be set
   * @param weight Weight of item to be set
   */
  public void set(int item, int value, int weight) {
    setValue(item, value);
    setWeight(item, weight);
  }

  /**
   * Get value for an item.
   *
   * @param item Item index
   */
  public int getValue(int item) {
    return c[item];
  }

  /**
   * Get array containing all values
   */
  public int[] getValueArray() {
    return c;
  }

  /**
   * Set value for an item.
   *
   * @param item Item index
   * @param value Value of item to be set
   */
  public void setValue(int item, int value) {
    c[item] = value;
  }

  /**
   * Get weight for an item.
   *
   * @param item Item index
   */
  public int getWeight(int item) {
    return w[item];
  }

  /**
   * Get array containing all weights
   */
  public int[] getWeightArray() {
    return w;
  }

  /**
   * Set weight for an item.
   *
   * @param item Item index
   * @param weight Weight of item to be set
   */
  public void setWeight(int item, int weight) {
    w[item] = weight;
  }

  /**
   * Get weight limit (capacity of knapsack).
   */
  public int getCapacity() {
    return W;
  }

  /**
   * Set weight limit (capacity of knapsack).
   *
   * @param capacity The capacity
   */
  public void setCapacity(int capacity) {
    W = capacity;
  }

  /**
   * Get number of item.
   */
  public int getSize() {
    return c.length;
  }
}
