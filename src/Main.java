import java.concurrent.Executors;
import java.concurrent.ExecutorService;
import java.concurrent.ConcurrentHashMap;

public class Main {

	static String IP = "localhost";
	static final int PORT = 55901;
	static final String TEAMNAME = "Team Thunder Badger";

	// Thread globals
	public static final int MAX_THEAD_COUNT = 20;

	// Logging object
	public static Logger log;

	// Thread Pool Object - Not to be accesible outside this class
	private ExecutorService executor;
	
	public static void main(String[] args) {
		executor = Executors.newFixedThreadPool(MAX_THEAD_COUNT);
		log = new Logger(2);
		log.Log("Starting logger with debug level " + log.getDebugLevel());

		// Begin with TCP client for peer onboarding
		TCPClient client = new TCPClient(IP, PORT, TEAMNAME);
		client.Start();

		// Begin UDP client for duration as peer
		// Thread out necessary processes

		log.Log("Shutting down.");
	}
}
