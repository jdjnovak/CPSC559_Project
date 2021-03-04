package tb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import tb.types.Peer;

/*
 Client.java
 - Class to handle creating a socket connection with the registry
   and to call the request/response handlers
*/
public class TCPClient {

  private static String SERVER_IP;
  private static int SERVER_PORT;
  private static String TEAMNAME;
  private static ArrayList<ArrayList<String>> PEERS_LIST;
  private static ArrayList<String> PEERS_SOURCES;
  private static ArrayList<String> PEERS_TIMESTAMPS;
  private static String PEER_UPDATED_TIMESTAMP;

  /*
     Default Constructor
       - Default constructor looks for a registry running on localhost
  */
  public TCPClient() {
    tb.TCPClient.SERVER_IP = "localhost";
    tb.TCPClient.SERVER_PORT = 55921;
    tb.TCPClient.TEAMNAME = "Default";
    tb.TCPClient.PEERS_LIST = new ArrayList<ArrayList<String>>();
    tb.TCPClient.PEERS_SOURCES = new ArrayList<String>();
    tb.TCPClient.PEERS_TIMESTAMPS = new ArrayList<String>();
    tb.TCPClient.PEER_UPDATED_TIMESTAMP = "";
  }

  /*
    Parametered Constructor
      - Specifies the IP, PORT, and Teamname for the client
  */
  public TCPClient(String ip, int port, String team) {
    tb.TCPClient.SERVER_IP = ip;
    tb.TCPClient.SERVER_PORT = port;
    tb.TCPClient.TEAMNAME = team;
    tb.TCPClient.PEERS_LIST = new ArrayList<ArrayList<String>>();
    tb.TCPClient.PEERS_SOURCES = new ArrayList<String>();
    tb.TCPClient.PEERS_TIMESTAMPS = new ArrayList<String>();
    tb.TCPClient.PEER_UPDATED_TIMESTAMP = "";
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
          ArrayList<String> plist = new ArrayList<String>();
          for (int peer = 0; peer < numPeers; peer++) { // for each line received from server,
            String peerLine = reader.readLine(); // read the next line
            tb.App.log.Log("Received: Peer is " + peerLine); // print out on client side
            if (plist.contains(peerLine)) {
              tb.App.log.Log("Peer " + peerLine + " already in peer list.");
            } else {
              // plist.add(peerLine); // add peer info to list
              String[] ipPort = peerLine.trim().split(":");
              Peer p =
                  new Peer(
                      ipPort[0],
                      Integer.parseInt(ipPort[1]),
                      SERVER_IP + ":" + SERVER_PORT,
                      Helper.getFormattedDate());
              tb.App.addToPeers(p);
            }
          }
          tb.TCPClient.PEER_UPDATED_TIMESTAMP = Helper.getFormattedDate();
          addPeerList(plist, SERVER_IP + ":" + SERVER_PORT, Helper.getFormattedDate());
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

  /* params: ArrayList<String>, String, String
       Adds a peerlist to the peers list, a source IP to peers sources
       and a string date to peers timestamps
     returns: void
  */
  public static void addPeerList(ArrayList<String> lst, String source, String timestamp) {
    PEERS_LIST.add(lst);
    PEERS_SOURCES.add(source);
    PEERS_TIMESTAMPS.add(timestamp);
  }

  /*  params: recv:String - the message sent by server
              possible values: 'get team name','get code','get report'
                  - any other value will return an error string
      Helper function to generate and format a reply for server requests.
      returns: formatted string based on request
  */
  public static String generateResponse(String recv) {
    if (recv.equals("get team name")) { // if server requests team name,
      return TEAMNAME + "\n"; // return team name followed by a newline character
    } else if (recv.equals("get code")) { // if server requests code
      // Tb.TCPClient line will need to be adjusted based on the files and
      // how the project structure is defined
      return "Java\n"
          + Helper.printAllFiles("src/main/java/tb/")
          + "\n...\n"; // navigate to source code folder and get file contents
    } else if (recv.equals("get report")) { // if serer requests a report
      int numberOfPeers = countPeers(); // return string formatted as server expects
      if (numberOfPeers == 0)
        return "0\n0\n"; // if there are no peers, return 0 peers and 0 sources
      return numberOfPeers
          + "\n" // # of peers
          + getPeers() // recorded peer info
          + PEERS_SOURCES.size()
          + "\n" // # of sources
          + getPeersAndTimes(); // Print peer amount in each source followed by their peers and
      // timestamp
    } else if (recv.equals("get location")) {
      return Helper.getPublicIP() + ":" + tb.App.UDP_PORT + "\n";
    }

    return "ERROR: Unrecognized code"; // return error string is request not recognized
  }

  /* params: none
       Get the formatted string for each peer list and their timestamp
     returns: String
  */
  public static String getPeersAndTimes() {
    String returnString = "";
    // Loop through all peer lists received
    for (int index = 0; index < PEERS_LIST.size(); index++) {
      returnString += PEERS_SOURCES.get(index) + "\n";
      returnString += PEERS_TIMESTAMPS.get(index) + "\n";
      returnString += PEERS_LIST.get(index).size() + "\n";

      // Loop through each peer in each list adding it to the string
      for (String peer : PEERS_LIST.get(index)) {
        returnString += peer + "\n";
      }
    }
    return returnString;
  }

  /* params: none
       returns latest peer update time
     returns: string
  */
  public static String getLatestPeerTime() {
    return PEER_UPDATED_TIMESTAMP;
  }

  /* params: none
       Get the total list of unique peers
     returns: int
  */
  public static int countPeers() {
    ArrayList<String> allPeers = new ArrayList<String>();
    for (ArrayList<String> lst : PEERS_LIST) {
      for (String peer : lst) {
        // Check if current peer is in the local list of all peers
        if (!allPeers.contains(peer)) allPeers.add(peer);
      }
    }
    return allPeers.size();
  }

  /*  params: none
        Uses PEERS arraylist to create a String of recorded peers.
        Between each set of peer details, we insert a newline character.
      returns: a string of all peer details, each on their own line
  */
  public static String getPeers() {
    String returnString = ""; // initialize string to return
    ArrayList<String> lst = new ArrayList<String>();
    for (ArrayList<String> peerlist : PEERS_LIST) {
      for (String peer : peerlist) { // for each peer in the PEERS list
        if (!lst.contains(peer)) {
          lst.add(peer);
          returnString = returnString + peer + "\n"; // append the peer info to returnString
        }
      }
    }
    // If there are no peers, return only a new line character
    return (returnString.equals("")) ? "\n" : returnString;
  }
}
