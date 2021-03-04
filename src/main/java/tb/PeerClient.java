package tb;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import tb.types.Peer;

public class PeerClient implements Runnable {
  private boolean DONE;

  public PeerClient() {
    this.DONE = false;
  }

  @Override
  public void run() {
    tb.App.log.Log("Starting peer client");
    while (!Thread.currentThread().isInterrupted()) {
      try {
        sendPeerInfo();
        Thread.sleep(10000);
      } catch (InterruptedException ie) {
        tb.App.log.Warn("Error: PeerClient - run - InterruptedException occured");
      }
    }
  }

  private void sendPeerInfo() {
    tb.App.log.Log("Peer client sending peer information");
    for (Peer p : tb.App.PEERS) {
      for (Peer p2 : tb.App.PEERS) {
        try {
          InetAddress addr = InetAddress.getByName(p.getAddress());
          String p_info = "peer" + p.getAddress() + ":" + p.getPort();
          DatagramPacket pk =
              new DatagramPacket(p_info.getBytes(), p_info.getBytes().length, addr, p.getPort());
          tb.UDPClient.sendPacket(pk);
        } catch (UnknownHostException uh) {
          tb.App.log.Warn(
              "Error: PeerClient - sendPeerInfo - UnknownHostException occured: " + p.getAddress());
        }
      }
    }
  }

  public void done() {
    this.DONE = true;
  }
}
