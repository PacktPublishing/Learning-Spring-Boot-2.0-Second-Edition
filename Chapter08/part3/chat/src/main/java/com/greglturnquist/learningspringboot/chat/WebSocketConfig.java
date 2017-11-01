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

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

/**
 * @author Greg Turnquist
 */
// tag::websocket-1[]
@Configuration
public class WebSocketConfig {
// end::websocket-1[]

	@Bean
	HandlerMapping webSocketMapping(CommentService commentService,
						InboundChatService inboundChatService,
						OutboundChatService outboundChatService) {
		Map<String, WebSocketHandler> urlMap = new HashMap<>();
		urlMap.put("/topic/comments.new", commentService);
		urlMap.put("/app/chatMessage.new", inboundChatService);
		urlMap.put("/topic/chatMessage.new", outboundChatService);

		// tag::cors[]
		Map<String, CorsConfiguration> corsConfigurationMap =
			new HashMap<>();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("http://localhost:8080");
		corsConfigurationMap.put(
			"/topic/comments.new", corsConfiguration);
		corsConfigurationMap.put(
			"/app/chatMessage.new", corsConfiguration);
		corsConfigurationMap.put(
			"/topic/chatMessage.new", corsConfiguration);
		// end::cors[]

		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(10);
		mapping.setUrlMap(urlMap);
		mapping.setCorsConfigurations(corsConfigurationMap);

		return mapping;
	}

	@Bean
	WebSocketHandlerAdapter handlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
}
