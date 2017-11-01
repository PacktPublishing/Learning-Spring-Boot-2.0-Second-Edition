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

import reactor.core.publisher.Mono;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Service
@EnableBinding(ChatServiceStreams.class)
public class InboundChatService implements WebSocketHandler {

	private final ChatServiceStreams chatServiceStreams;

	public InboundChatService(ChatServiceStreams chatServiceStreams) {
		this.chatServiceStreams = chatServiceStreams;
	}

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		return session
			.receive()
			.log("inbound-incoming-chat-message")
			.map(WebSocketMessage::getPayloadAsText)
			.log("inbound-convert-to-text")
			.map(s -> session.getId() + ": " + s)
			.log("inbound-mark-with-session-id")
			.flatMap(this::broadcast)
			.log("inbound-broadcast-to-broker")
			.then();
	}
	
	public Mono<?> broadcast(String message) {
		return Mono.fromRunnable(() -> {
			chatServiceStreams.clientToBroker().send(
					MessageBuilder
							.withPayload(message)
							.build());
		});
	}
}
// end::code[]
