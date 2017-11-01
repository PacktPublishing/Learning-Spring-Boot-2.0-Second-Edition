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

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Greg Turnquist
 */
public class ExampleTests {

	@Test
	public void data1() {
		// tag::1[]
		Flux.just("alpha", "bravo", "charlie");
		// end::1[]
	}

	@Test
	public void data2() {
		// tag::2[]
		String[] items = new String[]{"alpha", "bravo", "charlie"};
		Flux.fromArray(items);
		// end::2[]
	}

	@Test
	public void data3() {
		// tag::3[]
		List<String> items = Arrays.asList("alpha", "bravo", "charlie");
		Flux.fromIterable(items);
		// end::3[]
	}

	@Test
	public void data4() {
		// tag::4[]
		Stream<String> items = Arrays.asList("alpha", "bravo", "charlie").stream();
		Flux.fromStream(items);
		// end::4[]
	}

	@Test
	public void data5() {
		// tag::5[]
		Flux.just("alpha", "bravo", "charlie")
			.subscribe(System.out::println);
		// end::5[]
	}

	@Test
	public void data6() {
		// tag::6[]
		Flux.just("alpha", "bravo", "charlie")
			.map(String::toUpperCase)
			.flatMap(s -> Flux.fromArray(s.split("")))
			.groupBy(String::toString)
			.sort((o1, o2) -> o1.key().compareTo(o2.key()))
			.flatMap(group -> Mono.just(group.key()).zipWith(group.count()))
			.map(keyAndCount ->
				keyAndCount.getT1() + " => " + keyAndCount.getT2())
			.subscribe(System.out::println);
		// end::6[]
	}

	@Test
	public void data7() {
		// tag::7[]
		Flux.just(
			(Supplier<String>) () -> "alpha",
			(Supplier<String>) () -> "bravo",
			(Supplier<String>) () -> "charlie")
			.subscribe(supplier ->
				System.out.println(supplier.get()));
		// end::7[]
	}

}
