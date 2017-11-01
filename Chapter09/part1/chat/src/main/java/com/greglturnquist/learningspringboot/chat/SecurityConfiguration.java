/*
 * Copyright 2016 the original author or authors.
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

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.HttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@EnableWebFluxSecurity
public class SecurityConfiguration {

	// tag::security-filter-chain[]
	@Bean
	SecurityWebFilterChain springWebFilterChain(HttpSecurity http) {
		return http
			.authorizeExchange()
				.pathMatchers("/**").authenticated()
				.and()
			.build();
	}
	// end::security-filter-chain[]
}
// end::code[]
