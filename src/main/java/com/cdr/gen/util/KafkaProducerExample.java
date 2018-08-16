package com.cdr.gen.util;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducerExample {

  private static final Logger log = LoggerFactory.getLogger(KafkaProducerExample.class);

  private static final String BOOTSTRAP_SERVERS = "kafka-broker-1:9092,kafka-broker-2:9092,kafka-broker-3:9092";
  private static final String TOPIC = "public.cdr";

  public static Producer<String, String> createProducer() {
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    return new KafkaProducer<>(props);
  }

  public static void sendMessage(Producer<String, String> producer, final String message) {
    try {
      producer.send(new ProducerRecord<>(TOPIC, message)).get();
    } catch (InterruptedException | ExecutionException e) {
      log.error("Unable to send message", e);
    } finally {
      producer.flush();
    }
  }
}
