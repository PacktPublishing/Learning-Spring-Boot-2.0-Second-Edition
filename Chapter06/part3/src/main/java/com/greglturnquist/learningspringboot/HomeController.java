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
package com.greglturnquist.learningspringboot;

import java.io.IOException;
import java.util.HashMap;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.greglturnquist.learningspringboot.images.CommentReaderRepository;
import com.greglturnquist.learningspringboot.images.ImageService;

/**
 * @author Greg Turnquist
 */
@Controller
public class HomeController {

	private static final String BASE_PATH = "/images";
	private static final String FILENAME = "{filename:.+}";

	private final ImageService imageService;
	private final CommentReaderRepository repository;

	public HomeController(ImageService imageService,
						  CommentReaderRepository repository) {
		this.imageService = imageService;
		this.repository = repository;
	}

	// tag::index[]
	@GetMapping("/")
	public Mono<String> index(Model model) {
		model.addAttribute("images",
			imageService
				.findAllImages()
				.flatMap(image ->
					Mono.just(image)
						.zipWith(repository.findByImageId(
							image.getId()).collectList()))
				.map(imageAndComments -> new HashMap<String, Object>(){{
					put("id", imageAndComments.getT1().getId());
					put("name", imageAndComments.getT1().getName());
					put("comments",
						imageAndComments.getT2());
				}}));
		model.addAttribute("extra",
			"DevTools can also detect code changes too");
		return Mono.just("index");
	}
	// end::index[]

	@GetMapping(value = BASE_PATH + "/" + FILENAME + "/raw",
		produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public Mono<ResponseEntity<?>> oneRawImage(
		@PathVariable String filename) {
		// tag::try-catch[]
		return imageService.findOneImage(filename)
			.map(resource -> {
				try {
					return ResponseEntity.ok()
						.contentLength(resource.contentLength())
						.body(new InputStreamResource(
							resource.getInputStream()));
				} catch (IOException e) {
					return ResponseEntity.badRequest()
						.body("Couldn't find " + filename +
							" => " + e.getMessage());
				}
			});
		// end::try-catch[]
	}

	@PostMapping(value = BASE_PATH)
	public Mono<String> createFile(@RequestPart(name = "file")
									   Flux<FilePart> files) {
		return imageService.createImage(files)
			.then(Mono.just("redirect:/"));
	}

	@DeleteMapping(BASE_PATH + "/" + FILENAME)
	public Mono<String> deleteFile(@PathVariable String filename) {
		return imageService.deleteImage(filename)
			.then(Mono.just("redirect:/"));
	}
}