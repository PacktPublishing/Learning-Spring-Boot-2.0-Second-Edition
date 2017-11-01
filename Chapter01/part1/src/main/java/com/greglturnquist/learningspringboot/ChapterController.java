package com.greglturnquist.learningspringboot;

import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChapterController {

	private final ChapterRepository repository;

	public ChapterController(ChapterRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/chapters")
	public Flux<Chapter> listing() {
		return repository.findAll();
	}
}
