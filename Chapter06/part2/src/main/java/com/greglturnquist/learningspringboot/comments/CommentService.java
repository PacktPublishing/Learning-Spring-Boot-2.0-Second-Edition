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
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author Greg Turnquist
 */
@Service
public class CommentService {

	private final CommentWriterRepository repository;

	private final MeterRegistry meterRegistry;

	public CommentService(CommentWriterRepository repository,
						  MeterRegistry meterRegistry) {
		this.repository = repository;
		this.meterRegistry = meterRegistry;
	}

	// tag::code[]
	@RabbitListener(bindings = @QueueBinding(
		value = @Queue,
		exchange = @Exchange(value = "learning-spring-boot"),
		key = "comments.new"
	))
	public void save(Comment newComment) {
		repository
			.save(newComment)
			.log("commentService-save")
			.subscribe(comment -> {
				meterRegistry
					.counter("comments.consumed", "imageId", comment.getImageId())
					.increment();
			});
	}
	// end::code[]

	@Bean
	Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	CommandLineRunner setUp(CommentWriterRepository repository) {
		return args -> {
			repository.deleteAll().subscribe();
		};
	}

}
