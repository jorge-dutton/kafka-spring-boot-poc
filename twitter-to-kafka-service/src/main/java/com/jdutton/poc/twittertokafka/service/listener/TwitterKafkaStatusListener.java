package com.jdutton.poc.twittertokafka.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jdutton.poc.config.KafkaConfigData;
import com.jdutton.poc.kafka.avro.model.TwitterAvroModel;
import com.jdutton.poc.kafka.producer.config.service.KafkaProducer;
import com.jdutton.poc.twittertokafka.service.transformer.TwitterStatusToAvroTransformer;

import twitter4j.Status;
import twitter4j.StatusAdapter;

@Component
public class TwitterKafkaStatusListener extends StatusAdapter {

	private static final Logger LOG = LoggerFactory
			.getLogger(TwitterKafkaStatusListener.class);
	
	private final KafkaConfigData kafkaConfigData;
	private final KafkaProducer<Long, TwitterAvroModel> kafkaProducer;
	private final TwitterStatusToAvroTransformer twitterStatusToAvroTransformer;
	
	

	public TwitterKafkaStatusListener(KafkaConfigData kafkaConfigData,
			KafkaProducer<Long, TwitterAvroModel> kafkaProducer,
			TwitterStatusToAvroTransformer twitterStatusToAvroTransformer) {
		super();
		this.kafkaConfigData = kafkaConfigData;
		this.kafkaProducer = kafkaProducer;
		this.twitterStatusToAvroTransformer = twitterStatusToAvroTransformer;
	}



	@Override
	public void onStatus(Status status) {
		LOG.info("Twitter status with text {} sending to topic {}.", status.getText(), kafkaConfigData.getTopicName());
		var twitterAvroModel = twitterStatusToAvroTransformer.getTwitterAvroModel(status);
		kafkaProducer.send(kafkaConfigData.getTopicName(), twitterAvroModel.getUserId(), twitterAvroModel);
	}

}
