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
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * @author Greg Turnquist
 */
// tag::code-1[]
@Service
@EnableBinding(ChatServiceStreams.class)
public class OutboundChatService
				extends UserParsingHandshakeHandler {
	//end::code-1[]
	
	private final static Logger log =
		LoggerFactory.getLogger(CommentService.class);

	private Flux<Message<String>> flux;
	private FluxSink<Message<String>> chatMessageSink;

	public OutboundChatService() {
		this.flux = Flux.<Message<String>>create(
			emitter -> this.chatMessageSink = emitter,
			FluxSink.OverflowStrategy.IGNORE)
			.publish()
			.autoConnect();
	}

	@StreamListener(ChatServiceStreams.BROKER_TO_CLIENT)
	public void listen(Message<String> message) {
		if (chatMessageSink != null) {
			log.info("Publishing " + message +
				" to websocket...");
			chatMessageSink.next(message);
		}
	}

	// tag::code-2[]
	@Override
	protected Mono<Void> handleInternal(WebSocketSession session) {
		return session
			.send(this.flux
				.filter(s -> validate(s, getUser(session.getId())))
				.map(this::transform)
				.map(session::textMessage)
				.log(getUser(session.getId()) +
					"-outbound-wrap-as-websocket-message"))
			.log(getUser(session.getId()) +
				"-outbound-publish-to-websocket");
	}
	// end::code-2[]

	// tag::code-3[]
	private boolean validate(Message<String> message, String user) {
		if (message.getPayload().startsWith("@")) {
			String targetUser = message.getPayload()
				.substring(1, message.getPayload().indexOf(" "));

			String sender = message.getHeaders()
				.get(ChatServiceStreams.USER_HEADER, String.class);
			
			return user.equals(sender) || user.equals(targetUser);
		} else {
			return true;
		}
	}
	// end::code-3[]

	// tag::code-4[]
	private String transform(Message<String> message) {
		String user = message.getHeaders()
			.get(ChatServiceStreams.USER_HEADER, String.class);
		if (message.getPayload().startsWith("@")) {
			return "(" + user + "): " + message.getPayload();
		} else {
			return "(" + user + ")(all): " + message.getPayload();
		}
	}
	// end::code-4[]
}
