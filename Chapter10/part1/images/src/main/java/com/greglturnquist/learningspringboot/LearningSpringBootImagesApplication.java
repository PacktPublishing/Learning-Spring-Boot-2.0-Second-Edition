package com.greglturnquist.learningspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

// tag::code[]
@SpringCloudApplication
public class LearningSpringBootImagesApplication {

	public static void main(String[] args) {
		SpringApplication.run(
			LearningSpringBootImagesApplication.class, args);
	}
}
// end::code[]