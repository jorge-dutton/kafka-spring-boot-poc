package com.jdutton.poc.twittertokafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.jdutton.poc.twittertokafka.service.config.TwitterToKafkaServiceConfigData;

@SpringBootApplication
@ComponentScan(basePackages = "com.jdutton.poc.twittertokafka")
public class TwitterToKafkaServiceApplication implements CommandLineRunner{
	
	private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);
	
	private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfig;
	
	public TwitterToKafkaServiceApplication(final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfig) {
		super();
		this.twitterToKafkaServiceConfig = twitterToKafkaServiceConfig;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("Intializing app");
		twitterToKafkaServiceConfig.getTwitterKeywords().forEach(LOG::info);
		LOG.info(twitterToKafkaServiceConfig.getWelcomeMessage());
		
	}

}
