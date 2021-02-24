import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
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
		this.SOCKET = new DatagramSocket(0);
		this.DONE = false;
	}

	public void start() throws IOException {
		executor = Executors.newFixedThreadPool(POOL_SIZE);
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
			}
		}
	}

	public void done() {
		this.DONE = true;
	}
}
