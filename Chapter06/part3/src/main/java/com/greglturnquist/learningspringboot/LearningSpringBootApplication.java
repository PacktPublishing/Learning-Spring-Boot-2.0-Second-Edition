package com.greglturnquist.learningspringboot;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;

// tag::code[]
@SpringBootApplication
public class LearningSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningSpringBootApplication.class, args);
	}

	@Bean
	HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}

	@Bean
	ParameterMessageInterpolator parameterMessageInterpolator() {
		return new ParameterMessageInterpolator();
	}
}
// end::code[]