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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.spring5.context.webflux.ISpringWebFluxContext;

import org.springframework.expression.ParseException;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.util.SimpleMethodInvocation;

/**
 * @author Greg Turnquist
 */
// tag::part1[]
public class Authorization {

	private static final Logger log =
		LoggerFactory.getLogger(Authorization.class);

	private ISpringWebFluxContext context;
	private SecurityExpressionHandler<MethodInvocation> handler;

	public Authorization(ISpringWebFluxContext context,
		SecurityExpressionHandler<MethodInvocation> handler) {
		this.context = context;
		this.handler = handler;
	}
	// end::part1[]

	// tag::part2[]
	public boolean expr(String accessExpression) {
		Authentication authentication =
			(Authentication) this.context.getExchange()
								.getPrincipal().block();

		log.debug("Checking if user \"{}\" meets expr \"{}\".",
			new Object[] {
				(authentication == null ?
					null : authentication.getName()),
				accessExpression});
		
		/*
		 * In case this expression is specified as a standard
		 * variable expression (${...}), clean it.
		 */
		String expr =
			((accessExpression != null
				&&
				accessExpression.startsWith("${")
				&&
				accessExpression.endsWith("}")) ?

				accessExpression.substring(2,
					accessExpression.length()-1) :
				accessExpression);

		try {
			if (ExpressionUtils.evaluateAsBoolean(
				handler.getExpressionParser().parseExpression(expr),
				handler.createEvaluationContext(authentication,
					new SimpleMethodInvocation()))) {

				log.debug("Checked \"{}\" for user \"{}\". " +
						"Access GRANTED",
					new Object[] {
						accessExpression,
						(authentication == null ?
							null : authentication.getName())});

				return true;
			} else {
				log.debug("Checked \"{}\" for user \"{}\". " +
						"Access DENIED",
					new Object[] {
						accessExpression,
						(authentication == null ?
							null : authentication.getName())});

				return false;
			}
		} catch (ParseException e) {
			throw new TemplateProcessingException(
				"An error happened parsing \"" + expr + "\"", e);
		}
	}
	// end::part2[]
}
