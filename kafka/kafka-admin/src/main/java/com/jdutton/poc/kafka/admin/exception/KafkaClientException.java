package com.jdutton.poc.kafka.admin.exception;

public class KafkaClientException extends RuntimeException {

	private static final long serialVersionUID = -8439527976286594877L;

	public KafkaClientException() {
		super();
	}

	public KafkaClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public KafkaClientException(String message) {
		super(message);
	}
	
	

}
