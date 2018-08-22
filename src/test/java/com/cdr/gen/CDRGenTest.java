package com.cdr.gen;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

public class CDRGenTest {

  @Test
  public void test() {

    int sum = 0;
    for (int i = 0; i < (24 * 60); i++) {
      LocalDateTime now = LocalDateTime.of(2018, 8, 22, i / 60, i % 60, 0);
      sum += CDRGen.getDistribution(now);
    }
    Assert.assertEquals(2175033, sum);
    System.out.println(sum);
  }
}
