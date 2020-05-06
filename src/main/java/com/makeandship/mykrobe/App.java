package com.makeandship.mykrobe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.schema.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableSchemaRegistryClient
@EnableKafkaStreams
public class App implements CommandLineRunner {
	public static void main(String[] args) {
		log.trace(App.class.getName() + "#main: enter");
		SpringApplication.run(App.class, args);
		log.trace(App.class.getName() + "#main: exit");
		// Removed Runner
	}

	@Override
	public void run(String... args) throws Exception {

	}

	@Value("${shared.configuration.schema.registry.url}")
	private String endPoint;

	@Bean
	public SchemaRegistryClient schemaRegistryClient() {
		ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
		client.setEndpoint(endPoint);
		return client;
	}
}
