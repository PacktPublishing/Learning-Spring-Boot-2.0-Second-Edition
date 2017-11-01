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
import reactor.core.publisher.Mono;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author Greg Turnquist
 */
// tag::stream-1[]
@Service
@EnableBinding(CustomProcessor.class)
public class CommentService {
	// end::stream-1[]

	private final CommentWriterRepository repository;

	private final MeterRegistry meterRegistry;

	public CommentService(CommentWriterRepository repository,
						  MeterRegistry meterRegistry) {
		this.repository = repository;
		this.meterRegistry = meterRegistry;
	}

	// tag::stream-2[]
	@StreamListener
	@Output(CustomProcessor.OUTPUT)
	public Flux<Void> save(@Input(CustomProcessor.INPUT)
						   Flux<Comment> newComments) {
		return repository
			.saveAll(newComments)
			.flatMap(comment -> {
				meterRegistry
					.counter("comments.consumed", "imageId", comment.getImageId())
					.increment();
				return Mono.empty();
			});
	}
	// end::stream-2[]

	@Bean
	CommandLineRunner setUp(CommentWriterRepository repository) {
		return args -> {
			repository.deleteAll().subscribe();
		};
	}

}
