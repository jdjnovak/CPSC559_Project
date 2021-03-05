package tb;

import java.net.*;
import tb.types.Peer;
import tb.types.Snip;

/*
 * The class which is delegated to handle received requests from the UDP client
 */
public class HandleRequest {
  /* params: string, string, int
   *   Handles a snip given its content and source IP and port
   * returns: void
   */
  public static void HandleSnip(String n, String a, int p) {
    // Split snip by the space to get the timestamp
    String[] snipSplit = n.split(" ");

    // Get the current timestamp
    int currTS = tb.App.getSnipTimestamp();

    // If given timestamp is greater, replace own timestamp with it
    if (Integer.parseInt(snipSplit[0]) > currTS) {
      tb.App.setSnipTimestamp(Integer.parseInt(snipSplit[0]));
    }

    Peer np = new Peer(a, p, a + ":" + p, Helper.getFormattedDate());
    tb.App.ALL_PEERS.add(np);

    // Create new snip and add it to list
    Snip newSnip =
        new Snip(n.substring(snipSplit[0].length()).trim(), a, p, Integer.parseInt(snipSplit[0]));
    tb.App.addSnip(newSnip);
  }

  /* params: none
   *   Sets the global stop variable to true
   * returns: void
   */
  public static void HandleStop() {
    tb.App.STOP = true;
  }

  /* params: string, string, int
   *   Handles a peer request from UDP client
   * returns: void
   */
  public static void HandlePeer(String n, String a, int p) {
    // Split peer by its colon
    try {
      String[] newPeer = n.trim().split(":");

      if (newPeer[0].split(".")[0].equals("192") && newPeer[0].split(".")[1].equals("168")) {
        return;
      }

      // Create new peer
      Peer np =
          new Peer(
              newPeer[0], Integer.parseInt(newPeer[1]), a + ":" + p, Helper.getFormattedDate());

      // Add new peer to peers
      tb.App.addToPeers(np);
    } catch (ArrayIndexOutOfBoundsException ai) {
      tb.App.log.Warn("Illegal peer sent by: " + a + ":" + p + "--> " + n);
    }
  }
}
