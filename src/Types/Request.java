public class Request {
	private String VERB;
	private String NOUN;

	public Request(String v, String n) {
		this.VERB = v;
		this.NOUN = n;
	}

	public void Handle() {
		switch (v) {
		case "snip":
			HandleRequest.HandleSnip(n);
			break;
		case "stop":
			HandleRequest.HandleStop(n);
			break;
		case "peer":
			HandleRequest.HandlePeer(n);
			break;
		}
	}
}

