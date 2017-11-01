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
package com.greglturnquist.learningspringboot;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Greg Turnquist
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QueryTests.TestConfig.class)
@DataMongoTest
public class QueryTests {

	@Autowired
	EmployeeRepository repository;

	// tag::inject-template[]
	@Autowired
	ReactiveMongoOperations operations;
	// end::inject-template[]

	@Autowired
	MongoOperations mongoOperations;

	@Before
	public void setUp() {
		// tag::setup[]
		mongoOperations.dropCollection(Employee.class);

		Employee e1 = new Employee();
		e1.setId(UUID.randomUUID().toString());
		e1.setFirstName("Bilbo");
		e1.setLastName("Baggins");
		e1.setRole("burglar");

		mongoOperations.insert(e1);

		Employee e2 = new Employee();
		e2.setId(UUID.randomUUID().toString());
		e2.setFirstName("Frodo");
		e2.setLastName("Baggins");
		e2.setRole("ring bearer");
		
		mongoOperations.insert(e2);
		// end::setup[]
	}

	@Test
	public void testSingle() {
		// tag::example-mono[]
		Employee e = new Employee();
		e.setFirstName("Bilbo");
		Example<Employee> example = Example.of(e);
		// end::example-mono[]

		// tag::query-mono[]
		Mono<Employee> singleEmployee = repository.findOne(example);
		// end::query-mono[]

		StepVerifier.create(singleEmployee)
			.expectNextMatches(employee -> {
				assertThat(employee).hasNoNullFieldsOrProperties();
				assertThat(employee.getFirstName()).isEqualTo("Bilbo");
				assertThat(employee.getLastName()).isEqualTo("Baggins");
				assertThat(employee.getRole()).isEqualTo("burglar");
				return true;
			})
			.expectComplete()
			.verify();
	}

	@Test
	public void testMultiple() {
		// tag::example-flux[]
		Employee e = new Employee();
		e.setLastName("baggins"); // Lowercase lastName

		ExampleMatcher matcher = ExampleMatcher.matching()
			.withIgnoreCase()
			.withMatcher("lastName", startsWith())
			.withIncludeNullValues();

		Example<Employee> example = Example.of(e, matcher);
		// end::example-flux[]

		// tag::query-flux[]
		Flux<Employee> multipleEmployees = repository.findAll(example);
		// end::query-flux[]

		StepVerifier.create(multipleEmployees.collectList())
			.expectNextMatches(employees -> {
				assertThat(employees).hasSize(2);
				assertThat(employees).extracting("firstName")
					.contains("Frodo", "Bilbo");
				return true;
			})
			.expectComplete()
			.verify();
	}

	@Test
	public void testSingleWithTemplate() {
		// tag::mono-template[]
		Employee e = new Employee();
		e.setFirstName("Bilbo");
		Example<Employee> example = Example.of(e);

		Mono<Employee> singleEmployee = operations.findOne(
			new Query(byExample(example)), Employee.class);
		// end::mono-template[]

		StepVerifier.create(singleEmployee)
			.expectNextMatches(employee -> {
				assertThat(employee).hasNoNullFieldsOrProperties();
				assertThat(employee.getFirstName()).isEqualTo("Bilbo");
				assertThat(employee.getLastName()).isEqualTo("Baggins");
				assertThat(employee.getRole()).isEqualTo("burglar");
				return true;
			})
			.expectComplete()
			.verify();
	}

	@Test
	public void testMultipleWithTemplate() {
		// tag::flux-template[]
		Employee e = new Employee();
		e.setLastName("baggins"); // Lowercase lastName

		ExampleMatcher matcher = ExampleMatcher.matching()
			.withIgnoreCase()
			.withMatcher("lastName", startsWith())
			.withIncludeNullValues();

		Example<Employee> example = Example.of(e, matcher);

		Flux<Employee> multipleEmployees = operations.find(
			new Query(byExample(example)), Employee.class);
		// end::flux-template[]

		StepVerifier.create(multipleEmployees.collectList())
			.expectNextMatches(employees -> {
				assertThat(employees).hasSize(2);
				assertThat(employees).extracting("firstName")
					.contains("Frodo", "Bilbo");
				return true;
			})
			.expectComplete()
			.verify();
	}

	@Configuration
	@EnableReactiveMongoRepositories(basePackageClasses = EmployeeRepository.class)
	static class TestConfig {

	}
}
