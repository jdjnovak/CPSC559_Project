package tb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import tb.logging.Logger;
import tb.types.Peer;

public class App {

  // static String IP = "localhost";
  // static final int PORT = 55921;
  static String IP = "34.212.129.196";
  static final int PORT = 44444;
  static final int UDP_PORT = 36636;
  static final String TEAMNAME = "Team Thunder Badger";

  static ArrayList<Peer> PEERS = new ArrayList<Peer>();
  static int SNIP_TIMESTAMP = 0; // For snip timings

  // Thread globals
  public static final int MAX_THEAD_COUNT = 20;

  // Logging object
  public static Logger log;

  // Thread Pool Object - Not to be accesible outside this class
  private static ExecutorService executor;

  public static void main(String[] args) {
    executor = Executors.newFixedThreadPool(MAX_THEAD_COUNT);
    log = new Logger(2);
    log.Log("Starting logger with debug level " + log.getDebugLevel());

    try {
      // Begin with TCP client for peer onboarding
      TCPClient client = new TCPClient(IP, PORT, TEAMNAME);
      client.Start();

      // Begin UDP client for duration as peer
      // UDPClient udpclient = new UDPClient();
      // udpclient.Start();
      executor.execute(new UDPClient());

      executor.shutdown();
      log.Log("Shutting down.");

    } catch (IOException io) {
      log.Warn("App - An IO exception occured");
    } catch (Exception e) {
      log.Warn("App - An exception occured");
    }
  }

  public static int getUDPPort() {
    return UDP_PORT;
  }

  public static void addToPeers(Peer p) {
    PEERS.add(p);
  }

  public static ArrayList<Peer> getPeerlist() {
    return PEERS;
  }

  public static int getSnipTimestamp() {
    return SNIP_TIMESTAMP;
  }

  public static void setSnipTimestamp(int newts) {
    SNIP_TIMESTAMP = newts;
  }

  public static void incrementSnipTimestamp() {
    SNIP_TIMESTAMP++;
  }
}
