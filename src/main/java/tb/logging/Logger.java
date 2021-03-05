package tb.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Logger.java
 *   Contains the class that handles all CLI logging
 */
public class Logger {
  // ASCII CODES FOR COLOURS
  // Taken from:
  //
  // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_WHITE = "\u001B[37m";

  private int DEBUG_LEVEL;

  /*
   * params: none
   *   No parameters constructor
   * returns: void
   */
  public Logger() {
    this.DEBUG_LEVEL = 1;
  }

  /*
   * params:
   *   Parameterized Constructor
   *   - Pass in a level to set the logger to
   *   - Levels:
   *     0 : None
   *     1 : Errors/Exceptions
   *     2 : General Logging and Errors
   *     3 : Verbose
   * returns: void
   */
  public Logger(int level) {
    this.DEBUG_LEVEL = level;
  }

  /* params: none
   *   Returns the prefix to be used by the logger
   * returns: string
   */
  public String printPrefix() {
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    return fmt.format(date);
  }

  /* params: string
   *   Prints a log message to the console
   * returns: void
   */
  public void Log(String message) {
    if (this.DEBUG_LEVEL > 1)
      System.out.println(
          "["
              + ANSI_BLUE
              + printPrefix()
              + ANSI_RESET
              + "]("
              + ANSI_GREEN
              + "LOG"
              + ANSI_RESET
              + ") "
              + ANSI_RESET
              + message);
  }

  /* params: string
   *   Prints a debug message to the console
   * returns: void
   */
  public void Debug(String message) {
    if (this.DEBUG_LEVEL > 2)
      System.out.println(
          "["
              + ANSI_BLUE
              + printPrefix()
              + ANSI_RESET
              + "]("
              + ANSI_GREEN
              + "DEBUG"
              + ANSI_RESET
              + ") "
              + ANSI_CYAN
              + message
              + ANSI_RESET);
  }

  /* params: string
   *   Prints a warning log message to the console
   * returns: string
   */
  public void Warn(String message) {
    if (this.DEBUG_LEVEL > 0)
      System.out.println(
          "["
              + ANSI_BLUE
              + printPrefix()
              + ANSI_RESET
              + "]("
              + ANSI_GREEN
              + "WARN"
              + ANSI_RESET
              + ") "
              + ANSI_RED
              + message
              + ANSI_RESET);
  }

  /* params: string
   *   Prints a line for a prompt
   * returns: string
   */
  public void Prompt(String message) {
    if (this.DEBUG_LEVEL > 0)
      System.out.print(
          "["
              + ANSI_BLUE
              + printPrefix()
              + ANSI_RESET
              + "]("
              + ANSI_GREEN
              + "PROMPT"
              + ANSI_RESET
              + ") "
              + ANSI_YELLOW
              + message
              + ANSI_RESET);
  }

  /* params: none
   *   Returns current debug level
   * returns: int
   */
  public int getDebugLevel() {
    return this.DEBUG_LEVEL;
  }
}
