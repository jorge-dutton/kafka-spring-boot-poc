package com.jdutton.poc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "retry-config")
public class RetryConfigData {
	
	private Long initialIntervalMs;
	private Long maxIntervalMs;
	private Integer multiplier;
	private Integer maxAttempts;
	private Long sleepTimeMs;
}
