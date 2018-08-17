package com.cdr.gen;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

  public static void saveToFile() {

    Producer<String, String> producer = KafkaProducerExample.createProducer();
    for (Call c : generateCalls(new Random().nextInt(100))) {
      KafkaProducerExample.sendMessage(
          producer,
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
              .append(c.getMinutes())
              .append(",")
              .append(c.getCost())
              .toString());
    }

    producer.flush();
    producer.close();
  }

  private static List<Call> generateCalls(int num) {
    String[] regions = PhoneNumberUtil.getInstance().getSupportedRegions().toArray(new String[0]);

    List<Call> clls = new ArrayList<>();
    for (int i = 0; i < num; i++) {
      String fromRegion = regions[new Random().nextInt(regions.length)];
      String toRegion = regions[new Random().nextInt(regions.length)];
      PhoneNumber sourceNumber = PhoneNumberUtil.getInstance().getExampleNumber(fromRegion);
      PhoneNumber targetNumber = PhoneNumberUtil.getInstance().getExampleNumber(toRegion);
      clls.add(
          Call.builder()
              .time(LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_DATE_TIME))
              .callerId(Integer.toString(i))
              .from(sourceNumber.toString())
              .to(targetNumber.toString())
              .disposition("answered")
              .minutes(new Random().nextInt(120))
              .cost(0.02d)
              .build());
    }

    return clls;
  }

  public static void main(String[] args) throws InterruptedException {
    while (true) {
      saveToFile();
      Thread.sleep(1000);
      LOG.info("Done.");
    }
  }
}
