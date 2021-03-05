package tb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import tb.types.Peer;

public class PeerClient implements Runnable {

  public PeerClient() {}

  @Override
  public void run() {
    tb.App.log.Log("Starting peer client");
    while (!tb.App.STOP) {
      try {
        sendPeerInfo();
        Thread.sleep(30000);
      } catch (InterruptedException ie) {
        tb.App.log.Log("Error: PeerClient - run - Interrupt occured");
      }
    }
    tb.App.log.Log("Shutting down Peer Client");
  }

  private void sendPeerInfo() {
    tb.App.log.Log("Sending Peer Information");
    int pos = (int) (Math.random() * tb.App.PEERS.size());
    for (Peer p : tb.App.PEERS) {
      if (p.getAddress().equals(Helper.getPublicIP())) continue; // Don't send peers to self
      try {
        InetAddress addr = InetAddress.getByName(p.getAddress());
        String p_info =
            "peer" + tb.App.PEERS.get(pos).getAddress() + ":" + tb.App.PEERS.get(pos).getPort();
        DatagramPacket pk =
            new DatagramPacket(p_info.getBytes(), p_info.getBytes().length, addr, p.getPort());
        tb.App.log.Debug(
            "Sending: "
                + tb.App.SOCKET.getLocalPort()
                + "\tto : "
                + pk.getAddress().toString()
                + ":"
                + pk.getPort());
        tb.App.SOCKET.send(pk);
        tb.App.addToSentPeers(
            p.getAddress()
                + ":"
                + p.getPort()
                + " "
                + tb.App.PEERS.get(pos).getAddress()
                + ":"
                + tb.App.PEERS.get(pos).getPort()
                + " "
                + Helper.getFormattedDate()
                + "\n");
      } catch (UnknownHostException uh) {
        tb.App.log.Warn(
            "Error: PeerClient - sendPeerInfo - UnknownHostException occured: " + p.getAddress());
      } catch (IOException io) {
        tb.App.log.Warn("Error: PeerClient - sendPeerInfo - IOException occured: ");
      }
    }
  }
}
