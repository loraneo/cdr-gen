package com.cdr.gen;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.kafka.clients.producer.Producer;
import org.apache.log4j.Logger;

import com.cdr.gen.util.KafkaProducerExample;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

/**
 * This class only loads the configuration file and handles the saving of the population to a file.
 *
 * @author Maycon Viana Bordin <mayconbordin@gmail.com>
 */
public final class CDRGen {
  private static final Logger LOG = Logger.getLogger(CDRGen.class);

  public static void saveToFile(List<Call> calls, Producer<String, String> producer) {

    for (Call c : calls) {
      String message =
          new StringBuilder()
              .append(c.getTime())
              .append(",")
              .append(c.getCallerId())
              .append(",")
              .append(c.getFrom())
              .append(",")
              .append(c.getTo())
              .append(",")
              .append(c.getDisposition())
              .append(",")
              .append(c.getSeconds())
              .append(",")
              .append(c.getCost())
              .toString();
      KafkaProducerExample.sendMessage(producer, message);
    }
    producer.flush();
  }

  static List<Call> generateCalls(LocalDateTime now) {
    String[] regions = PhoneNumberUtil.getInstance().getSupportedRegions().toArray(new String[0]);

    List<Call> clls = new ArrayList<>();
    int numberOfCalls = getDistribution(now);
    for (int i = 0; i < numberOfCalls; i++) {
      String code = "NG";
      String fromRegion = "";
      String toRegion = "";
      if (i % 4 == 0) {
        fromRegion = regions[new Random().nextInt(regions.length)];
        toRegion = code;
      } else {
        fromRegion = code;
        toRegion = regions[new Random().nextInt(regions.length)];
      }

      PhoneNumber sourceNumber = PhoneNumberUtil.getInstance().getExampleNumber(fromRegion);
      PhoneNumber targetNumber = PhoneNumberUtil.getInstance().getExampleNumber(toRegion);
      clls.add(
          Call.builder()
              .time(now.format(java.time.format.DateTimeFormatter.ISO_DATE_TIME))
              .callerId(Integer.toString(i))
              .from(sourceNumber.getCountryCode() + "" + sourceNumber.getNationalNumber())
              .to(targetNumber.getCountryCode() + "" + targetNumber.getNationalNumber())
              .disposition("answered")
              .seconds(new Random().nextInt(120))
              .cost(0.02d)
              .build());
    }

    return clls;
  }

  public static int getDistribution(LocalDateTime now) {
    int time = now.getHour() * 60 + now.getMinute();
    Gaussian distribution = new Gaussian(1d, 0.5d);

    double point = time / 1440d;

    int count = 700;
    int diff = 1700;
    int numberOfCalls = (int) (count + diff * (distribution.value(point)));
    return numberOfCalls;
  }

  public static void main(String[] args) throws InterruptedException {

    Producer<String, String> producer = KafkaProducerExample.createProducer();

    while (true) {
      LocalDateTime now = LocalDateTime.now();
      System.out.println("Generating calls for minute: " + now.toString());
      saveToFile(generateCalls(now), producer);
      Thread.sleep(1000 * 60);
      LOG.info("Done.");
    }
  }
}
