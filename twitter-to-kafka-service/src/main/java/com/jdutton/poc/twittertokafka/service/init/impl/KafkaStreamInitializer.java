package com.jdutton.poc.twittertokafka.service.init.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jdutton.poc.config.KafkaConfigData;
import com.jdutton.poc.kafka.admin.client.KafkaAdminClient;
import com.jdutton.poc.twittertokafka.service.init.StreamInitializer;

@Component
public class KafkaStreamInitializer implements StreamInitializer {
	
	private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamInitializer.class);
	
	private final KafkaConfigData kafkaConfigData;
	
	private final KafkaAdminClient kafkaAdminClient;
	
	public KafkaStreamInitializer(KafkaConfigData kafkaConfigData,
			KafkaAdminClient kafkaAdminClient) {
		super();
		this.kafkaConfigData = kafkaConfigData;
		this.kafkaAdminClient = kafkaAdminClient;
	}

	@Override
	public void init() {
		kafkaAdminClient.createTopics();
		kafkaAdminClient.checkSchemaRegistry();
		LOG.info("Topics with name {} are ready for operations", kafkaConfigData.getTopicNamesToCreate().toArray());
	}

}
