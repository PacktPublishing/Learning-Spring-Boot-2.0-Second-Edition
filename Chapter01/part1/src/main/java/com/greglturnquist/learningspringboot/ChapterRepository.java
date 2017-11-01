package com.greglturnquist.learningspringboot;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChapterRepository
	extends ReactiveCrudRepository<Chapter, String> {

}
