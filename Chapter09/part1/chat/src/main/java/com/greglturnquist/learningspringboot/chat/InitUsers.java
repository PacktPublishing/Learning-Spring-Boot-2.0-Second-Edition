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
package com.greglturnquist.learningspringboot.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Configuration
public class InitUsers {

	@Bean
	CommandLineRunner initializeUsers(MongoOperations operations) {
		return args -> {
			operations.dropCollection(User.class);

			operations.insert(
				new User(
					null,
					"greg", "turnquist",
					new String[]{"ROLE_USER", "ROLE_ADMIN"}));
			operations.insert(
				new User(
					null,
					"phil", "webb",
					new String[]{"ROLE_USER"}));

			operations.findAll(User.class).forEach(user -> {
				System.out.println("Loaded " + user);
			});
		};
	}
}
// end::code[]