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

import java.time.Duration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Greg Turnquist
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImageServiceTests.TestConfig.class)
@DataMongoTest
public class ImageServiceTests {

	@Autowired
	ImageRepository repository;

	@Autowired
	ImageService imageService;

	@Autowired
	MongoOperations operations;

	@Before
	public void setUp() {
		operations.dropCollection(Image.class);

		operations.insert(new Image("1",
			"learning-spring-boot-cover.jpg"));
		operations.insert(new Image("2",
			"learning-spring-boot-2nd-edition-cover.jpg"));
		operations.insert(new Image("3",
			"bazinga.png"));
	}

	@Test
	public void findAllImages() {
		imageService.findAllImages().collectList().block(Duration.ofSeconds(30));
	}

	@Test
	public void findOneImage() {
		imageService.findOneImage("learning-spring-boot.png").block(Duration.ofSeconds(30));
	}

	@Test
	public void deleteImage() {
		imageService.deleteImage("learning-spring-boot.png").block(Duration.ofSeconds(30));
	}

	@Configuration
	@EnableReactiveMongoRepositories(basePackageClasses = ImageRepository.class)
	static class TestConfig {

		@Bean
		public ImageService imageService(ResourceLoader resourceLoader, ImageRepository imageRepository) {
			return new ImageService(resourceLoader, imageRepository);
		}

	}

}
