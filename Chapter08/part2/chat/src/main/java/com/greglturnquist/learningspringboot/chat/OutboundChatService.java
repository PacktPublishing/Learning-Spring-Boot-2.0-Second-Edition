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

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Service
@EnableBinding(ChatServiceStreams.class)
public class OutboundChatService implements WebSocketHandler {

	private final static Logger log =
		LoggerFactory.getLogger(CommentService.class);

	private Flux<String> flux;
	private FluxSink<String> chatMessageSink;

	public OutboundChatService() {
		this.flux = Flux.<String>create(
			emitter -> this.chatMessageSink = emitter,
			FluxSink.OverflowStrategy.IGNORE)
			.publish()
			.autoConnect();
	}

	@StreamListener(ChatServiceStreams.BROKER_TO_CLIENT)
	public void listen(String message) {
		if (chatMessageSink != null) {
			log.info("Publishing " + message +
				" to websocket...");
			chatMessageSink.next(message);
		}
	}

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		return session
			.send(this.flux
				.map(session::textMessage)
				.log("outbound-wrap-as-websocket-message"))
			.log("outbound-publish-to-websocket");

	}
}
// end::code[]