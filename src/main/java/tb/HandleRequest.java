package tb;

import java.net.*;
import tb.types.Peer;

public class HandleRequest {
  /*
  public void Handle() {
    tb.App.log.Log("HandleRequest - Running");
    if (this.VERB.equals("snip")) {
      HandleSnip(this.NOUN);
    } else if (this.VERB.equals("peer")) {
      HandlePeer(this.NOUN, );
    } else if (this.VERB.equals("stop")) {
      HandleStop();
    }
  }
  */

  public static void HandleSnip(String n) {
    tb.App.log.Log("SNIP: " + n);
    String[] snipSplit = n.split(" ");
    int currTS = tb.App.getSnipTimestamp();
    if (Integer.parseInt(snipSplit[0]) > currTS) {
      tb.App.setSnipTimestamp(Integer.parseInt(snipSplit[0]));
    }
  }

  public static void HandleStop() {
    tb.App.log.Log("STOP");
  }

  public static void HandlePeer(String n, String a, int p) {
    // tb.App.log.Log("PEER: " + n);
    String[] newPeer = n.split(":");
    Peer np =
        new Peer(newPeer[0], Integer.parseInt(newPeer[1]), a + ":" + p, Helper.getFormattedDate());
    tb.App.addToPeers(np);
  }
}
