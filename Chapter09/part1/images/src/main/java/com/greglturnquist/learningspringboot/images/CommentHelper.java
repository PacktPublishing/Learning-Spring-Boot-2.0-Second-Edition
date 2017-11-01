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

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.greglturnquist.learningspringboot.ImagesConfiguration;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @author Greg Turnquist
 */
@Component
public class CommentHelper {

	private final RestTemplate restTemplate;

	private final ImagesConfiguration imagesConfiguration;

	CommentHelper(RestTemplate restTemplate,
				  ImagesConfiguration imagesConfiguration) {
		this.restTemplate = restTemplate;
		this.imagesConfiguration = imagesConfiguration;
	}

	// tag::get-comments[]
	@HystrixCommand(fallbackMethod = "defaultComments")
	public List<Comment> getComments(Image image, String sessionId) {

		ResponseEntity<List<Comment>> results = restTemplate.exchange(
			"http://COMMENTS/comments/{imageId}",
			HttpMethod.GET,
			new HttpEntity<>(new HttpHeaders() {{
				String credentials = imagesConfiguration.getCommentsUser() + ":" +
					imagesConfiguration.getCommentsPassword();
				String token = new String(Base64Utils.encode(credentials.getBytes()));
				set(AUTHORIZATION, "Basic " + token);
				set("Cookie", "SESSION=" + sessionId);
			}}),
			new ParameterizedTypeReference<List<Comment>>() {},
			image.getId());
		return results.getBody();
	}
	// end::get-comments[]

	// tag::fallback[]
	public List<Comment> defaultComments(Image image, String sessionId) {
		return Collections.emptyList();
	}
	// end::fallback[]
}
