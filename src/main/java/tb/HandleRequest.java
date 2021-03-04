package tb;

import java.net.*;

public class HandleRequest {
  private String VERB;
  private String NOUN;

  public HandleRequest(String v, String n) {
    this.VERB = v;
    this.NOUN = n;
  }

  public void Handle() {
    tb.App.log.Log("HandleRequest - Running");
    if (this.VERB.equals("snip")) {
      HandleSnip(this.NOUN);
    } else if (this.VERB.equals("peer")) {
      HandlePeer(this.NOUN);
    } else if (this.VERB.equals("stop")) {
      HandleStop();
    }
  }

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

  public static void HandlePeer(String n) {
    tb.App.log.Log("PEER: " + n);
  }
}
