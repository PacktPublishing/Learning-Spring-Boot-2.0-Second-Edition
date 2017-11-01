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
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.tuple.Tuple;
import org.springframework.web.server.WebSession;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Configuration
public class GatewayConfig {

	private static final Logger log =
		LoggerFactory.getLogger(GatewayConfig.class);

	/**
	 * Force the current WebSession to get saved
	 */
	static class SaveSessionGatewayFilterFactory
								implements GatewayFilterFactory {
		@Override
		public GatewayFilter apply(Tuple args) {
			return (exchange, chain) -> exchange.getSession()
				.map(webSession -> {
					log.debug("Session id: " + webSession.getId());
					webSession.getAttributes().entrySet()
						.forEach(entry ->
						log.debug(entry.getKey() + " => " +
											entry.getValue()));
					return webSession;
				})
				.map(WebSession::save)
				.then(chain.filter(exchange));
		}
	}

	@Bean
	SaveSessionGatewayFilterFactory saveSessionWebFilterFactory() {
		return new SaveSessionGatewayFilterFactory();
	}
}
// end::code[]
