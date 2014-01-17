/*
 * Copyright (c) 2006 Pyxis Technologies inc.
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
package com.greenpepper.interpreter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.greenpepper.Assertions.assertAnnotatedException;
import static com.greenpepper.Assertions.assertAnnotatedMissing;
import static com.greenpepper.Assertions.assertAnnotatedRight;
import static com.greenpepper.Assertions.assertAnnotatedStopped;
import static com.greenpepper.Assertions.assertAnnotatedSurplus;
import static com.greenpepper.Assertions.assertAnnotatedWrongWithDetails;
import static com.greenpepper.Assertions.assertNotAnnotated;
import com.greenpepper.GreenPepper;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.interpreter.collection.RowFixtureTarget;
import com.greenpepper.reflect.CollectionProvider;
import com.greenpepper.reflect.DefaultFixture;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.Tables;
import junit.framework.TestCase;

public class ListOfInterpreterTest
		extends TestCase
{

	private RowFixtureTarget target = new RowFixtureTarget();
	private ListOfInterpreter interpreter = new ListOfInterpreter(new PlainOldFixture(target));
	private Tables tables;

	@Override
	protected void setUp()
			throws Exception
	{
		GreenPepper.setStopOnFirstFailure(false);
	}

	@Override
	protected void tearDown()
			throws Exception
	{
		GreenPepper.setStopOnFirstFailure(false);
	}

	public void testInterpretsOneRowOfSpecification()
			throws Exception
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[a][b][c]\n" +
				"[1][2][3]"
		);
		interpreter.interpret(document());

		assertAnnotatedRight(tables.at(0, 2, 0));
		assertAnnotatedRight(tables.at(0, 2, 1));
		assertAnnotatedRight(tables.at(0, 2, 2));

		assertAnnotatedSurplus(tables.at(0, 3, 0));
		assertAnnotatedSurplus(tables.at(0, 4, 0));
	}

	private FakeSpecification document()
	{
		return new FakeSpecification(tables);
	}

	public void testInterpretAllRowsOfSpecification()
			throws Exception
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[a][b][c]\n" +
				"[1][2][3]\n" +
				"[1][2][3]\n" +
				"[1][2][3]"
		);
		interpreter.interpret(document());

		assertAnnotatedRight(tables.at(0, 2, 0));
		assertAnnotatedRight(tables.at(0, 2, 1));
		assertAnnotatedRight(tables.at(0, 2, 2));
		assertAnnotatedRight(tables.at(0, 3, 0));
		assertAnnotatedRight(tables.at(0, 3, 1));
		assertAnnotatedRight(tables.at(0, 3, 2));
		assertAnnotatedRight(tables.at(0, 4, 0));
		assertAnnotatedRight(tables.at(0, 4, 1));
		assertAnnotatedRight(tables.at(0, 4, 2));
	}

	public void testInterpreterWhenSpecificationDoesRespectsOrder()
			throws Exception
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[bar]\n" +
				"[1]\n" +
				"[2]\n" +
				"[3]"
		);
		interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
		interpreter.interpret(document());

		assertAnnotatedRight(tables.at(0, 2, 0));
		assertAnnotatedRight(tables.at(0, 3, 0));
		assertAnnotatedRight(tables.at(0, 4, 0));
	}

	public void testInterpreterWhenSpecificationDoesNotRespectOrder()
			throws Exception
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[bar]\n" +
				"[1]\n" +
				"[3]\n" +
				"[2]"
		);
		interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
		interpreter.interpret(document());

		assertAnnotatedRight(tables.at(0, 2, 0));
		assertAnnotatedWrongWithDetails(tables.at(0, 3, 0));
		assertAnnotatedWrongWithDetails(tables.at(0, 4, 0));
	}

	public void testInterpreterCanWorkWithCollectionReturnedFromQueryAction()
			throws Exception
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[a][b][c]\n" +
				"[1][2][3]"
		);

		interpreter = new ListOfInterpreter(fixtureThatReturnsABC());
		interpreter.interpret(document());

		assertAnnotatedRight(tables.at(0, 2, 0));
		assertAnnotatedRight(tables.at(0, 2, 1));
		assertAnnotatedRight(tables.at(0, 2, 2));
	}

	public void testInterpreterCollectsTestExecutionStatistics()
			throws Exception
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[a][b][c]\n" +
				"[1][2][4]"
		);

		interpreter = new ListOfInterpreter(fixtureThatReturnsABC());
		interpreter.interpret(document());

		assertAnnotatedRight(tables.at(0, 2, 0));
		assertAnnotatedRight(tables.at(0, 2, 1));
		assertAnnotatedWrongWithDetails(tables.at(0, 2, 2));
	}

	public void testFixtureReturnsMoreRows()
			throws Exception
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[bar]\n" +
				"[1]\n" +
				"[2]"
		);
		interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
		interpreter.interpret(document());

		assertAnnotatedSurplus(tables.at(0, 4, 0));
	}

	public void testFixtureReturnsMoreRowsUsingStopOnFirstFailure()
			throws Exception
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = Tables.parse(
				"[list of][values]\n" +
				"[bar]\n" +
				"[1]\n" +
				"[2]"
		);
		interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
		FakeSpecification document = document();
		interpreter.interpret(document);
		assertTrue(document.stats().indicatesFailure());
		assertAnnotatedSurplus(tables.at(0, 4, 0));
		assertAnnotatedStopped(tables.at(0, 4, 1));
	}

	public void testFixtureReturnsFewerRows()
			throws Exception
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[bar]\n" +
				"[1]\n" +
				"[2]\n" +
				"[3]\n" +
				"[4]"
		);
		interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
		interpreter.interpret(document());
		assertAnnotatedMissing(tables.at(0, 5, 0));
	}

	public void testFixtureReturnsFewerRowsUsingStopOnFirstFailure()
			throws Exception
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = Tables.parse(
				"[list of][values]\n" +
				"[bar]\n" +
				"[1]\n" +
				"[2]\n" +
				"[3]\n" +
				"[4]"
		);
		interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
		FakeSpecification document = document();
		interpreter.interpret(document);
		assertTrue(document.stats().indicatesFailure());
		assertAnnotatedMissing(tables.at(0, 5, 0));
		assertAnnotatedStopped(tables.at(0, 5, 1));
	}

	public void testFixtureThatReturnsEmptyCollection()
			throws Exception
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[bar]\n" +
				"[1]\n" +
				"[2]"
		);
		interpreter = new ListOfInterpreter(fixtureThatReturnsEmptyCollection());
		interpreter.interpret(document());
		assertAnnotatedMissing(tables.at(0, 2, 0));
		assertAnnotatedMissing(tables.at(0, 3, 0));
	}

	public void testFixtureThatReturnsEmptyCollectionUsingStopOnFirstFailure()
			throws Exception
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = Tables.parse(
				"[list of][values]\n" +
				"[bar]\n" +
				"[1]\n" +
				"[2]"
		);
		interpreter = new ListOfInterpreter(fixtureThatReturnsEmptyCollection());

		FakeSpecification document = document();
		interpreter.interpret(document);
		assertTrue(document.stats().indicatesFailure());
		assertAnnotatedMissing(tables.at(0, 2, 0));
		assertAnnotatedStopped(tables.at(0, 2, 1));
		assertNotAnnotated(tables.at(0, 3, 0));
	}

	public void testCanAccessTheCollectionproviderAttributes()
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[A][B][C]\n" +
				"[1][2][3]\n" +
				"[1][2][3]\n" +
				"[1][2][3]"
		);
		interpreter = new ListOfInterpreter(new PlainOldFixture(new TestObjectWithCollectionProvider(1)));
		interpreter.interpret(document());
		assertEquals(2, interpreter.statistics().wrongCount());
	}

	public void testThatCellWithMethodThrowingAnExceptionIsAnnotated()
			throws Exception
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[ex]\n" +
				"[1]"
		);
		interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
		FakeSpecification document = document();
		interpreter.interpret(document);
		assertTrue(document.stats().indicatesFailure());
		assertAnnotatedException(tables.at(0, 2, 0));
	}

	public void testThatCellWithMethodThrowingAnExceptionIsAnnotatedStopWhenStopOnFirstFailure()
			throws Exception
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = Tables.parse(
				"[list of][values]\n" +
				"[ex]\n" +
				"[1]"
		);
		interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
		FakeSpecification document = document();
		interpreter.interpret(document);
		assertTrue(document.stats().indicatesFailure());
		assertAnnotatedException(tables.at(0, 2, 0));
		assertAnnotatedStopped(tables.at(0, 2, 1));
	}

	public void testThatFixtureWithoutAProperCollectionQueryHasAnnotatedCellToException()
	{
		tables = Tables.parse(
				"[list of][values]\n" +
				"[bar]\n" +
				"[1]"
		);
		interpreter = new ListOfInterpreter(new DefaultFixture(new FixtureThatHasNoCollection()));
		FakeSpecification document = document();
		interpreter.interpret(document);
		assertTrue(document.stats().indicatesFailure());
		assertAnnotatedException(tables.at(0, 1, 0));
	}

	public void testThatFixtureWithoutAProperCollectionQueryHasAnnotatedCellToExceptionAndStopWhenStopOnFirstFailure()
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = Tables.parse(
				"[list of][values]\n" +
				"[bar]\n" +
				"[1]"
		);
		interpreter = new ListOfInterpreter(new DefaultFixture(new FixtureThatHasNoCollection()));
		FakeSpecification document = document();
		interpreter.interpret(document);
		assertTrue(document.stats().indicatesFailure());
		assertAnnotatedException(tables.at(0, 1, 0));
		assertAnnotatedStopped(tables.at(0, 1, 1));
	}

	private Fixture fixtureThatReturnsABC()
	{
		return new PlainOldFixture(new FixtureThatReturnsABCRowCollection());
	}

	private Fixture fixtureThatReturnsFoosAndBars()
	{
		return new PlainOldFixture(new FixtureThatReturnsCollectionOfFoo(3));
	}

	private Fixture fixtureThatReturnsEmptyCollection()
	{
		return new PlainOldFixture(new FixtureThatReturnsCollectionOfFoo(0));
	}

	public static class FixtureThatReturnsABCRowCollection
	{

		public Collection query()
		{
			ArrayList<Object> list = new ArrayList<Object>();

			list.add(new RowFixtureTarget.TestObject());
			return list;
		}
	}

	public class FixtureThatHasNoCollection
	{
		
	}

	public class FixtureThatReturnsCollectionOfFoo
	{

		private int howmany;

		public FixtureThatReturnsCollectionOfFoo(int howmany)
		{
			this.howmany = howmany;
		}

		public Collection query()
		{
			Collection<Foo> c = new ArrayList<Foo>();
			for (int i = 0; i < howmany; i++)
			{
				c.add(new Foo("" + (i + 1)));
			}
			return c;
		}

		public class Foo
		{

			private String bar;

			public Foo(String bar)
			{
				this.bar = bar;
			}

			public String getBar()
			{
				return bar;
			}

			public String getEx()
			{
				throw new RuntimeException(bar);
			}
		}
	}

	public class TestObjectWithCollectionProvider
	{

		private int howMany;

		public TestObjectWithCollectionProvider(int howMany)
		{
			this.howMany = howMany;
		}

		@CollectionProvider()
		public Collection SomeMethod()
		{
			List<Object> objects = new ArrayList<Object>();

			for (int i = 0; i != howMany; ++i)
			{
				objects.add(new RowFixtureTarget.TestObject());
			}
			return objects;
		}
	}
}