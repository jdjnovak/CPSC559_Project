package tb;

import java.net.*;
import tb.types.Peer;
import tb.types.Snip;

public class HandleRequest {
  public static void HandleSnip(String n, String a, int p) {
    String[] snipSplit = n.split(" ");
    int currTS = tb.App.getSnipTimestamp();
    if (Integer.parseInt(snipSplit[0]) > currTS) {
      tb.App.setSnipTimestamp(Integer.parseInt(snipSplit[0]));
    }
    Snip newSnip =
        new Snip(n.substring(snipSplit[0].length()).trim(), a, p, Integer.parseInt(snipSplit[0]));
    tb.App.addSnip(newSnip);
  }

  public static void HandleStop() {
    tb.App.STOP = true;
  }

  public static void HandlePeer(String n, String a, int p) {
    String[] newPeer = n.trim().split(":");
    Peer np =
        new Peer(newPeer[0], Integer.parseInt(newPeer[1]), a + ":" + p, Helper.getFormattedDate());
    if (np.getAddress().equals(Helper.getPublicIP()) && np.getPort() != tb.App.UDP_PORT) {
      // tb.App.log.Warn("Rogue peer: " + np.getAddress() + ":" + np.getPort());
	  tb.App.log.Warn("Rogue peer: " + np.toString());
	  return;
	}
    tb.App.addToPeers(np);
  }
}
