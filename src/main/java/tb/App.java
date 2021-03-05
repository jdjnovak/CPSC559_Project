package tb;

import java.io.IOException;
import java.util.ArrayList;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import tb.logging.Logger;
import tb.types.Peer;
import tb.types.Snip;

public class App {

  // static String IP = "localhost";
  // static final int PORT = 55921;
  static String IP = "34.212.129.196";
  static final int PORT = 44444;
  // static final String IP = "136.159.5.22";
  // static final int PORT = 55921;
  static final String TEAMNAME = "Team Thunder Badger";

  static ArrayList<Peer> INITIAL_PEERS = new ArrayList<Peer>();
  static String INITIAL_PEERS_TIMESTAMP;

  static CopyOnWriteArrayList<Peer> PEERS = new CopyOnWriteArrayList<Peer>();
  static CopyOnWriteArrayList<Peer> ALL_PEERS = new CopyOnWriteArrayList<Peer>();
  static CopyOnWriteArrayList<Snip> SNIPS = new CopyOnWriteArrayList<Snip>();
  static int SNIP_TIMESTAMP = 0; // For snip timings

  // Thread globals
  public static final int MAX_THEAD_COUNT = 20;
  public static ExecutorService executor;

  // Major parameters
  public static Logger log;
  static int UDP_PORT;
  static int LOG_LEVEL;

  public static DatagramSocket SOCKET;

  public static boolean STOP;

  public static void start(int u, int l) throws SocketException {
    UDP_PORT = u;
    log = new Logger(l);
    executor = Executors.newFixedThreadPool(MAX_THEAD_COUNT);
    log.Log("Starting logger with debug level " + log.getDebugLevel());

    try {
	  tb.App.SOCKET = new DatagramSocket(UDP_PORT);
      // Begin with TCP client for peer onboarding
      TCPClient client = new TCPClient(IP, PORT, TEAMNAME);
      client.Start();

      // Start UDP thread and Peer thread
      executor.execute(new UDPClient());
      executor.execute(new CLIClient());
      executor.execute(new PeerClient());

      while (!STOP) System.out.print("");
      executor.shutdown();
      try {
        log.Log("Before awaitTerminate");
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
        log.Log("After shutdown");
      } catch (InterruptedException ie) {
        log.Warn("Error: App - main - InterruptedException on awaitTermination");
      }
      log.Log("Before End Client");

      TCPClient endClient = new TCPClient(IP, PORT, TEAMNAME);
      endClient.Start();

      log.Log("Shutting down.");

    // } catch (IOException io) {
      // log.Warn("App - An IO exception occured");
	} catch (SocketException se) {
      log.Warn("App - A socket exception occured");
    } catch (Exception e) {
      log.Warn("App - An exception occured");
    }
  }

  public static int getUDPPort() {
    return UDP_PORT;
  }

  public static void addSnip(Snip s) {
    if (!checkInSnips(s)) {
      SNIPS.add(s);
    }
  }

  public static void addToPeers(Peer p) {
    ALL_PEERS.add(p);
    if (checkInPeers(p)) {
      replacePeer(p);
    } else {
      PEERS.add(p);
    }
  }

  private static void replacePeer(Peer p) {
    for (Peer q : PEERS) {
      if (q.getAddress().equals(p.getAddress()) && q.getPort() == p.getPort()) {
        PEERS.remove(q);
        PEERS.add(p);
        break;
      }
    }
  }

  private static boolean checkInPeers(Peer p) {
    for (Peer q : PEERS) {
      if (q.getAddress().equals(p.getAddress()) && q.getPort() == p.getPort()) {
        return true;
      }
    }
    return false;
  }

  private static boolean checkInSnips(Snip s) {
    for (Snip t : SNIPS) {
      if (s.getContent().equals(t.getContent())
          && s.getSourceAddress().equals(t.getSourceAddress())
          && s.getSourcePort() == t.getSourcePort()
          && s.getTimestamp() == t.getTimestamp()) {
        return true;
      }
    }
    return false;
  }

  public static CopyOnWriteArrayList<Peer> getPeerlist() {
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

  public static void initShutdown() {}
}
