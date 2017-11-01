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

import org.aopalliance.intercept.MethodInvocation;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.springframework.security.access.expression.SecurityExpressionHandler;

/**
 * @author Greg Turnquist
 */
// tag::code[]
public class SecurityDialect
	extends AbstractDialect
	implements IExpressionObjectDialect {

	private final
		SecurityExpressionHandler<MethodInvocation> handler;

	public SecurityDialect(
		SecurityExpressionHandler<MethodInvocation> handler) {
		super("Security Dialect");
		this.handler = handler;
	}

	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new SecurityExpressionObjectFactory(handler);
	}
}
// end::code[]