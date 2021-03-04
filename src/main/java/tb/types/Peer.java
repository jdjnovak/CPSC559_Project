package tb.types;

public class Peer {
  private String ADDR;
  private int PORT;
  private String SRC_ADDR;
  private String TIMESTAMP;

  // Parameterized constructor
  public Peer(String adr, int prt, String src, String recv) {
    this.ADDR = adr;
    this.PORT = prt;
    this.SRC_ADDR = src;
    this.TIMESTAMP = recv;
  }

  // Return Peer Address
  public String getAddress() {
    return this.ADDR;
  }

  // Return Peer Port
  public int getPort() {
    return this.PORT;
  }

  // Return Peer Source Address
  public String getSourceAddress() {
    return this.SRC_ADDR;
  }

  // Return Peer Timestamp
  public String getTimestamp() {
    return this.TIMESTAMP;
  }

  // Override the toString method for the needed output
  public String toString() {
    return this.ADDR + ":" + this.PORT + " " + this.SRC_ADDR + " " + this.TIMESTAMP;
  }
}
