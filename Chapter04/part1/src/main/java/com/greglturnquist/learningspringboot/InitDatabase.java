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

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

/**
 * @author Greg Turnquist
 */
@Component
public class InitDatabase {
	@Bean
	CommandLineRunner init(MongoOperations operations) {
		return args -> {
			// tag::log[]
			operations.dropCollection(Image.class);

			operations.insert(new Image("1",
				"learning-spring-boot-cover.jpg"));
			operations.insert(new Image("2",
				"learning-spring-boot-2nd-edition-cover.jpg"));
			operations.insert(new Image("3",
				"bazinga.png"));

			operations.findAll(Image.class).forEach(image -> {
				System.out.println(image.toString());
			});
			// end::log[]
		};
	}
}