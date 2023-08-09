package de.uos.inf.ko.utils;

/**
 * A logger class for debug output
 *
 * @author Stephan Beyer
 */
public class Logger {
  private static boolean enabled = false;

  public static void enable() {
    enabled = true;
  }

  public static void disable() {
    enabled = false;
  }

  public static void print(Object obj) {
    if (enabled) {
      System.out.print(obj.toString());
    }
  }

  public static void println(Object obj) {
    if (enabled) {
      System.out.println(obj.toString());
    }
  }

  public static void println() {
    if (enabled) {
      System.out.println();
    }
  }
}
