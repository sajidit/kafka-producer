package com.example.kafka.producer;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.util.backoff.FixedBackOff;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class KafkaProducerApplication {

	private final Logger logger =  LoggerFactory.getLogger(KafkaProducerApplication.class);
 
	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerApplication.class, args).close();
	}

	/*
	 * Boot will autowire this into the container factory.
	 */
	@Bean
	public SeekToCurrentErrorHandler errorHandler(KafkaOperations<Object, Object> template) {
		return new SeekToCurrentErrorHandler(
				new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 2));
	}

	@Bean
	public RecordMessageConverter converter() {
		return new StringJsonMessageConverter();
	}

	@Bean
	public NewTopic topic() {
		return new NewTopic("topic1", 1, (short) 1);
	}

	@Bean
	@Profile("default") // Don't run from test(s)
	public ApplicationRunner runner() {
		return args -> {
			System.out.println("Hit Enter to terminate...");
			System.in.read();
		};
	}

}
