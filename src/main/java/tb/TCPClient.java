package tb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import tb.types.Peer;
import tb.types.Snip;

/*
 Client.java
 - Class to handle creating a socket connection with the registry
   and to call the request/response handlers
*/
public class TCPClient {

  private static String SERVER_IP;
  private static int SERVER_PORT;
  private static String TEAMNAME;

  /*
     Default Constructor
       - Default constructor looks for a registry running on localhost
  */
  public TCPClient() {
    tb.TCPClient.SERVER_IP = "localhost";
    tb.TCPClient.SERVER_PORT = 55921;
    tb.TCPClient.TEAMNAME = "Default";
  }

  /*
    Parametered Constructor
      - Specifies the IP, PORT, and Teamname for the client
  */
  public TCPClient(String ip, int port, String team) {
    tb.TCPClient.SERVER_IP = ip;
    tb.TCPClient.SERVER_PORT = port;
    tb.TCPClient.TEAMNAME = team;
  }

  /* params: none
      start a socket and connect to the registry
    returns: void
  */
  public void Start() {
    tb.App.log.Log("Starting TCP client");
    try {
      // Connect to the registry and notify user on CLI
      Socket socket = new Socket(SERVER_IP, SERVER_PORT);
      tb.App.log.Log("Connected to " + SERVER_IP.toString());

      // Create a PrintStream on the output stream from the socket
      PrintStream writer = new PrintStream(socket.getOutputStream());

      // Create a BufferedReader on the input stream from the socket
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      // Receive the first line and keep receiving lines until registry sends close
      String receivedLine = reader.readLine();
      while (!receivedLine.equals("close")) {
        tb.App.log.Log("Received: " + receivedLine); // print out request on client side
        if (receivedLine.startsWith("get")) { // if server requests info,
          String response = generateResponse(receivedLine); // generate appropriate response
          tb.App.log.Log("Sending:\n" + response); // with helper function
          writer.print(response); // send response to server
        } else { // else server will send peer list
          int numPeers =
              Integer.parseInt(reader.readLine().trim()); // record number of peers received
          tb.App.log.Log("Received: Number of peers: " + numPeers);
          tb.App.INITIAL_PEERS_TIMESTAMP = Helper.getFormattedDate();
          for (int peer = 0; peer < numPeers; peer++) { // for each line received from server,
            String peerLine = reader.readLine(); // read the next line
            tb.App.log.Log("Received: Peer is " + peerLine); // print out on client side
            String[] ipPort = peerLine.trim().split(":");
            Peer p =
                new Peer(
                    ipPort[0],
                    Integer.parseInt(ipPort[1]),
                    SERVER_IP + ":" + SERVER_PORT,
                    Helper.getFormattedDate());
            tb.App.INITIAL_PEERS.add(p);
            tb.App.addToPeers(p);
          }
        }
        // Read next line from registry
        receivedLine = reader.readLine();
      }
      // Close the IO streams and the socket
      writer.close();
      reader.close();
      socket.close();
    } catch (Exception e) {
      // Catch all exceptions
      tb.App.log.Warn("Error: " + e.getStackTrace()[0]);
    }
  }

  /* params: String
   *   Generates a response based on the given request
   * returns: String
   */
  public static String generateResponse(String recv) {
    if (recv.equals("get team name")) { // if server requests team name,
      return TEAMNAME + "\n"; // return team name followed by a newline character
    } else if (recv.equals("get code")) { // if server requests code
      return "Java\n"
          + Helper.printAllFiles("src/main/java/tb/")
          + "\n...\n"; // navigate to source code folder and get file contents
    } else if (recv.equals("get report")) { // if serer requests a report
      int numberOfPeers = tb.App.PEERS.size(); // return string formatted as server expects
      if (numberOfPeers == 0)
        return "0\n0\n"; // if there are no peers, return 0 peers and 0 sources
      return numberOfPeers
          + "\n" // # of peers
          + getPeers() // recorded peer info
          + "1\n"
          + tb.App.IP
          + ":"
          + tb.App.PORT
          + "\n" // # of sources
          + tb.App.INITIAL_PEERS_TIMESTAMP
          + "\n"
          + tb.App.INITIAL_PEERS.size()
          + "\n"
          + getInitialPeers()
          + tb.App.ALL_PEERS.size()
          + "\n"
          + getRecvPeers()
		  + tb.App.SENT_PEERS.size()
		  + getSentPeers()
          + tb.App.SNIPS.size()
          + "\n"
          + getSnippets();
    } else if (recv.equals("get location")) {
	  tb.App.log.Warn("SENDING PORT: " + tb.App.UDP_PORT);
      return Helper.getPublicIP() + ":" + tb.App.UDP_PORT + "\n";
    }

    return "ERROR: Unrecognized code"; // return error string is request not recognized
  }

  /* params: none
   *   Returns all cuurent peers, <IP>:<PORT>, separated by newlines
   * returns: String
   */
  public static String getPeers() {
    String returnString = ""; // initialize string to return
    for (Peer p : tb.App.PEERS) { // for each peer in the PEERS list
      returnString +=
          p.getAddress() + ":" + p.getPort() + "\n"; // append the peer info to returnString
    }
    // If there are no peers, return only a new line character
    return (returnString.equals("")) ? "\n" : returnString;
  }

  /* params: none
   *   Returns all received peers, <IP>:<PORT>, separated by newlines
   * returns: String
   */
  public static String getRecvPeers() {
    String returnString = ""; // initialize string to return
    for (Peer p : tb.App.ALL_PEERS) { // for each peer in the PEERS list
      returnString += p.toString() + "\n";
    }
    // If there are no peers, return only a new line character
    return (returnString.equals("")) ? "\n" : returnString;
  }

  /* params: none
   *   Returns the initial peers, <IP>:<PORT>, separated by newlines
   * returns: String
   */
  public static String getInitialPeers() {
    String returnString = ""; // initialize string to return
    for (Peer p : tb.App.INITIAL_PEERS) { // for each peer in the PEERS list
      returnString +=
          p.getAddress() + ":" + p.getPort() + "\n"; // append the peer info to returnString
    }
    // If there are no peers, return only a new line character
    return (returnString.equals("")) ? "\n" : returnString;
  }

  /* params: none
   *   Returns the list of all received snippets
   * returns: String
   */
  public static String getSnippets() {
    String returnString = ""; // initialize string to return
	for (int i = 0; i < tb.App.SNIPS.size(); i++) {
	  String end = "\n";
	  if (i == tb.App.SNIPS.size() - 1) end = "";
      if (!s.getSourceAddress().equals("127.0.0.1")) {
	    returnString += s.toString() + end;
	  } else {
		returnString += s.getTimestamp() + " " + s.getContent() + " " + Helper.getPublicIP() + ":" + tb.App.UDP_PORT + end;
	  }
    }
    // If there are no peers, return only a new line character
    return (returnString.equals("")) ? "\n" : returnString;
  }
   
  /* params: none
   *   Returns the peers sent via PeerClient, <IP>:<PORT>, separated by newlines
   * returns: String
   */
  public static String getSentPeers() {
    String returnString = ""; // initialize string to return
    for (String s : tb.App.SENT_PEERS) { // for each peer in the PEERS list
      returnString += s;
    }
    // If there are no peers, return only a new line character
    return (returnString.equals("")) ? "\n" : returnString;
  }
}
