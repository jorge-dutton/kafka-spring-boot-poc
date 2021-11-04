package com.jdutton.poc.kafka.admin.client;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.jdutton.poc.config.KafkaConfigData;
import com.jdutton.poc.config.RetryConfigData;
import com.jdutton.poc.kafka.admin.exception.KafkaClientException;

@Component
public class KafkaAdminClient {

	private static final Logger LOG = LoggerFactory
			.getLogger(KafkaAdminClient.class);

	private final KafkaConfigData kafkaConfigData;

	private final RetryConfigData retryConfigData;

	private final AdminClient adminClient;

	private final RetryTemplate retryTemplate;

	private final WebClient webClient;

	public KafkaAdminClient(KafkaConfigData kafkaConfigData,
			RetryConfigData retryConfigData, AdminClient adminClient,
			RetryTemplate retryTemplate, WebClient webClient) {
		super();
		this.kafkaConfigData = kafkaConfigData;
		this.retryConfigData = retryConfigData;
		this.adminClient = adminClient;
		this.retryTemplate = retryTemplate;
		this.webClient = webClient;
	}

	public void createTopics() {
		CreateTopicsResult createTopicsResult;
		try {
			createTopicsResult = retryTemplate.execute(this::doCreateTopics);
			LOG.info("Create topic result {}", createTopicsResult.values().values());
		} catch (Throwable t) {
			throw new KafkaClientException(
					"Reached mx number of retries to create kafka topic(s).");
		}
		checkTopicsCreated();
	}

	public void checkTopicsCreated() {
		var topics = getTopics();
		var retryCount = 1;
		var maxRetry = retryConfigData.getMaxAttempts();
		var multiplier = retryConfigData.getMultiplier();
		var sleepTimeMs = retryConfigData.getSleepTimeMs();

		for (String topic : kafkaConfigData.getTopicNamesToCreate()) {
			while (!isTopicCreated(topics, topic)) {
				checkMaxRetry(retryCount++, maxRetry);
				sleep(sleepTimeMs);
				sleepTimeMs *= multiplier;
				topics = getTopics();
			}
		}
	}

	public void checkSchemaRegistry() {
		var retryCount = 1;
		var maxRetry = retryConfigData.getMaxAttempts();
		var multiplier = retryConfigData.getMultiplier();
		var sleepTimeMs = retryConfigData.getSleepTimeMs();
		while (!getSchemaRegistryStatus().is2xxSuccessful()) {
			checkMaxRetry(retryCount++, maxRetry);
			sleep(sleepTimeMs);
			sleepTimeMs *= multiplier;
		}
	}

	private HttpStatus getSchemaRegistryStatus() {

		try {
			// @formatter:off
			var response = webClient.get()
					.uri(kafkaConfigData.getSchemaRegistryUrl())
					.retrieve()
					.toBodilessEntity()
					.block();
			return response == null? HttpStatus.INTERNAL_SERVER_ERROR:response.getStatusCode();
			// @formatter:on
		} catch (Exception e) {
			return HttpStatus.SERVICE_UNAVAILABLE;
		}
	}

	private void sleep(Long sleepTimeMs) {
		try {
			Thread.sleep(sleepTimeMs);
		} catch (InterruptedException e) {
			throw new KafkaClientException(
					"Error while sleeping at waiting for new created topics");
		}

	}

	private void checkMaxRetry(int retry, Integer maxRetry) {
		if (retry > maxRetry) {
			throw new KafkaClientException(
					"Reached max number of retries for reading kafka topic(s).");
		}

	}

	private boolean isTopicCreated(Collection<TopicListing> topics,
			String topicName) {
		if (topics == null) {
			return false;
		}
		return topics.stream()
				.anyMatch(topic -> topic.name().equals(topicName));
	}

	private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
		var topicNames = kafkaConfigData.getTopicNamesToCreate();
		LOG.info("Creating {} topic(s), attempt {}", topicNames.size(),
				retryContext.getRetryCount());
		// @formatter:off
		var kafkaTopics = topicNames.stream()
				.map(topic -> new NewTopic(topic.trim(),
						kafkaConfigData.getNumOfPartitions(),
						kafkaConfigData.getReplicationFactor()))
				.collect(Collectors.toList());
		// @formatter:on
		return adminClient.createTopics(kafkaTopics);
	}

	private Collection<TopicListing> getTopics() {
		Collection<TopicListing> topics;
		try {
			topics = retryTemplate.execute(this::doGetTopics);
		} catch (Exception e) {
			throw new KafkaClientException(
					"Reached max number of retries for reading kafka topic(s)",
					e);
		}
		return topics;
	}

	private Collection<TopicListing> doGetTopics(RetryContext retryContext)
			throws ExecutionException, InterruptedException {
		LOG.info("Reading kafka topic {}, attempt {}",
				kafkaConfigData.getTopicNamesToCreate().toArray(),
				retryContext.getRetryCount());
		var topics = adminClient.listTopics().listings().get();
		if (topics != null) {
			topics.forEach(
					topic -> LOG.debug("Topic with name {}", topic.name()));
		}

		return topics;
	}

}
