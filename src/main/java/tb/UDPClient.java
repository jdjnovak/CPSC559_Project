package tb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import tb.types.Peer;

public class UDPClient {
  // private static String IP;
  private static DatagramSocket SOCKET;
  private static final int POOL_SIZE = 50;
  private static final int MAX_PEERS = 200;
  private ExecutorService executor;
  private boolean DONE;

  public UDPClient() throws SocketException {
    tb.UDPClient.SOCKET = new DatagramSocket(tb.App.UDP_PORT);
    this.DONE = false;
  }

  public void Start() throws IOException {
    tb.App.log.Log("Starting UDP client");
    executor = Executors.newFixedThreadPool(POOL_SIZE);
    createCommandLineThread();
    DatagramPacket pack = null;
    byte[] recv = new byte[65535];
    while (!this.DONE) {
      try {
        pack = new DatagramPacket(recv, recv.length);
        tb.UDPClient.SOCKET.receive(pack);

        String req_string = Helper.data(recv);
        String[] parsed = parsePacket(req_string);
        // String[] split = req_string.split(" ");
        Request req;
        /*
              if (split.length > 1) {
                req = new Request(split[0], split[1]);
              } else {
                // req = new Request(split[0]);
                this.DONE = true;
              }
        */
        // executor.execute(new HandleRequest(split[0], split[1]));
        executor.execute(new HandleRequest(parsed[0], parsed[1]));
        recv = new byte[65535];
      } catch (SocketException se) {
        tb.App.log.Warn("A socket exception has occured.");
      } catch (IOException io) {
        tb.App.log.Warn("An IO exception has occured.");
      }
    }
  }

  private String[] parsePacket(String pck) {
    String[] parsed = new String[2];
    if (pck.startsWith("snip")) {
      parsed[0] = "snip";
      parsed[1] = pck.substring(4);
    } else {
      parsed[0] = "";
      parsed[1] = "";
    }
    return parsed;
  }

  private void createCommandLineThread() {
    Thread t =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                Scanner keyboard = new Scanner(System.in);
                while (!getDone()) {
                  String command = keyboard.nextLine().trim();
                  if (command.equalsIgnoreCase("snip")) {
                    tb.App.log.Prompt("Enter a snip: ");
                    String content = keyboard.nextLine().trim();
                    // Send throughout the peers with the same DatagramSocket
                    byte[] buf = content.getBytes();
                    for (Peer p : tb.App.PEERS) {
                      try {
                        InetAddress peerIp = InetAddress.getByName(p.getAddress());
                        DatagramPacket pack =
                            new DatagramPacket(buf, buf.length, peerIp, p.getPort());
                        tb.App.log.Log("Sending snip to peers: " + content);
                        tb.UDPClient.SOCKET.send(pack);
                        tb.App.incrementSnipTimestamp();
                        tb.App.log.Log("Timestamp incremented to: " + tb.App.getSnipTimestamp());
                      } catch (UnknownHostException uh) {
                        tb.App.log.Warn(
                            "Error: createCommandLineThread() - Sending snip - unkown host: "
                                + p.getAddress());
                      } catch (IOException io) {
                        tb.App.log.Warn(
                            "Error: createCommandLineThread() - IO Exception while sending snip");
                      }
                    }
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
