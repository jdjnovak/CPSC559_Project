import java.net.*;

public class HandleRequest implements Runnable {
	private String VERB;
	private String NOUN;

	public HandleRequest(String v, String n) {
		this.VERB = v;
		this.NOUN = n;
	}

	@Override
	public void run() {
		if (this.VERB.equals("snip")) {
			Main.log.Log("SNIP");
		} else if (this.VERB.equals("peer")) {
			Main.log.Log("PEER");
		} else if (this.VERB.equals("stop")) {
			Main.log.Log("STOP");
		}
	}

    public static void HandleSnip() {
		System.out.println("SNIP");
	}

	public static void HandleStop() {
		System.out.println("STOP");
	}

	public static void HandlePeer() {
		System.out.println("PEER");
	}
}
