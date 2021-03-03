import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPClient {
	// private static String IP;
	private static DatagramSocket SOCKET;
	private static final int POOL_SIZE = 50;
	private static final int MAX_PEERS = 200;
    private ExecutorService executor;
	private boolean DONE;

	public UDPClient() throws SocketException {
		this.SOCKET = new DatagramSocket(Main.UDP_PORT);
		this.DONE = false;
	}

	public void Start() throws IOException {
		Main.log.Log("Starting UDP client");
		executor = Executors.newFixedThreadPool(POOL_SIZE);
		createCommandLineThread();
		DatagramPacket pack = null;
		byte[] recv = new byte[65535];
		while (!this.DONE) {
			try {
			    pack = new DatagramPacket(recv, recv.length);
			    this.SOCKET.receive(pack);
			    String req_string = Helper.data(recv);
			    String[] split = req_string.split(" ");
			    Request req;
			    if (split.length > 1) {
			        req = new Request(split[0], split[1]);
		        }else {
			    	// req = new Request(split[0]);
			    	this.DONE = true;
			    }
			    executor.execute(new HandleRequest(split[0],split[1]));
			    recv = new byte[65535];
			} catch (SocketException se) {
				Main.log.Warn("A socket exception has occured.");
			} catch (IOException io) {
				Main.log.Warn("An IO exception has occured.");
			}
		}
	}

	private void createCommandLineThread() {
		Thread t = new Thread(new Runnable() {
		    @Override
			public void run() {
				Scanner keyboard = new Scanner(System.in);
				while (!getDone()) {
					String command = keyboard.nextLine().trim();
					// Main.log.Log("COMMAND RECIEVED: " + command);
					if (command.equalsIgnoreCase("snip")) {
						Main.log.Prompt("Enter a snip: ");
						String content = keyboard.nextLine().trim();
						Main.log.Log("SNIP RECV'D: " + content);
						// Send throughout the peers with the same DatagramSocket
					}
				}
			}
		});
		t.start();
	}

	public boolean getDone() {
		return this.DONE;
	}

	public void done() {
		this.DONE = true;
	}
}
