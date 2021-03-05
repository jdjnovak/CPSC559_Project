package tb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import tb.types.Peer;
import tb.types.Snip;

public class CLIClient implements Runnable {
  public CLIClient() {}

  @Override
  public void run() {
    Scanner keyboard = new Scanner(System.in);
    while (!tb.App.STOP) {
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
            InetAddress peerIp;
            if (Helper.getPublicIP().equals(p.getAddress())) {
              peerIp = InetAddress.getByName("localhost");
            } else {
              peerIp = InetAddress.getByName(p.getAddress());
            }
            DatagramPacket pack = new DatagramPacket(buf, buf.length, peerIp, p.getPort());
            tb.App.SOCKET.send(pack);
          } catch (UnknownHostException uh) {
            tb.App.log.Warn(
                "Error: createCommandLineThread() - Sending snip - unkown host: " + p.getAddress());
          } catch (IOException io) {
            tb.App.log.Warn("Error: createCommandLineThread() - IO Exception while sending snip");
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
    tb.App.log.Log("Shutting down CLI");
  }
}
