package com.greglturnquist.learningspringboot;

import reactor.core.publisher.Hooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.reactor.core.ReactorCoreProperties;
import org.springframework.cloud.client.SpringCloudApplication;

// tag::code[]
@SpringCloudApplication
public class LearningSpringBootImagesApplication {

	public static void main(String[] args) {
		SpringApplication.run(
			LearningSpringBootImagesApplication.class, args);
	}

	@Autowired
	protected void initialize(ReactorCoreProperties properties) {
		if (properties.getStacktraceMode().isEnabled()) {
			Hooks.onOperatorDebug();
		}
	}
}
// end::code[]