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
package com.greglturnquist.learningspringboot.chat;

import reactor.core.publisher.Mono;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsRepository;
import org.springframework.stereotype.Component;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Component
public class SpringDataUserDetailsRepository implements UserDetailsRepository {

	private final UserRepository repository;

	public SpringDataUserDetailsRepository(UserRepository repository)
	{
		this.repository = repository;
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return repository.findByUsername(username)
			.map(user -> new User(
				user.getUsername(),
				user.getPassword(),
				AuthorityUtils.createAuthorityList(user.getRoles())
			));
	}
}
// end::code[]
