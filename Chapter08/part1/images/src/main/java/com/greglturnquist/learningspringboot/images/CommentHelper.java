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
package com.greglturnquist.learningspringboot.images;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * @author Greg Turnquist
 */
// tag::injection[]
@Component
public class CommentHelper {

	private final RestTemplate restTemplate;

	CommentHelper(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
// end::injection[]

	// tag::get-comments[]
	@HystrixCommand(fallbackMethod = "defaultComments")
	public List<Comment> getComments(Image image) {
		return restTemplate.exchange(
			"http://COMMENTS/comments/{imageId}",
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<Comment>>() {
			},
			image.getId()).getBody();
	}
	// end::get-comments[]

	// tag::fallback[]
	public List<Comment> defaultComments(Image image) {
		return Collections.emptyList();
	}
	// end::fallback[]
}
