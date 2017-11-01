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
package com.greglturnquist.learningspringboot.webdriver;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Data
@ConfigurationProperties("com.greglturnquist.webdriver")
public class WebDriverConfigurationProperties {

	private Firefox firefox = new Firefox();
	private Safari safari = new Safari();
	private Chrome chrome = new Chrome();

	@Data
	static class Firefox {
		private boolean enabled = true;
	}

	@Data
	static class Safari {
		private boolean enabled = true;
	}

	@Data
	static class Chrome {
		private boolean enabled = true;
	}
}
// end::code[]
