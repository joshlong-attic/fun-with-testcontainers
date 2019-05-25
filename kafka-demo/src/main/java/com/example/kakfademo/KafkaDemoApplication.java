package com.example.kakfademo;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testcontainers.containers.KafkaContainer;

import java.time.Instant;

@Log4j2
@SpringBootApplication
@RestController
public class KafkaDemoApplication {

	private static final String TOPIC = "greetings";

	public static void main(String[] args) {
		KafkaContainer kafkaContainer = new KafkaContainer();
		kafkaContainer.start();
		System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
		SpringApplication.run(KafkaDemoApplication.class, args);
	}

	@PostMapping("/greet/{name}")
	void greet(@PathVariable String name) {
		Message<String> build = MessageBuilder.withPayload("Hello " + name + " @ " + Instant.now())
			.setHeader(KafkaHeaders.TOPIC, this.TOPIC)
			.setHeader(KafkaHeaders.GROUP_ID, this.TOPIC)
			.build();
		this.out().send(build);
	}

	@KafkaListener(
		groupId = KafkaDemoApplication.TOPIC,
		topics = KafkaDemoApplication.TOPIC)
	public void onNewMessage(String payload) {
		log.info("new message: " + payload);
	}

	@Bean
	NewTopic greetingsTopic() {
		return new NewTopic(TOPIC, 1, (short) 1);
	}

	@Bean
	MessageChannel out() {
		return MessageChannels.direct().get();
	}

	@Bean
	IntegrationFlow consume(KafkaTemplate<String, String> kt) {
		return IntegrationFlows
			.from(this.out())
			.handle(Kafka.outboundChannelAdapter(kt))
			.get();
	}
}
