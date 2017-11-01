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

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

/**
 * @author Greg Turnquist
 */
// tag::comment-service-1[]
@Service
public class CommentService {

	private CommentWriterRepository repository;

	public CommentService(CommentWriterRepository repository) {
		this.repository = repository;
	}
	// end::comment-service-1[]

	// tag::comment-service-2[]
	@RabbitListener(bindings = @QueueBinding(
		value = @Queue,
		exchange = @Exchange(value = "learning-spring-boot"),
		key = "comments.new"
	))
	public void save(Comment newComment) {
		repository
			.save(newComment)
			.log("commentService-save")
			.subscribe();
	}
	// end::comment-service-2[]

	// tag::comment-service-3[]
	@Bean
	Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	// end::comment-service-3[]

	// tag::comment-service-4[]
	@Bean
	CommandLineRunner setUp(MongoOperations operations) {
		return args -> {
			operations.dropCollection(Comment.class);
		};
	}
	// end::comment-service-4[]

}
