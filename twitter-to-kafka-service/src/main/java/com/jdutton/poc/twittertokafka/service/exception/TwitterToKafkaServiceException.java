package com.jdutton.poc.twittertokafka.service.exception;

public class TwitterToKafkaServiceException extends RuntimeException {

	private static final long serialVersionUID = 2178288302532830941L;

	public TwitterToKafkaServiceException() {
		super();
	}

	public TwitterToKafkaServiceException(String message) {
		super(message);
	}

	public TwitterToKafkaServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
