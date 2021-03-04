package tb;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import tb.logging.Logger;
import tb.types.Peer;

public class App {

  // static String IP = "localhost";
  // static final int PORT = 55921;
  static String IP = "34.212.129.196";
  static final int PORT = 44444;
  static final int UDP_PORT = 36636;
  static final String TEAMNAME = "Team Thunder Badger";

  static CopyOnWriteArrayList<Peer> PEERS = new CopyOnWriteArrayList<Peer>();
  static CopyOnWriteArrayList<Peer> ALL_PEERS = new CopyOnWriteArrayList<Peer>();
  static int SNIP_TIMESTAMP = 0; // For snip timings

  // Thread globals
  public static final int MAX_THEAD_COUNT = 20;
  private static ExecutorService executor;

  // Logging object
  public static Logger log;

  public static void main(String[] args) {
    executor = Executors.newFixedThreadPool(MAX_THEAD_COUNT);
    log = new Logger(2);
    log.Log("Starting logger with debug level " + log.getDebugLevel());

    try {
      // Begin with TCP client for peer onboarding
      TCPClient client = new TCPClient(IP, PORT, TEAMNAME);
      client.Start();

      // Begin UDP client for duration as peer
      executor.execute(new UDPClient());
      executor.execute(new PeerClient());

      executor.shutdown();
      try {
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
      } catch (InterruptedException ie) {
        log.Warn("Error: App - main - InterruptedException on awaitTermination");
      }

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
    boolean ret = false;
    for (Peer q : PEERS) {
      if (q.getAddress().equals(p.getAddress()) && q.getPort() == p.getPort()) {
        ret = true;
      }
    }
    return ret;
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
}
