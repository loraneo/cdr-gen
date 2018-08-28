package com.cdr.gen;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.apache.kafka.clients.producer.Producer;

import com.cdr.gen.util.KafkaProducerExample;

public class CDRHistory {
	public static void main(String[] args) {
		Producer<String, String> producer = KafkaProducerExample.createProducer();

		IntStream.range(0, 47520).parallel().forEach(p -> {
			System.out.println("Generating calls for minute: " + p);
			LocalDateTime now = LocalDateTime.now().minusMinutes(p);
			CDRGen.saveToFile(CDRGen.generateCalls(now), producer);
		});
		producer.close();
	}
}
