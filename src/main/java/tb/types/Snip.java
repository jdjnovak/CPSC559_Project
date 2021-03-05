package tb.types;

/*
 * Class for the Snip object
 */
public class Snip {
  private String CONTENT;
  private String SRC_ADDR;
  private int SRC_PORT;
  private int TIMESTAMP;

  /* params: string, string, int, int
   *   Constructor for Snip class with four parameters
   * returns: Snip
   */
  public Snip(String c, String a, int p, int t) {
    this.CONTENT = c;
    this.SRC_ADDR = a;
    this.SRC_PORT = p;
    this.TIMESTAMP = t;
  }

  /* params: none
   *   Returns the string of a snips content
   * returns: string
   */
  public String getContent() {
    return this.CONTENT;
  }

  /* params: none
   *   Returns the string of the snips source address
   * returns: string
   */
  public String getSourceAddress() {
    return this.SRC_ADDR;
  }

  /* params: none
   *   Returns the int of the snips source port
   * returns: int
   */
  public int getSourcePort() {
    return this.SRC_PORT;
  }

  /* params: none
   *   Returns the int of the snips timestamp
   * returns: int
   */
  public int getTimestamp() {
    return this.TIMESTAMP;
  }

  /* params: none
   *   Overrides the toString method for the snip class
   * returns: string
   */
  public String toString() {
    return this.TIMESTAMP + " " + this.CONTENT + " " + this.SRC_ADDR + ":" + this.SRC_PORT;
  }
}
