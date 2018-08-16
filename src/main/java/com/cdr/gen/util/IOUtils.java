package com.cdr.gen.util;

import java.io.InputStream;
import java.util.Scanner;

public class IOUtils {
  public static String convertStreamToString(InputStream is) {
    try (Scanner s = new Scanner(is).useDelimiter("\\A"); ) {
      return s.hasNext() ? s.next() : "";
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String convertStreamToString(InputStream is, String charsetName) {
    try (Scanner s = new Scanner(is, charsetName).useDelimiter("\\A")) {
      return s.hasNext() ? s.next() : "";
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
