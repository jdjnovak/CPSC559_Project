package tb;

import java.net.DatagramPacket;
import tb.types.Peer;

public class PeerClient implements Runnable {
  private boolean DONE;

  public PeerClient() {
    this.DONE = false;
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {}
  }

  private void sendPeerInfo() {
    DatagramPacket pk;
    String content = "";
    for (Peer p : tb.App.PEERS) {}
  }

  public void done() {
    this.DONE = true;
  }
}
