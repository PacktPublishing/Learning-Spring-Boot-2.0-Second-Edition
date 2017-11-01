package com.greglturnquist.learningspringboot.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;

// tag::code[]
@SpringCloudApplication
public class LearningSpringBootChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(
			LearningSpringBootChatApplication.class, args);
	}

	@Bean
	HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
}
// end::code[]