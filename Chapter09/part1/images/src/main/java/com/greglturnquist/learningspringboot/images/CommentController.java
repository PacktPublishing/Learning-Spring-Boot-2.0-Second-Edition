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

import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Greg Turnquist
 */
@RestController
@EnableBinding(Source.class)
public class CommentController {

	private final MeterRegistry meterRegistry;
	private FluxSink<Message<Comment>> commentSink;
	private Flux<Message<Comment>> flux;

	public CommentController(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
		this.flux = Flux.<Message<Comment>>create(
			emitter -> this.commentSink = emitter,
			FluxSink.OverflowStrategy.IGNORE)
			.publish()
			.autoConnect();
	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/comments")
	public Mono<ResponseEntity<?>> addComment(Mono<Comment> newComment) {
		if (commentSink != null) {
			return newComment
				.map(comment -> {
					System.out.println("Got " + comment);
					commentSink.next(MessageBuilder
						.withPayload(comment)
						.setHeader(MessageHeaders.CONTENT_TYPE,
							MediaType.APPLICATION_JSON_VALUE)
						.build());
					return comment;
				})
				.flatMap(comment -> {
					meterRegistry
						.counter("comments.produced", "imageId", comment.getImageId())
						.increment();
					return Mono.just(ResponseEntity.noContent().build());
				});
		} else {
			return Mono.just(ResponseEntity.noContent().build());
		}
	}

	@StreamEmitter
	@Output(Source.OUTPUT)
	public void emit(FluxSender output) {
		output.send(this.flux);
	}

}
