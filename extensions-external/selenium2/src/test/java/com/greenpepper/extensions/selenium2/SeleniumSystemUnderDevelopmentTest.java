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
package com.greenpepper.extensions.selenium2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;

import com.greenpepper.reflect.Fixture;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SeleniumSystemUnderDevelopmentTest {

	@Mock private WebDriver driver;

	private SeleniumSystemUnderDevelopment sud;

	private MySeleniumFixture mySeleniumFixture;

	@Before
	public void init()
			throws Throwable {

		sud = new SeleniumSystemUnderDevelopment(new WebDriverTestModule(driver));
		sud.addImport(MySeleniumFixture.class.getPackage().getName());

		Fixture fixture = sud.getFixture("My Selenium");
		mySeleniumFixture = (MySeleniumFixture)fixture.getTarget();
	}

	@Test
	public void webDriverShouldBeInjectedInTheFixture() {

		assertTrue(mySeleniumFixture.isDriverReady());
	}

	@Test
	public void verifyDriverLifecycle() {

		sud.onStartDocument(null);
		sud.onEndDocument(null);

		verify(driver).quit();
		verifyNoMoreInteractions(driver);
	}
}
