package com.jdutton.poc.kafka.admin.config;

import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import com.jdutton.poc.config.KafkaConfigData;

@EnableRetry
@Configuration
public class KafkaAdminConfig {

	private final KafkaConfigData kafkaConfigData;

	public KafkaAdminConfig(KafkaConfigData kafkaConfigData) {
		super();
		this.kafkaConfigData = kafkaConfigData;
	}

	@Bean
	public AdminClient adminClient() {
		return AdminClient
				.create(Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
						kafkaConfigData.getBootstrapServers()));
	}

}
