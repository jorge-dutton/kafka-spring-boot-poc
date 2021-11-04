package com.jdutton.poc.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.jdutton.poc.config.RetryConfigData;

@Configuration
public class RetryConfig {

	private final RetryConfigData retryConfigData;

	public RetryConfig(RetryConfigData retryConfigData) {
		super();
		this.retryConfigData = retryConfigData;
	}
	
	@Bean
	public RetryTemplate retryTemplate() {
		var retryTemplate = new RetryTemplate();
		
		var exponentialBackoffPolicy = new ExponentialBackOffPolicy();
		exponentialBackoffPolicy.setInitialInterval(retryConfigData.getInitialIntervalMs());
		exponentialBackoffPolicy.setMaxInterval(retryConfigData.getMaxIntervalMs());
		exponentialBackoffPolicy.setMultiplier(retryConfigData.getMultiplier());

		retryTemplate.setBackOffPolicy(exponentialBackoffPolicy);
		
		var simpleRetryPolicy = new SimpleRetryPolicy();
		simpleRetryPolicy.setMaxAttempts(retryConfigData.getMaxAttempts());
		
		retryTemplate.setRetryPolicy(simpleRetryPolicy);
		
		return retryTemplate;
	}
}
