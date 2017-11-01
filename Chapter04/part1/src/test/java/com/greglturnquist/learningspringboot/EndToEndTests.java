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
import static org.openqa.selenium.chrome.ChromeDriverService.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

/**
 * @author Greg Turnquist
 */
// tag::1[]
@RunWith(SpringRunner.class)
@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTests {
	// end::1[]

	// tag::2[]
	static ChromeDriverService service;

	static ChromeDriver driver;

	@LocalServerPort
	int port;
	// end::2[]

	// tag::3[]
	@BeforeClass
	public static void setUp() throws IOException {
		System.setProperty("webdriver.chrome.driver",
			"ext/chromedriver");
		service = createDefaultService();
		driver = new ChromeDriver(service);
		Path testResults = Paths.get("build", "test-results");
		if (!Files.exists(testResults)) {
			Files.createDirectory(testResults);
		}
	}

	@AfterClass
	public static void tearDown() {
		service.stop();
	}
	// end::3[]

	// tag::4[]
	@Test
	public void homePageShouldWork() throws IOException {
		driver.get("http://localhost:" + port);

		takeScreenshot("homePageShouldWork-1");

		assertThat(driver.getTitle())
			.isEqualTo("Learning Spring Boot: Spring-a-Gram");

		String pageContent = driver.getPageSource();

		assertThat(pageContent)
			.contains("<a href=\"/images/bazinga.png/raw\">");

		WebElement element = driver.findElement(
			By.cssSelector("a[href*=\"bazinga.png\"]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().perform();

		takeScreenshot("homePageShouldWork-2");

		driver.navigate().back();
	}
	// end::4[]

	// tag::5[]
	private void takeScreenshot(String name) throws IOException {
		FileCopyUtils.copy(
			driver.getScreenshotAs(OutputType.FILE),
			new File("build/test-results/TEST-" + name + ".png"));
	}
	// end::5[]
}
