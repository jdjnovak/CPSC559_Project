package tb;

import java.net.SocketException;

public class Main {
  /*
   * Program variables set by CLI arguments
   */
  private static int UDP_PORT;
  private static int DEBUG_LEVEL;

  /* params: cli arguments
   *   Program entry point
   * returns: void
   */
  public static void main(String[] args) {
    parseArgs(args);
	try {
    tb.App.start(UDP_PORT, DEBUG_LEVEL);
	} catch (SocketException se) {
	  System.out.println("SE");
	}
  }

  /* params: void
   *   Prints the usage
   * returns: void
   */
  private static void usage() {
    System.out.println(
        "USAGE: java -cp target/<JAR name> tb.Main [flags]\n\t-d <num> | --debug <num>\t\tThe level for the debugger\n\t-u <port> | --udp <port>\t\tThe UDP port number");
  }

  /* params: String[]
   *   Parse and set the arguments for the application entry point
   * returns: void
   */
  private static void parseArgs(String[] ags) {
    if (ags.length > 0) {
      /*
       * Valid arguments:
       *   -d | --debug <level> : the debug level
       *   -u | --udp <port>    : the UDP port
       */
      for (int i = 0; i < ags.length; i++) {
        try {
          if (ags[i].equals("-d") || ags[i].equals("--debug")) {
            DEBUG_LEVEL = Integer.parseInt(ags[i + 1]);
          } else if (ags[i].equals("-u") || ags[i].equals("--udp")) {
            UDP_PORT = Integer.parseInt(ags[i + 1]);
          }
        } catch (ArrayIndexOutOfBoundsException ai) {
          System.out.println("ERROR: Argument parsing error.\n");
          usage();
          System.exit(-1);
        }
      }
    } else {
      // DEFAULTS
      UDP_PORT = 36636;
      DEBUG_LEVEL = 2;
    }
  }
}
