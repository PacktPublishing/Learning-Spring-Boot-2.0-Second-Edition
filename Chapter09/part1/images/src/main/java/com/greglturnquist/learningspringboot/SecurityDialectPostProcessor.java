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
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.stereotype.Component;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Component
public class SecurityDialectPostProcessor
	implements BeanPostProcessor, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(
				ApplicationContext applicationContext)
		throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object postProcessBeforeInitialization(
		Object bean, String beanName) throws BeansException {
		if (bean instanceof SpringTemplateEngine) {
			SpringTemplateEngine engine =
						(SpringTemplateEngine) bean;
			SecurityExpressionHandler<MethodInvocation> handler =
				applicationContext.getBean(
								SecurityExpressionHandler.class);
			SecurityDialect dialect = new SecurityDialect(handler);
			engine.addDialect(dialect);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(
		Object bean, String beanName) throws BeansException {
		return bean;
	}
}
// end::code[]
