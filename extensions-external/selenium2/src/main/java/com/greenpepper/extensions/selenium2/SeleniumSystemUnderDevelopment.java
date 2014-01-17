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

import org.openqa.selenium.WebDriver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.greenpepper.document.Document;
import com.greenpepper.reflect.DefaultFixture;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;

public class SeleniumSystemUnderDevelopment
		extends DefaultSystemUnderDevelopment {

	private Injector injector;
	private WebDriver driver;

	public SeleniumSystemUnderDevelopment(Module... modules) {
		super();

		injector = Guice.createInjector(modules);
	}

	@Override
	public void onStartDocument(Document document) {
		driver = injector.getInstance(WebDriver.class);
	}

	@Override
	public void onEndDocument(Document document) {
		if (driver != null) {
			driver.quit();
		}
	}

	@Override
	public Fixture getFixture(String name, String... params)
			throws Throwable {

		Class<?> klass = loadType(name).getUnderlyingClass();
		Object target = injector.getInstance(klass);
		return new DefaultFixture(target);
	}
}
