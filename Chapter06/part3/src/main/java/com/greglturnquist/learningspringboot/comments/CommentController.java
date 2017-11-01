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
package com.greglturnquist.learningspringboot.comments;

import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Controller
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

	@PostMapping("/comments")
	public Mono<String> addComment(Mono<Comment> newComment) {
		if (commentSink != null) {
			return newComment
				.map(comment -> commentSink.next(MessageBuilder
					.withPayload(comment)
					.build()))
				.then(Mono.just("redirect:/"));
		} else {
			return Mono.just("redirect:/");
		}
	}

	@StreamEmitter
	public void emit(@Output(Source.OUTPUT) FluxSender output) {
		output.send(this.flux);
	}

}
// end::code[]