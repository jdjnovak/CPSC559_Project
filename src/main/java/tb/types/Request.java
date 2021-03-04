package tb.types;

public class Request implements Runnable {
  private String VERB;
  private String NOUN;

  // Single parameter constructor for 'stop'
  public Request(String v) {
    this.VERB = v;
    this.NOUN = "";
  }

  // Double parameter constructor for 'snip' and 'peer'
  public Request(String v, String n) {
    this.VERB = v;
    this.NOUN = n;
  }

  @Override
  public void run() {
    tb.App.log.Log("Request recv'd: " + this.VERB + " " + this.NOUN);
    switch (this.VERB) {
      case "snip":
        tb.HandleRequest.HandleSnip(this.NOUN);
        break;
      case "stop":
        tb.HandleRequest.HandleStop();
        break;
      case "peer":
        tb.HandleRequest.HandlePeer(this.NOUN);
        break;
    }
  }
}
