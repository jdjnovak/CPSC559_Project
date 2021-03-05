package tb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import tb.types.Request;

public class UDPClient implements Runnable {

  public UDPClient() throws SocketException {}

  @Override
  public void run() {
    try {
      tb.App.log.Log("Starting UDP client");
      byte[] recv = new byte[1024];
      while (!tb.App.STOP) {
        DatagramPacket pack = new DatagramPacket(recv, recv.length);
        tb.App.SOCKET.receive(pack);

        String req_string = Helper.data(recv);
        tb.App.log.Debug(
            "REQUEST: "
                + req_string
                + "\tFROM: "
                + pack.getAddress().toString()
                + ":"
                + pack.getPort());
        String[] parsed = parsePacket(req_string);
        if (req_string.startsWith("stop")) break;
        tb.App.executor.execute(
            new Request(
                parsed[0], parsed[1], pack.getAddress().toString().split("/")[1], pack.getPort()));
        recv = new byte[1024];
      }
    } catch (SocketException se) {
      tb.App.log.Warn("A socket exception has occured.");
    } catch (IOException io) {
      tb.App.log.Warn("An IO exception has occured.");
    }
    HandleRequest.HandleStop();
    tb.App.log.Log("Shutting down UDP Client");
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
      parsed[0] = "stop";
      parsed[1] = "";
    }
    return parsed;
  }

  public static void sendPacket(DatagramPacket pk) {
    try {
      tb.App.log.Debug("Sending packet : " + pk.getAddress().toString() + ":" + pk.getPort());
      tb.App.SOCKET.send(pk);
    } catch (IOException io) {
      tb.App.log.Warn("Error: UDPClient - sendPacket - IOException occured");
    }
  }
}
