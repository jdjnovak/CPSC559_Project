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
import tb.types.Request;
import tb.types.Snip;

public class UDPClient implements Runnable {
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

  @Override
  public void run() {
    tb.App.log.Log("Starting UDP client");
    executor = Executors.newFixedThreadPool(POOL_SIZE);
    createCommandLineThread();
    DatagramPacket pack = null;
    byte[] recv = new byte[65535];
    while (!this.DONE) {
      try {
        pack = new DatagramPacket(recv, recv.length);
        tb.UDPClient.SOCKET.receive(pack);
        tb.App.log.Debug("Packet from " + pack.getAddress().toString());

        String req_string = Helper.data(recv);
        String[] parsed = parsePacket(req_string);
        executor.execute(
            new Request(
                parsed[0], parsed[1], pack.getAddress().toString().split("/")[1], pack.getPort()));
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
    } else if (pck.startsWith("peer")) {
      parsed[0] = "peer";
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

                    tb.App.incrementSnipTimestamp();
                    tb.App.log.Debug("Timestamp incremented to: " + tb.App.getSnipTimestamp());

                    String total_content = "snip" + tb.App.getSnipTimestamp() + " " + content;
                    byte[] buf = total_content.getBytes();

                    tb.App.log.Log("Sending snip to peers: " + content);
                    // Send throughout the peers with the same DatagramSocket
                    for (Peer p : tb.App.PEERS) {
                      try {
                        InetAddress peerIp = InetAddress.getByName(p.getAddress());
                        DatagramPacket pack =
                            new DatagramPacket(buf, buf.length, peerIp, p.getPort());
                        tb.UDPClient.SOCKET.send(pack);
                      } catch (UnknownHostException uh) {
                        tb.App.log.Warn(
                            "Error: createCommandLineThread() - Sending snip - unkown host: "
                                + p.getAddress());
                      } catch (IOException io) {
                        tb.App.log.Warn(
                            "Error: createCommandLineThread() - IO Exception while sending snip");
                      }
                    }
                  } else if (command.equalsIgnoreCase("gimme")) {
                    tb.App.log.Debug("Current Peers:");
                    for (Peer p : tb.App.PEERS) {
                      tb.App.log.Debug(p.toString());
                    }
                  } else if (command.equalsIgnoreCase("snops")) {
                    tb.App.log.Debug("Current Snips:");
                    for (Snip s : tb.App.SNIPS) {
                      tb.App.log.Debug(s.toString());
                    }
                  }
                }
              }
            });
    t.start();
  }

  public static void sendPacket(DatagramPacket pk) {
    try {
      SOCKET.send(pk);
    } catch (IOException io) {
      tb.App.log.Warn("Error: UDPClient - sendPacket - IOException occured");
    }
  }

  public boolean getDone() {
    return this.DONE;
  }

  public void done() {
    this.DONE = true;
  }
}
