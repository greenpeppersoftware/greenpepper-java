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

import static com.greenpepper.Assertions.assertAnnotatedException;
import static com.greenpepper.Assertions.assertAnnotatedMissing;
import static com.greenpepper.Assertions.assertAnnotatedRight;
import static com.greenpepper.Assertions.assertAnnotatedStopped;
import static com.greenpepper.Assertions.assertAnnotatedSurplus;
import static com.greenpepper.Assertions.assertNotAnnotated;
import com.greenpepper.GreenPepper;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.interpreter.collection.RowFixtureTarget;
import com.greenpepper.reflect.DefaultFixture;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.Tables;
import junit.framework.TestCase;

public class SetOfInterpreterTest
    extends TestCase
{
    private RowFixtureTarget target = new RowFixtureTarget();
    private SetOfInterpreter interpreter = new SetOfInterpreter( new PlainOldFixture( target ) );
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

    public void testInterpretsOneRowOfSpecification() throws Exception
    {
        tables = Tables.parse(
            "[set of][values]\n" +
            "[a][b][c]\n" +
            "[1][2][3]"
        );
        interpreter.interpret( document() );

        assertAnnotatedRight( tables.at( 0, 2, 0 ) );
        assertAnnotatedRight( tables.at( 0, 2, 1 ) );
        assertAnnotatedRight( tables.at( 0, 2, 2 ) );
    }

    public void testInterpretAllRowsOfSpecification() throws Exception
    {
        tables = Tables.parse(
            "[set of][values]\n" +
            "[a][b][c]\n" +
            "[1][2][3]\n" +
            "[1][2][3]\n" +
            "[1][2][3]"
        );
        interpreter.interpret( document() );

        assertAnnotatedRight( tables.at( 0, 2, 0 ) );
        assertAnnotatedRight( tables.at( 0, 2, 1 ) );
        assertAnnotatedRight( tables.at( 0, 2, 2 ) );
        assertAnnotatedRight( tables.at( 0, 3, 0 ) );
        assertAnnotatedRight( tables.at( 0, 3, 1 ) );
        assertAnnotatedRight( tables.at( 0, 3, 2 ) );
        assertAnnotatedRight( tables.at( 0, 4, 0 ) );
        assertAnnotatedRight( tables.at( 0, 4, 1 ) );
        assertAnnotatedRight( tables.at( 0, 4, 2 ) );
    }

    public void testInterpreterCanWorkWithCollectionReturnedFromQueryAction() throws Exception
    {
        tables = Tables.parse(
            "[set of][values]\n" +
            "[a][b][c]\n" +
            "[1][2][3]"
        );
        interpreter = new SetOfInterpreter( fixtureThatReturnsABaz() );
        interpreter.interpret( document() );

        assertAnnotatedRight( tables.at( 0, 2, 0 ) );
        assertAnnotatedRight( tables.at( 0, 2, 1 ) );
        assertAnnotatedRight( tables.at( 0, 2, 2 ) );
    }

    public void testInterpreterWhenSpecificationDoesRespectsOrder() throws Exception
    {
        tables = Tables.parse(
            "[set of][values]\n" +
            "[bar]\n" +
            "[1]\n" +
            "[2]\n" +
            "[3]"
        );
        interpreter = new SetOfInterpreter( fixtureThatReturnsFoosAndBars() );
        interpreter.interpret( document() );

        assertAnnotatedRight( tables.at( 0, 2, 0 ) );
        assertAnnotatedRight( tables.at( 0, 3, 0 ) );
        assertAnnotatedRight( tables.at( 0, 4, 0 ) );
    }

    public void testInterpreterWhenSpecificationDoesNotRespectsOrder() throws Exception
    {
        tables = Tables.parse(
            "[set of][values]\n" +
            "[bar]\n" +
            "[1]\n" +
            "[3]\n" +
            "[2]"
        );
        interpreter = new SetOfInterpreter( fixtureThatReturnsFoosAndBars() );
        interpreter.interpret( document() );

        assertAnnotatedRight( tables.at( 0, 2, 0 ) );
        assertAnnotatedRight( tables.at( 0, 3, 0 ) );
        assertAnnotatedRight( tables.at( 0, 4, 0 ) );
    }

    public void testInterpreterWhenSpecificationDoesNotMatchFixture() throws Exception
    {
        tables = Tables.parse(
            "[set of][values]\n" +
            "[bar]\n" +
            "[1]\n" +
            "[5]\n" +
            "[2]"
        );
        interpreter = new SetOfInterpreter( fixtureThatReturnsFoosAndBars() );
        interpreter.interpret( document() );

        assertAnnotatedRight( tables.at( 0, 2, 0 ) );
        assertAnnotatedMissing( tables.at( 0, 3, 0 ) );
        assertAnnotatedRight( tables.at( 0, 4, 0 ) );
    }

    public void testThatTheMethodNameIsCallOnTheObjectCollection()
    {
        tables = Tables.parse(
            "[set of][values]\n" +
            "[intValue]\n" +
            "[1]\n" +
            "[2]\n" +
            "[3]\n" +
            "[4]"
        );
        interpreter = new SetOfInterpreter( new PlainOldFixture( new Integer[]{1, 2, 5, 4} ) );
        interpreter.interpret( document() );

        assertAnnotatedRight( tables.at( 0, 2, 0 ) );
        assertAnnotatedRight( tables.at( 0, 3, 0 ) );
        assertAnnotatedMissing( tables.at( 0, 4, 0 ) );
        assertAnnotatedRight( tables.at( 0, 5, 0 ) );
        assertAnnotatedSurplus( tables.at( 0, 6, 0 ) );
    }

    public void testThatMissingRowsAreAnnotatedMissing()
    {
        tables = Tables.parse(
            "[set of][values]\n" +
            "[intValue]\n" +
            "[1]\n" +
            "[2]\n" +
            "[3]\n" +
            "[4]"
        );
        interpreter = new SetOfInterpreter( new PlainOldFixture( new Integer[]{1, 2, 4} ) );
        interpreter.interpret( document() );

        assertAnnotatedRight( tables.at( 0, 2, 0 ) );
        assertAnnotatedRight( tables.at( 0, 3, 0 ) );
        assertAnnotatedMissing( tables.at( 0, 4, 0 ) );
        assertAnnotatedRight( tables.at( 0, 5, 0 ) );
    }

	public void testThatMissingRowsAreAnnotatedMissingAndStoppedWhenStopOnFirstFailure()
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = Tables.parse(
			"[set of][values]\n" +
			"[intValue]\n" +
			"[1]\n" +
			"[2]\n" +
			"[3]\n" +
			"[4]"
		);
		interpreter = new SetOfInterpreter( new PlainOldFixture( new Integer[]{1, 2, 4} ) );

		FakeSpecification document = document();
		interpreter.interpret( document );
		assertTrue(document.stats().indicatesFailure());
		
		assertAnnotatedRight( tables.at( 0, 2, 0 ) );
		assertAnnotatedRight( tables.at( 0, 3, 0 ) );
		assertAnnotatedMissing( tables.at( 0, 4, 0 ) );
		assertAnnotatedStopped( tables.at( 0, 4, 1 ) );
		assertAnnotatedRight( tables.at( 0, 5, 0 ) );
	}

	public void testThatSurplusRowsAreAnnotatedSurplus()
	{
		tables = Tables.parse(
			"[set of][values]\n" +
			"[intValue]\n" +
			"[1]\n" +
			"[2]"
		);
		interpreter = new SetOfInterpreter( new PlainOldFixture( new Integer[]{1, 2, 4} ) );
		interpreter.interpret( document() );

		assertAnnotatedRight( tables.at( 0, 2, 0 ) );
		assertAnnotatedRight( tables.at( 0, 3, 0 ) );
		assertAnnotatedSurplus( tables.at( 0, 4, 0 ) );
	}


	public void testThatSurplusRowsAreAnnotatedSurplusAndStopWhenStopOnFirstFailure()
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = Tables.parse(
			"[set of][values]\n" +
			"[intValue]\n" +
			"[1]\n" +
			"[2]"
		);
		interpreter = new SetOfInterpreter( new PlainOldFixture( new Integer[]{1, 2, 4} ) );

		FakeSpecification document = document();
		interpreter.interpret( document );
		assertTrue(document.stats().indicatesFailure());

		assertAnnotatedRight( tables.at( 0, 2, 0 ) );
		assertAnnotatedRight( tables.at( 0, 3, 0 ) );
		assertAnnotatedSurplus( tables.at( 0, 4, 0 ) );
		assertAnnotatedStopped( tables.at( 0, 4, 1 ) );
	}

    public void testThatWeCanTestMoreThenOneMethodOnARow()
    {
        tables = Tables.parse(
            "[set of][values]\n" +
            "[a][b]\n" +
            "[11][12]\n" +
            "[21][22]\n" +
            "[31][0]\n" +
            "[41][42]"
        );

        IntegersFixture[] collection = {
            new IntegersFixture( 11, 12 ),
            new IntegersFixture( 21, 22 ),
            new IntegersFixture( 31, 32 ),
            new IntegersFixture( 41, 42 )};

        interpreter = new SetOfInterpreter( new PlainOldFixture( collection ) );
        interpreter.interpret( document() );

        assertAnnotatedRight( tables.at( 0, 2, 0 ) );
        assertAnnotatedRight( tables.at( 0, 2, 1 ) );

        assertAnnotatedRight( tables.at( 0, 3, 0 ) );
        assertAnnotatedRight( tables.at( 0, 3, 1 ) );

        assertAnnotatedMissing( tables.at( 0, 4, 0 ) );
        assertAnnotatedMissing( tables.at( 0, 4, 1 ) );

        assertAnnotatedRight( tables.at( 0, 5, 0 ) );
        assertAnnotatedRight( tables.at( 0, 5, 1 ) );

        assertAnnotatedSurplus( tables.at( 0, 6, 0 ) );
        assertAnnotatedSurplus( tables.at( 0, 6, 1 ) );
    }

	public void testThatFixtureWithoutAProperCollectionQueryHasAnnotatedCellToException()
	{
		tables = Tables.parse(
			"[set of][values]\n" +
			"[bytes]\n" +
			"[]"
		);
		interpreter = new SetOfInterpreter( new DefaultFixture( "" ) );

		FakeSpecification document = document();
		interpreter.interpret( document );
		assertTrue(document.stats().indicatesFailure());

		assertAnnotatedException( tables.at( 0, 1, 0 ) );
	}

	public void testThatFixtureWithoutAProperCollectionQueryHasAnnotatedCellToExceptionAndStopWhenStopOnFirstFailure()
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = Tables.parse(
			"[set of][values]\n" +
			"[bytes]\n" +
			"[]"
		);
		interpreter = new SetOfInterpreter( new DefaultFixture( "" ) );

		FakeSpecification document = document();
		interpreter.interpret( document );
		assertTrue(document.stats().indicatesFailure());

		assertAnnotatedException( tables.at( 0, 1, 0 ) );
		assertAnnotatedStopped( tables.at( 0, 1, 1 ) );
	}

	public void testThatCellWithMethodThrowingAnExceptionIsConsideredAsMissing() throws Exception
	{
		tables = Tables.parse(
			"[set of][values]\n" +
			"[ex]\n" +
			"[1]\n" +
			"[2]\n" +
			"[3]"
		);
		interpreter = new SetOfInterpreter( fixtureThatReturnsFoosAndBars() );

		FakeSpecification document = document();
		interpreter.interpret( document );
		assertTrue(document.stats().indicatesFailure());

		assertAnnotatedMissing( tables.at( 0, 2, 0 ) );
		assertAnnotatedMissing( tables.at( 0, 3, 0 ) );
		assertAnnotatedMissing( tables.at( 0, 4, 0 ) );
	}

	public void testThatCellWithMethodThrowingAnExceptionIsConsideredAsMissingWhenStop() throws Exception
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = Tables.parse(
			"[set of][values]\n" +
			"[ex]\n" +
			"[1]\n" +
			"[2]\n" +
			"[3]"
		);
		interpreter = new SetOfInterpreter( fixtureThatReturnsFoosAndBars() );

		FakeSpecification document = document();
		interpreter.interpret( document );
		assertTrue(document.stats().indicatesFailure());

		assertAnnotatedMissing( tables.at( 0, 2, 0 ) );
		assertAnnotatedStopped( tables.at( 0, 2, 1 ) );
		assertNotAnnotated( tables.at( 0, 3, 0 ) );
		assertNotAnnotated( tables.at( 0, 4, 0 ) );
	}

    private FakeSpecification document()
    {
        return new FakeSpecification( tables );
    }

    public class IntegersFixture
    {
        public Integer a;
        public Integer b;

        IntegersFixture( Integer a, Integer b )
        {
            this.a = a;
            this.b = b;
        }
    }

    private Fixture fixtureThatReturnsABaz()
    {
        return new PlainOldFixture( new FixtureThatReturnsCollection() );
    }

    public static class FixtureThatReturnsCollection
    {
        public Collection query()
        {
            ArrayList<Object> list = new ArrayList<Object>();

            list.add( new RowFixtureTarget.TestObject() );
            return list;
        }
    }

    private PlainOldFixture fixtureThatReturnsFoosAndBars()
    {
        return new PlainOldFixture( new FixtureThatReturnsCollection2() );
    }

    public class FixtureThatReturnsCollection2
    {
        public Collection query()
        {
            Collection<Foo> c = new ArrayList<Foo>();
            for (int i = 0; i < 3; i++)
            {
                c.add( new Foo( new Integer( i + 1 ) ) );
            }
            return c;
        }

        public class Foo
        {
            private Integer bar;

            public Foo( Integer bar )
            {
                this.bar = bar;
            }

            public Integer getBar()
            {
                return bar;
            }

			public Integer getEx()
			{
				throw new RuntimeException(String.valueOf(bar));
			}
        }
    }
}