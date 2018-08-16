package com.cdr.gen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.cdr.gen.util.Configuration;
import com.cdr.gen.util.IOUtils;
import com.cdr.gen.util.JavaUtils;
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

  public void saveToFile(String outputFile, List<Person> customers) {
    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

    try {
      FileWriter fw = new FileWriter(outputFile);
      String newLine = System.getProperty("line.separator");

      for (Person p : customers) {
        for (Call c : p.getCalls()) {
          fw.append(
              c.getId()
                  + ","
                  + p.getPhoneNumber()
                  + ","
                  + c.getLine()
                  + ","
                  + c.getDestPhoneNumber()
                  + ","
                  + c.getTime().getStart().toString(dateFormatter)
                  + ","
                  + c.getTime().getEnd().toString(dateFormatter)
                  + ","
                  + c.getTime().getStart().toString(timeFormatter)
                  + ","
                  + c.getTime().getEnd().toString(timeFormatter)
                  + ","
                  + c.getType()
                  + ","
                  + c.getCost()
                  + newLine);
        }
      }

      fw.close();
    } catch (IOException ex) {
      LOG.error("Error while writing the output file.", ex);
    }
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      String exec =
          new java.io.File(
                  CDRGen.class.getProtectionDomain().getCodeSource().getLocation().getPath())
              .getName();
      System.out.println("Usage: java -jar " + exec + " <output_file> [<config_file>]");
      System.exit(1);
    }

    String configFile = (args.length > 1) ? args[1] : DEFAULT_CONFIG_FILE;
    CDRGen generator = new CDRGen(configFile);

    Population population = new Population(generator.getConfig());
    population.create();

    List<Person> customers = population.getPopulation();
    generator.saveToFile(args[0], customers);
    LOG.info("Done.");
  }
}
