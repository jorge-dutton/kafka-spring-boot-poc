package com.jdutton.poc.twittertokafka.service.runner.impl;

import java.util.Arrays;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.jdutton.poc.config.TwitterToKafkaServiceConfigData;
import com.jdutton.poc.twittertokafka.service.listener.TwitterKafkaStatusListener;
import com.jdutton.poc.twittertokafka.service.runner.StreamRunner;

import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "false", matchIfMissing = true)
public class TwitterKafkaStreamRunner implements StreamRunner {

	private static final Logger LOG = LoggerFactory
			.getLogger(TwitterKafkaStreamRunner.class);

	private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;

	private final TwitterKafkaStatusListener twitterKafkaStatusListener;

	private TwitterStream twitterStream;

	public TwitterKafkaStreamRunner(
			TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData,
			TwitterKafkaStatusListener twitterKafkaStatusListener) {
		super();
		this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
		this.twitterKafkaStatusListener = twitterKafkaStatusListener;
	}

	@Override
	public void start() throws TwitterException {
		this.twitterStream = new TwitterStreamFactory().getInstance();
		this.twitterStream.addListener(twitterKafkaStatusListener);
		addFilter();
	}

	@PreDestroy
	public void shutdown() {
		if (twitterStream != null) {

		}
	}
	
	private void addFilter() {
		String[] keywords = twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[0]);
		var filterQuery = new FilterQuery(keywords);
		twitterStream.filter(filterQuery);
		var keywordsStr = Arrays.toString(keywords);
		LOG.info("Started filtering twitter stream for keywords {}", keywordsStr);
	}
}
