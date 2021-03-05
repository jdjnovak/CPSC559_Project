package tb.types;

public class Snip {
  private String CONTENT;
  private String SRC_ADDR;
  private int SRC_PORT;
  private int TIMESTAMP;

  public Snip(String c, String a, int p, int t) {
    this.CONTENT = c;
    this.SRC_ADDR = a;
    this.SRC_PORT = p;
    this.TIMESTAMP = t;
  }

  public String getContent() {
    return this.CONTENT;
  }

  public String getSourceAddress() {
    return this.SRC_ADDR;
  }

  public int getSourcePort() {
    return this.SRC_PORT;
  }

  public int getTimestamp() {
    return this.TIMESTAMP;
  }

  public String toString() {
    return this.TIMESTAMP + " " + this.CONTENT + " " + this.SRC_ADDR + ":" + this.SRC_PORT;
  }
}
