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
    tb.App.log.Log("Sending Peer Information");
    int pos = (int) (Math.random() * tb.App.PEERS.size());
    for (Peer p : tb.App.PEERS) {
      try {
        InetAddress addr = InetAddress.getByName(p.getAddress());
        String p_info =
            "peer" + tb.App.PEERS.get(pos).getAddress() + ":" + tb.App.PEERS.get(pos).getPort();
        DatagramPacket pk =
            new DatagramPacket(p_info.getBytes(), p_info.getBytes().length, addr, p.getPort());
        tb.UDPClient.sendPacket(pk);
      } catch (UnknownHostException uh) {
        tb.App.log.Warn(
            "Error: PeerClient - sendPeerInfo - UnknownHostException occured: " + p.getAddress());
      }
    }
  }

  public void done() {
    this.DONE = true;
  }
}
