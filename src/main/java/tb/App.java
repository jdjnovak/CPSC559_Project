package tb;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import tb.logging.Logger;
import tb.types.Peer;
import tb.types.Snip;

/*
 * Class that runs the application
 */
public class App {

  /*
   * All hardcoded structures not set to change
   */
  static final String IP = "136.159.5.22";
  static final int PORT = 55921;
  static final String TEAMNAME = "Team Thunder Badger";

  /*
   * All peer related structures
   */
  static String INITIAL_PEERS_TIMESTAMP; // Timestamp of when the registry sent the initial list
  static ArrayList<Peer> INITIAL_PEERS =
      new ArrayList<Peer>(); // Initial peerl list from the registry
  static CopyOnWriteArrayList<Peer> PEERS =
      new CopyOnWriteArrayList<Peer>(); // Master (unique) peer list
  static CopyOnWriteArrayList<Peer> ALL_PEERS =
      new CopyOnWriteArrayList<Peer>(); // All received peers (not unique)
  static CopyOnWriteArrayList<String> SENT_PEERS =
      new CopyOnWriteArrayList<String>(); // All sent peer messages

  /*
   * All snip related information
   */
  static int SNIP_TIMESTAMP = 0; // Snip timestamp
  static CopyOnWriteArrayList<Snip> SNIPS = new CopyOnWriteArrayList<Snip>(); //

  /*
   * Threading information
   */
  public static final int MAX_THEAD_COUNT = 20;
  public static ExecutorService executor;

  /*
   * All major parameters
   */
  public static Logger log;
  static int UDP_PORT;
  static int LOG_LEVEL;

  /*
   * Other necessary global objects
   */
  public static DatagramSocket SOCKET;
  public static boolean STOP;

  /* params: int, int
   *   Starts the application
   * returns: void
   */
  public static void start(int u, int l) throws SocketException {
    // Set globals
    UDP_PORT = u;
    log = new Logger(l);
    executor = Executors.newFixedThreadPool(MAX_THEAD_COUNT);

    log.Debug("Starting logger with debug level of: " + log.getDebugLevel());
    log.Debug("Starting with a selected UDP port of: " + UDP_PORT);

    log.Log("System starting");
    try {
      // Set UDP socket
      tb.App.SOCKET = new DatagramSocket(UDP_PORT);

      // Begin with TCP client for peer onboarding
      TCPClient client = new TCPClient(IP, PORT, TEAMNAME);
      client.Start();

      // Start UDP, CLI, and Peer threads
      log.Debug("Starting thread for UDP client");
      executor.execute(new UDPClient());
      log.Debug("Starting thread for CLI client");
      executor.execute(new CLIClient());
      log.Debug("Starting thread for Peer client");
      executor.execute(new PeerClient());

      // Wait on threads to finish
      while (!STOP) System.out.print("");
      executor.shutdown();
      try {
        log.Log("Preparing for shutdown");
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException ie) {
        log.Warn("Error: App - main - InterruptedException on awaitTermination");
      }
      // Start the reporting TCP client
      log.Debug("Starting reporting TCP client");
      TCPClient endClient = new TCPClient(IP, PORT, TEAMNAME);
      endClient.Start();

      log.Log("System Shutting down.");

    } catch (SocketException se) {
      log.Warn("App - A socket exception occured");
    } catch (Exception e) {
      log.Warn("App - An exception occured");
    }
  }

  /* params: none
   *   Returns the set UDP_PORT
   * returns: int
   */
  public static int getUDPPort() {
    return UDP_PORT;
  }

  /* params: snip
   *   Adds a snip if the snip wasn't in already
   *   (Checks for identical snips, incl. its timestamp and
   *    everything else)
   * returns:void
   */
  public static void addSnip(Snip s) {
    if (!checkInSnips(s)) {
      SNIPS.add(s);
    }
  }

  /* params: string
   *   Add a string representing the sent peer request this peer sent
   * returns: void
   */
  public static void addToSentPeers(String s) {
    SENT_PEERS.add(s);
  }

  /* params: peer
   *   Add a peer to ALL_PEERS (non-unique) and checks to see if
   *   it is in the unique PEERs list and decides whether to add
   *   or replace it
   * returns: void
   */
  public static void addToPeers(Peer p) {
    ALL_PEERS.add(p);
    if (checkInPeers(p)) {
      replacePeer(p);
    } else {
      PEERS.add(p);
    }
  }

  /* params: peer
   *   Replaces a peer in the list
   * returns: void
   */
  private static void replacePeer(Peer p) {
    for (Peer q : PEERS) {
      if (q.getAddress().equals(p.getAddress()) && q.getPort() == p.getPort()) {
        PEERS.remove(q);
        PEERS.add(p);
        break;
      }
    }
  }

  /* params: peer
   *   Checks if the input peer address & port is already in the unique peer list
   * returns: boolean
   */
  private static boolean checkInPeers(Peer p) {
    for (Peer q : PEERS) {
      if (q.getAddress().equals(p.getAddress()) && q.getPort() == p.getPort()) {
        return true;
      }
    }
    return false;
  }

  /* params: snip
   *   Checks if a snip is in the snip list
   * returns: boolean
   */
  private static boolean checkInSnips(Snip s) {
    for (Snip t : SNIPS) {
      // If address are the same, ports are the same, content is
      // the same, and timestamps are the same: snip in list

      // Handles clients who may error and double send the same
      // snip to its peers
      if (s.getContent().equals(t.getContent())
          && s.getSourceAddress().equals(t.getSourceAddress())
          && s.getSourcePort() == t.getSourcePort()
          && s.getTimestamp() == t.getTimestamp()) {
        return true;
      }
    }
    return false;
  }

  /* params: none
   *   Return list of current (unique) peers
   * returns: CopyOnWriteArrayList<Peer>
   */
  public static CopyOnWriteArrayList<Peer> getPeerlist() {
    return PEERS;
  }

  /* params: none
   *   Returns the current snip timestamp
   * returns: int
   */
  public static int getSnipTimestamp() {
    return SNIP_TIMESTAMP;
  }

  /* params: int
   *   Sets the snip timestamp to a specific integer
   * returns: void
   */
  public static void setSnipTimestamp(int newts) {
    SNIP_TIMESTAMP = newts;
  }

  /* params: none
   *   Increments snip timestamp
   * returns: void
   */
  public static void incrementSnipTimestamp() {
    SNIP_TIMESTAMP++;
  }
}
