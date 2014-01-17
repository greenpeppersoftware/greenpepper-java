/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
package com.greenpepper.extensions.grails;

import org.jmock.Expectations;
import org.jmock.Mockery;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.greenpepper.reflect.Fixture;

public class GrailsSystemUnderDevelopmentTest
{
	private GrailsSystemUnderDevelopment sud;

	@Before
	public void setUp()
	{
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();

		BeanDefinition bookServiceDef = new RootBeanDefinition(BookService.class, RootBeanDefinition.AUTOWIRE_AUTODETECT);
		bf.registerBeanDefinition("bookService", bookServiceDef);

		BeanDefinition bookFixtureDef = new RootBeanDefinition(BookFixture.class, RootBeanDefinition.AUTOWIRE_AUTODETECT);
		bf.registerBeanDefinition("bookFixture", bookFixtureDef);

		sud = new GrailsSystemUnderDevelopment(bf);
		sud.addImport(getClass().getPackage().getName());
	}

	@Test
	public void shouldFindFixtureWithMixedCapitalization()
			throws Throwable
	{
		Fixture fixture = sud.getFixture("BookFixture");
		assertTargetNotNull(fixture);

		fixture = sud.getFixture("bookFixture");
		assertTargetNotNull(fixture);
	}

	@Test
	public void serviceShouldBeInjected()
			throws Throwable
	{
		Fixture fixture = sud.getFixture("bookFixture");
		
		BookFixture bookFixture = (BookFixture)fixture.getTarget();
		assertNotNull(bookFixture.getBookService());
	}

	@Test
	public void autoWiringService()
			throws Throwable
	{
		Fixture fixture = sud.getFixture("StoreService");

		StoreService storeService = (StoreService)fixture.getTarget();
		assertNotNull(storeService.getBookService());
	}

	@Test
	public void havingSpaceInFixtureName()
			throws Throwable
	{
		Fixture fixture = sud.getFixture("book Fixture");
		assertTargetNotNull(fixture);
	}

	@Test
	public void withoutFixtureSuffix()
			throws Throwable
	{
		Fixture fixture = sud.getFixture("Book");
		assertTargetNotNull(fixture);
	}

	@Test
	public void processAllFixtureNameResolution()
			throws Throwable
	{
		final Mockery context = new Mockery();
		final BeanFactory beanFactory = context.mock(BeanFactory.class);

		sud = new GrailsSystemUnderDevelopment(beanFactory);
		sud.addImport(getClass().getPackage().getName());

		context.checking(new Expectations()
		{{
			    one(beanFactory).containsBean("aFixture");
				will(returnValue(false));
				one(beanFactory).containsBean("AFixture");
				will(returnValue(false));
				one(beanFactory).containsBean("a");
				will(returnValue(false));
				one(beanFactory).containsBean("A");
				will(returnValue(true));
				one(beanFactory).getBean("A");
				will(returnValue(new BookService()));
		}});

		Fixture fixture = sud.getFixture("A");
		assertTargetNotNull(fixture);
		assertTrue(fixture.getTarget() instanceof BookService);

		context.assertIsSatisfied();
	}

	private void assertTargetNotNull(Fixture fixture)
	{
		Object target = fixture.getTarget();
		assertNotNull(target);
	}
}