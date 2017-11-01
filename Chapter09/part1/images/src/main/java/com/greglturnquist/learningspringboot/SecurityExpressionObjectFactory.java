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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.spring5.context.webflux.ISpringWebFluxContext;
import org.springframework.security.access.expression.SecurityExpressionHandler;

/**
 * @author Greg Turnquist
 */
// tag::code[]
public class SecurityExpressionObjectFactory
		implements IExpressionObjectFactory {

	private final
		SecurityExpressionHandler<MethodInvocation> handler;

	public SecurityExpressionObjectFactory(
		SecurityExpressionHandler<MethodInvocation> handler) {
		this.handler = handler;
	}

	@Override
	public Set<String> getAllExpressionObjectNames() {
		return Collections.unmodifiableSet(
			new HashSet<>(Arrays.asList(
			"authorization"
		)));
	}
	
	@Override
	public boolean isCacheable(String expressionObjectName) {
		return true;
	}

	@Override
	public Object buildObject(IExpressionContext context,
							  String expressionObjectName) {
		if (expressionObjectName.equals("authorization")) {
			if (context instanceof ISpringWebFluxContext) {
				return new Authorization(
					(ISpringWebFluxContext) context, handler);
			}
		}
		return null;
	}
}
// end::code[]
