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

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Greg Turnquist
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTests {

	@Autowired
	WebDriver driver;

	@LocalServerPort
	int port;

	// tag::4[]
	@Test
	public void homePageShouldWork() throws IOException {
		driver.get("http://localhost:" + port);

		assertThat(driver.getTitle())
			.isEqualTo("Learning Spring Boot: Spring-a-Gram");

		String pageContent = driver.getPageSource();

		assertThat(pageContent)
			.contains("<a href=\"/images/bazinga.png/raw\">");

		WebElement element = driver.findElement(
			By.cssSelector("a[href*=\"bazinga.png\"]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().perform();

		driver.navigate().back();
	}
	// end::4[]

}
