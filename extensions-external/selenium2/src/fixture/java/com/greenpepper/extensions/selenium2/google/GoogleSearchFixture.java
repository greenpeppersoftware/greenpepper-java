/**
 * Copyright (c) 2010 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.greenpepper.extensions.selenium2.google;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.google.inject.Inject;
import com.greenpepper.interpreter.flow.scenario.Check;
import com.greenpepper.interpreter.flow.scenario.Expectation;
import com.greenpepper.interpreter.flow.scenario.Given;
import com.greenpepper.interpreter.flow.scenario.Then;
import com.greenpepper.interpreter.flow.scenario.When;

public class GoogleSearchFixture {

	private final WebDriver driver;

	private GoogleSearchPage searchPage;

	@Inject
	public GoogleSearchFixture(WebDriver driver) {
		this.driver = driver;
	}

	@Given("I am on http://www.google.com")
	public void openGooglePage() {
		searchPage = PageFactory.initElements(driver, GoogleSearchPage.class);
	}

	@Check("The title is \"(.*)\"")
	public boolean checkTitle(String title) {
		return driver.getTitle().equals(title);
	}

	@When("I type \"(.*)\" in the search box")
	public void setCriteria(String value) {
		searchPage.setCriteria(value);
	}

	@When("I submit the search")
	public void submit() {
		searchPage.submit();
	}

	@Then("The first result is \"(.*)\"")
	public void verifyFirstResult(Expectation firstResultExpected) {
		GoogleSearchResultPage searchResultPage = PageFactory.initElements(driver, GoogleSearchResultPage.class);
		firstResultExpected.setActual(searchResultPage.getFirstResult());
	}

	@Then("Show me the page")
	public void showMeThePage() {
		System.err.println(driver.getPageSource());
	}
}
