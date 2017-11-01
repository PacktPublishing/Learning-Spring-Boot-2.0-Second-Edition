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
package com.greglturnquist.learningspringboot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Component
public class LearningSpringBootHealthIndicator
						implements HealthIndicator {

	@Override
	public Health health() {
		try {
			URL url =
				new URL("http://greglturnquist.com/learning-spring-boot");
			HttpURLConnection conn =
				(HttpURLConnection) url.openConnection();
			int statusCode = conn.getResponseCode();
			if (statusCode >= 200 && statusCode < 300) {
				return Health.up().build();
			} else {
				return Health.down()
					.withDetail("HTTP Status Code", statusCode)
					.build();
			}
		} catch (IOException e) {
			return Health.down(e).build();
		}
	}
}
// end::code[]