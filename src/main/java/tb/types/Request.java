package tb.types;

public class Request implements Runnable {
  private String VERB;
  private String NOUN;
  private String ADDR;
  private int PORT;

  // Single parameter constructor for 'stop'
  public Request(String v, String a, int p) {
    this.VERB = v;
    this.NOUN = "";
    this.PORT = p;
  }

  // Double parameter constructor for 'snip' and 'peer'
  public Request(String v, String n, String a, int p) {
    this.VERB = v;
    this.NOUN = n;
    this.ADDR = a;
    this.PORT = p;
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
        tb.HandleRequest.HandlePeer(this.NOUN, this.ADDR, this.PORT);
        break;
    }
  }
}
