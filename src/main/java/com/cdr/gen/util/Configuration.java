package com.cdr.gen.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Configuration implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  private Map<String, Long> callsMade;
  private int numAccounts;
  private Map<String, Long> phoneLines;
  private List<String> callTypes;
  private Map<String, Map<String, Double>> outgoingCallParams;
  private Map<String, String> offPeakTimePeriod;
  private Map<String, Double> dayDistribution;
  private String startDate;
  private String endDate;
  private String timeDistCsv;
  private Map<String, Map<String, Long>> outgoingNumberDistribution;

  public Map<String, Long> getCallsMade() {
    return callsMade;
  }

  public int getNumAccounts() {
    return numAccounts;
  }

  public Map<String, Long> getPhoneLines() {
    return phoneLines;
  }

  public List<String> getCallTypes() {
    return callTypes;
  }

  public Map<String, Map<String, Double>> getOutgoingCallParams() {
    return outgoingCallParams;
  }

  public Map<String, String> getOffPeakTimePeriod() {
    return offPeakTimePeriod;
  }

  public Map<String, Double> getDayDistribution() {
    return dayDistribution;
  }

  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public String getTimeDistCsv() {
    return timeDistCsv;
  }

  public Map<String, Map<String, Long>> getOutgoingNumberDistribution() {
    return outgoingNumberDistribution;
  }
}
