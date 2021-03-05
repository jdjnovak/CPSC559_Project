package tb.types;

/*
 * Class for the Peer object
 */
public class Peer {
  private String ADDR;
  private int PORT;
  private String SRC_ADDR;
  private String TIMESTAMP;

  /* params: string, int, string, string
   *   The parametized constructor of a peer
   * returns: Peer
   */
  public Peer(String adr, int prt, String src, String recv) {
    this.ADDR = adr;
    this.PORT = prt;
    this.SRC_ADDR = src;
    this.TIMESTAMP = recv;
  }

  /* params: none
   *   Returns a string of the address of the peer
   * returns: string
   */
  public String getAddress() {
    return this.ADDR;
  }

  /* params: none
   *   Returns the integer of the peer port
   * returns: int
   */
  public int getPort() {
    return this.PORT;
  }

  /* params: none
   *   Returns the string of the source address in
   *   in the format of <ip>:<port>
   * returns: string
   */
  public String getSourceAddress() {
    return this.SRC_ADDR;
  }

  /* params: none
   *   Returns the string of the peer timestamp
   * returns: string
   */
  public String getTimestamp() {
    return this.TIMESTAMP;
  }

  /* params: none
   *   Overriden toString method for the peer class
   * returns: string
   */
  public String toString() {
    return this.SRC_ADDR + " " + this.ADDR + ":" + this.PORT + " " + this.TIMESTAMP;
  }
}
