package com.jdutton.poc.twittertokafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.jdutton.poc.config.TwitterToKafkaServiceConfigData;
import com.jdutton.poc.twittertokafka.service.runner.StreamRunner;

@SpringBootApplication
@ComponentScan(basePackages = "com.jdutton.poc")
public class TwitterToKafkaServiceApplication implements CommandLineRunner{
	
	private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);
	
	private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfig;
	
	private final StreamRunner streamRunner;
	
	public TwitterToKafkaServiceApplication(final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfig, final StreamRunner streamRunner) {
		super();
		this.twitterToKafkaServiceConfig = twitterToKafkaServiceConfig;
		this.streamRunner = streamRunner;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("Intializing app");
		twitterToKafkaServiceConfig.getTwitterKeywords().forEach(LOG::info);
		LOG.info(twitterToKafkaServiceConfig.getWelcomeMessage());
		this.streamRunner.start();
	}

}
