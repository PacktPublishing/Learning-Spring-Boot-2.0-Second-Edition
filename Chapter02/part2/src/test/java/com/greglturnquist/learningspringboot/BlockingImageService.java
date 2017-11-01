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
import java.util.List;

import reactor.core.publisher.Flux;

import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;

/**
 * @author Greg Turnquist
 */
// tag::1[]
public class BlockingImageService {

	private final ImageService imageService;

	public BlockingImageService(ImageService imageService) {
		this.imageService = imageService;
	}
	// end::1[]

	// tag::2[]
	public List<Image> findAllImages() {
		return imageService.findAllImages()
			.collectList()
			.block(Duration.ofSeconds(10));
	}
	// end::2[]

	// tag::3[]
	public Resource findOneImage(String filename) {
		return imageService.findOneImage(filename)
			.block(Duration.ofSeconds(30));
	}
	// end::3[]

	// tag::4[]
	public void createImage(List<FilePart> files) {
		imageService.createImage(Flux.fromIterable(files))
			.block(Duration.ofMinutes(1));
	}
	// end::4[]

	// tag::5[]
	public void deleteImage(String filename) {
		imageService.deleteImage(filename)
			.block(Duration.ofSeconds(20));
	}
	// end::6[]
}
