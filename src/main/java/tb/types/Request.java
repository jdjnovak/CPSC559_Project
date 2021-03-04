package tb.types;

public class Request {
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

  /*
  public void Handle() {
  	switch (this.VERB) {
  	case "snip":
  		HandleRequest.HandleSnip(this.NOUN);
  		break;
  	case "stop":
  		HandleRequest.HandleStop();
  		break;
  	case "peer":
  		HandleRequest.HandlePeer(this.NOUN);
  		break;
  	}
  }
  */
}
