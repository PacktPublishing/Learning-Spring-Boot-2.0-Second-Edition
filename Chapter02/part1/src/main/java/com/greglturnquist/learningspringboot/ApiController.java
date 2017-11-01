/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.learningspringboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Greg Turnquist
 */
@RestController
public class ApiController {

	private static final Logger log = LoggerFactory.getLogger(ApiController.class);

	private static final String API_BASE_PATH = "/api";

	// tag::get[]
	@GetMapping(API_BASE_PATH + "/images")
	Flux<Image> images() {
		return Flux.just(
			new Image("1", "learning-spring-boot-cover.jpg"),
			new Image("2", "learning-spring-boot-2nd-edition-cover.jpg"),
			new Image("3", "bazinga.png")
		);
	}
	// end::get[]

	// tag::post[]
	@PostMapping(API_BASE_PATH + "/images")
	Mono<Void> create(@RequestBody Flux<Image> images) {
		return images
			.map(image -> {
				log.info("We will save " + image +
					" to a Reactive database soon!");
				return image;
			})
			.then();
	}
	// end::post[]

}
