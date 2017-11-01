package com.greglturnquist.learningspringboot.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

// tag::code[]
//@SpringCloudApplication
@SpringCloudApplication
public class LearningSpringBootChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(
			LearningSpringBootChatApplication.class, args);
	}
}
// end::code[]