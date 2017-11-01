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

import static org.openqa.selenium.chrome.ChromeDriverService.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

/**
 * Autoconfigure a {@link WebDriver} based on what's available, falling back to {@link HtmlUnitDriver}
 * if none other is available.
 *
 * @author Greg Turnquist
 */
// tag::1[]
@Configuration
@ConditionalOnClass(WebDriver.class)
@EnableConfigurationProperties(
	WebDriverConfigurationProperties.class)
@Import({ChromeDriverFactory.class,
	FirefoxDriverFactory.class, SafariDriverFactory.class})
public class WebDriverAutoConfiguration {
// end::1[]

	// tag::2[]
	@Autowired
	WebDriverConfigurationProperties properties;
	// end::2[]

	// tag::3[]
	@Primary
	@Bean(destroyMethod = "quit")
	@ConditionalOnMissingBean(WebDriver.class)
	public WebDriver webDriver(
		FirefoxDriverFactory firefoxDriverFactory,
		SafariDriverFactory safariDriverFactory,
		ChromeDriverFactory chromeDriverFactory) {

		WebDriver driver = firefoxDriverFactory.getObject();

		if (driver == null) {
			driver = safariDriverFactory.getObject();
		}

		if (driver == null) {
			driver = chromeDriverFactory.getObject();
		}

		if (driver == null) {
			driver = new HtmlUnitDriver();
		}

		return driver;
	}
	// end::3[]

	// tag::5[]
	@Bean(destroyMethod = "stop")
	@Lazy
	public ChromeDriverService chromeDriverService() {
		System.setProperty("webdriver.chrome.driver",
			"ext/chromedriver");
		return createDefaultService();
	}
	// end::5[]

}
