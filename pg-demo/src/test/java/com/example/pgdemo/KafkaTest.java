package com.example.pgdemo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.KafkaContainer;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class KafkaTest {

	static {
		KafkaContainer kafkaContainer = new KafkaContainer();
		kafkaContainer.start();
		System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
	}
	@Configuration
	static class IntegrationConfig {


	}


	@Test
	public void connect (){

	}
}
