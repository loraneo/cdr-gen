package com.cdr.gen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.kafka.clients.producer.Producer;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.cdr.gen.util.Configuration;
import com.cdr.gen.util.IOUtils;
import com.cdr.gen.util.JavaUtils;
import com.cdr.gen.util.KafkaProducerExample;
import com.google.common.io.Files;
import com.google.gson.Gson;

/**
 * This class only loads the configuration file and handles the saving of the population to a file.
 *
 * @author Maycon Viana Bordin <mayconbordin@gmail.com>
 */
public final class CDRGen {
  private static final Logger LOG = Logger.getLogger(CDRGen.class);
  private static final String DEFAULT_CONFIG_FILE = "/config.json";
  private Configuration config;

  public CDRGen() {
    loadConfig(DEFAULT_CONFIG_FILE);
  }

  public CDRGen(String configFile) {
    loadConfig(configFile);
  }

  public void loadConfig(String file) {
    try {

      String configStr;
      if (JavaUtils.isJar() && file.equals(DEFAULT_CONFIG_FILE)) {
        InputStream is = CDRGen.class.getResourceAsStream(file);
        configStr = IOUtils.convertStreamToString(is);
      } else {
        if (file.equals(DEFAULT_CONFIG_FILE)) file = "src/main/resources" + file;

        configStr = Files.toString(new File(file), Charset.defaultCharset());
      }

      Gson gson = new Gson();
      config = gson.fromJson(configStr, Configuration.class);

    } catch (IOException ex) {
      LOG.error("Unable to read config file '" + file + "'.", ex);
    }
  }

  public Configuration getConfig() {
    return config;
  }

  public void saveToFile(List<Person> customers) {
    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

    Producer<String, String> producer = KafkaProducerExample.createProducer();
    for (Person p : customers) {
      for (Call c : p.getCalls()) {
        KafkaProducerExample.sendMessage(
            producer,
            new StringBuilder()
                .append(c.getId())
                .append(",")
                .append(p.getPhoneNumber())
                .append(",")
                .append(c.getLine())
                .append(",")
                .append(c.getDestPhoneNumber())
                .append(",")
                .append(c.getTime().getStart().toString(dateFormatter))
                .append(",")
                .append(c.getTime().getEnd().toString(dateFormatter))
                .append(",")
                .append(c.getTime().getStart().toString(timeFormatter))
                .append(",")
                .append(c.getTime().getEnd().toString(timeFormatter))
                .append(",")
                .append(c.getType())
                .append(",")
                .append(c.getCost())
                .toString());
      }
    }

    producer.flush();
    producer.close();
  }

  public static void main(String[] args) throws InterruptedException {

    String configFile = DEFAULT_CONFIG_FILE;
    CDRGen generator = new CDRGen(configFile);

    Population population = new Population(generator.getConfig());
    population.create();

    List<Person> customers = population.getPopulation();
    generator.saveToFile(customers);
    Thread.sleep(10000);
    LOG.info("Done.");
  }
}
