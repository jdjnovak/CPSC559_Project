package tb.types;

/*
 * Request class for any received request in the
 * UDP client
 */
public class Request implements Runnable {
  private String VERB;
  private String NOUN;
  private String ADDR;
  private int PORT;

  /* params: string, string, int
   *   Constructor for Request with three parameters
   * returns: Request
   */
  public Request(String v, String a, int p) {
    this.VERB = v;
    this.NOUN = "";
    this.ADDR = a;
    this.PORT = p;
  }

  /* params: string, string, string, int
   *   Constructor for Request with four parameters
   * returns: Request
   */
  public Request(String v, String n, String a, int p) {
    this.VERB = v;
    this.NOUN = n;
    this.ADDR = a;
    this.PORT = p;
  }

  /* params: none
   *   Overridden run function for runnable class
   * returns: void
   */
  @Override
  public void run() {
    tb.App.log.Debug("Request received: " + this.VERB + " " + this.NOUN);
    // Handles the Request based on the verb given (i.e., stop, snip, peer, etc)
    switch (this.VERB) {
      case "snip":
        tb.HandleRequest.HandleSnip(this.NOUN, this.ADDR, this.PORT);
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
