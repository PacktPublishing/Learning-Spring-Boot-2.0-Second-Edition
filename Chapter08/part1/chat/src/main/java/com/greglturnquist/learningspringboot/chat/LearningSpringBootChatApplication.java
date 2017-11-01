package com.greglturnquist.learningspringboot.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// tag::code[]
@SpringBootApplication
@EnableEurekaClient
public class LearningSpringBootChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(
			LearningSpringBootChatApplication.class, args);
	}
}
// end::code[]