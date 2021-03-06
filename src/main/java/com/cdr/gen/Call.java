package com.cdr.gen;

import java.io.Serializable;

/**
 * Holds information about a call.
 *
 * @author Maycon Viana Bordin <mayconbordin@gmail.com>
 */
public class Call implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  private String time;
  private String callerId;
  private String from;
  private String to;
  private String disposition;
  private long seconds;
  private double cost;

  public String getTime() {
    return this.time;
  }

  public String getCallerId() {
    return this.callerId;
  }

  public String getFrom() {
    return this.from;
  }

  public String getTo() {
    return this.to;
  }

  public String getDisposition() {
    return this.disposition;
  }

  public long getSeconds() {
    return this.seconds;
  }

  public double getCost() {
    return this.cost;
  }

  public static Builder builder() {
    return new Call().new Builder();
  }

  public class Builder {
    public Builder time(String time) {
      Call.this.time = time;
      return this;
    }

    public Builder callerId(String callerId) {
      Call.this.callerId = callerId;
      return this;
    }

    public Builder from(String from) {
      Call.this.from = from;
      return this;
    }

    public Builder to(String to) {
      Call.this.to = to;
      return this;
    }

    public Builder disposition(String disposition) {
      Call.this.disposition = disposition;
      return this;
    }

    public Builder seconds(long seconds) {
      Call.this.seconds = seconds;
      return this;
    }

    public Builder cost(double cost) {
      Call.this.cost = cost;
      return this;
    }

    public Call build() {
      return Call.this;
    }
  }
}
