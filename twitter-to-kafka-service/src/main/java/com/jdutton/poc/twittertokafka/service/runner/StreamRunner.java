package com.jdutton.poc.twittertokafka.service.runner;

import twitter4j.TwitterException;

public interface StreamRunner {
	void start() throws TwitterException;
}
