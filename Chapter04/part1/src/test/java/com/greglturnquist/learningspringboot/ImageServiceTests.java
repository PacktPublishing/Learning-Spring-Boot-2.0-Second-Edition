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

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;

/**
 * @author Greg Turnquist
 */
// tag::1[]
@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageServiceTests {

	@Autowired
	ImageService imageService;

	@MockBean
	ImageRepository repository;
	// end::1[]

	@MockBean(name = "setUp")
	Object ignoreImageServiceCommandLineRunner;

	@Before
	public void setUp() throws IOException {
		FileSystemUtils.deleteRecursively(new File(ImageService.UPLOAD_ROOT));
		Files.createDirectory(Paths.get(ImageService.UPLOAD_ROOT));
	}

	@Test
	public void findAllShouldJustReturnTheFlux() {
		// given
		given(repository.findAll()).willReturn(
			Flux.just(
				new Image("1", "alpha.jpg"),
				new Image("2", "bravo.jpg")
			)
		);

		// when
		Flux<Image> images = imageService.findAllImages();

		// then
		then(images).isNotNull();

		StepVerifier.create(images)
			.recordWith(ArrayList::new)
			.expectNextCount(2)
			.consumeRecordedWith(results -> {
				then(results)
					.extracting(Image::getId, Image::getName)
					.contains(
						tuple("1", "alpha.jpg"),
						tuple("2", "bravo.jpg")
					);
			})
			.verifyComplete();
	}

	@Test
	public void findOneShouldReturnNotYetFetchedUrl() {
		// when
		Mono<Resource> image = imageService.findOneImage("alpha.jpg");

		// then
		then(image).isNotNull();

		StepVerifier.create(image)
			.expectNextMatches(resource -> {
				then(resource.getDescription()).isEqualTo("URL [file:upload-dir/alpha.jpg]");
				then(resource.exists()).isFalse();
				then(resource.getClass()).isEqualTo(UrlResource.class);
				return true;
			})
			.verifyComplete();
	}

	@Test
	public void createImageShouldWork() {
		// given
		Image alphaImage = new Image("1", "alpha.jpg");
		Image bravoImage = new Image("2", "bravo.jpg");
		given(repository.save(new Image(any(), alphaImage.getName()))).willReturn(Mono.just(alphaImage));
		given(repository.save(new Image(any(), bravoImage.getName()))).willReturn(Mono.just(bravoImage));
		given(repository.findAll()).willReturn(Flux.just(alphaImage, bravoImage));
		FilePart file1 = mock(FilePart.class);
		given(file1.filename()).willReturn(alphaImage.getName());
		given(file1.transferTo(any())).willReturn(Mono.empty());
		FilePart file2 = mock(FilePart.class);
		given(file2.filename()).willReturn(bravoImage.getName());
		given(file2.transferTo(any())).willReturn(Mono.empty());

		// when
		Mono<Void> done = imageService.createImage(Flux.just(file1, file2));

		// then
		StepVerifier.create(done)
			.verifyComplete();
	}

	@Test
	public void deleteImageShouldWork() {
		// given
		String imageName = "alpha.jpg";
		Mono<Image> image = Mono.just(new Image("1", imageName));
		given(repository.findByName(any())).willReturn(image);
		given(repository.delete((Image) any())).willReturn(Mono.empty());

		// when
		Mono<Void> done = imageService.deleteImage(imageName);

		// then
		StepVerifier.create(done)
			.verifyComplete();
	}

}
