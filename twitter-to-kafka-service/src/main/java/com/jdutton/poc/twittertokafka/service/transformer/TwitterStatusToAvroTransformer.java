package com.jdutton.poc.twittertokafka.service.transformer;

import org.springframework.stereotype.Component;

import com.jdutton.poc.kafka.avro.model.TwitterAvroModel;

import twitter4j.Status;

@Component
public class TwitterStatusToAvroTransformer {
	public TwitterAvroModel getTwitterAvroModel(Status status) {
		return TwitterAvroModel.newBuilder()
				.setId(status.getId())
				.setUserId(status.getUser().getId())
				.setText(status.getText())
				.setCreatedAt(status.getCreatedAt()
				.getTime()).build();
	}
}
