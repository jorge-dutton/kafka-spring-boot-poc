package com.jdutton.poc.twittertokafka.service;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jdutton.poc.twittertokafka.service.config.TwitterToKafkaServiceConfig;

@SpringBootApplication
public class TwitterToKafkaServiceApplication implements CommandLineRunner{
	
	private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);
	
	private final TwitterToKafkaServiceConfig twitterToKafkaServiceConfig;
	
	public TwitterToKafkaServiceApplication(final TwitterToKafkaServiceConfig twitterToKafkaServiceConfig) {
		super();
		this.twitterToKafkaServiceConfig = twitterToKafkaServiceConfig;
	}
	
	public static void main(String... args) {
		SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("Intializing app");
		LOG.info(Arrays.toString(twitterToKafkaServiceConfig.getTwitterKeywords().toArray(new String[] {})));
		
	}

}
