package com.cdr.gen;

import java.util.Map;

import com.cdr.gen.util.Configuration;

import junit.framework.TestCase;

public class CDRGenTest extends TestCase {

  public CDRGenTest(String testName) {
    super(testName);
  }

  /** Test of loadConfig method, of class CDRGen. */
  public void testLoadConfig() {
    CDRGen generator = new CDRGen();

    Configuration config = generator.getConfig();

    Map<String, Map<String, Double>> outgoingCallParams = config.getOutgoingCallParams();
    Map<String, Double> conf = outgoingCallParams.get("Free");

    long stdDev = conf.get("callStdDev").longValue();
    long mean = conf.get("callDur").longValue();

    System.out.println("Std Dev: " + stdDev + "\nMean: " + mean);
  }
}
