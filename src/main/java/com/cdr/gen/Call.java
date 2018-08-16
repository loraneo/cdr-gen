package com.cdr.gen;

import java.io.Serializable;

import org.joda.time.Interval;

/**
 * Holds information about a call.
 *
 * @author Maycon Viana Bordin <mayconbordin@gmail.com>
 */
public class Call implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  private long id;
  private int line;
  private String type;
  private Interval time;
  private double cost;
  private String destPhoneNumber;

  public long getId() {
    return this.id;
  }

  public int getLine() {
    return this.line;
  }

  public String getType() {
    return this.type;
  }

  public Interval getTime() {
    return this.time;
  }

  public double getCost() {
    return this.cost;
  }

  public String getDestPhoneNumber() {
    return this.destPhoneNumber;
  }

  public static Builder builder() {
    return new Call().new Builder();
  }

  public class Builder {
    public Builder id(long id) {
      Call.this.id = id;
      return this;
    }

    public Builder line(int line) {
      Call.this.line = line;
      return this;
    }

    public Builder type(String type) {
      Call.this.type = type;
      return this;
    }

    public Builder time(Interval time) {
      Call.this.time = time;
      return this;
    }

    public Builder cost(double cost) {
      Call.this.cost = cost;
      return this;
    }

    public Builder destPhoneNumber(String destPhoneNumber) {
      Call.this.destPhoneNumber = destPhoneNumber;
      return this;
    }

    public Call build() {
      return Call.this;
    }
  }
}
