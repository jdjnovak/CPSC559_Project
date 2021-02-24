import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

	static String IP = "localhost";
	static final int PORT = 55921;
	static final int UDP_PORT = 36636;
	static final String TEAMNAME = "Team Thunder Badger";

	// Thread globals
	public static final int MAX_THEAD_COUNT = 20;

	// Logging object
	public static Logger log;

	// Thread Pool Object - Not to be accesible outside this class
	private static ExecutorService executor;
	
	public static void main(String[] args) {
		executor = Executors.newFixedThreadPool(MAX_THEAD_COUNT);
		log = new Logger(2);
		log.Log("Starting logger with debug level " + log.getDebugLevel());

		// Begin with TCP client for peer onboarding
		TCPClient client = new TCPClient(IP, PORT, TEAMNAME);
		client.Start();
		UDPClient udpclient = new UDPClient();

		// Begin UDP client for duration as peer
		// Thread out necessary processes

		log.Log("Shutting down.");
	}

	public static int getUDPPort() {
		return UDP_PORT;
	}
}
