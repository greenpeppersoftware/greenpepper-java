/*
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

package com.greenpepper.interpreter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import static com.greenpepper.Assertions.assertAnnotatedEntered;
import static com.greenpepper.Assertions.assertAnnotatedSkipped;
import static com.greenpepper.Assertions.assertNotAnnotated;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.Tables;
import com.greenpepper.util.TestCase;

public class DoSetupInterpreterTest
		extends TestCase
{
    private Tables tables;
    private DoSetupInterpreter interpreter;
	private Mockery context = new JUnit4Mockery();
    private Target fixture;

	@Override
    protected void setUp() throws Exception
    {
        fixture = context.mock( Target.class );
        interpreter = new DoSetupInterpreter( code() );
    }

	@Override
	protected void tearDown()
	{
		context.assertIsSatisfied();
	}

	public void testATableDefinesASequenceOfActions() throws Exception
    {
		context.checking(new Expectations()
		{{
			exactly(2).of(fixture).commandTakingNoParameter();
			will(returnValue(true));
		}});
        tables = Tables.parse(
            "[do setup][mock]\n" +
            "[commandTakingNoParameter]\n" +
            "[commandTakingNoParameter]"
        );
        interpreter.interpret( document() );
    }

    public void testStatisticShouldBeInFailuresWithNonExistingMethod()
    {
		tables = Tables.parse(
			"[do setup][mock]\n" +
			"[non existing method]\n"
		);

		FakeSpecification specification = document();
		interpreter.interpret( specification );
		assertTrue(specification.stats().hasFailed());
    }

	public void testStatisticShouldBeInFailuresWithBadNumberOfMethodParameter()
	{
		tables = Tables.parse(
			"[do setup][mock]\n" +
			"[commandExpectingParameters]\n"
		);

		FakeSpecification specification = document();
		interpreter.interpret( specification );
		assertTrue(specification.stats().hasFailed());
	}

    public void testActionsAcceptParametersInEveryOtherCell() throws Exception
    {
        tables = Tables.parse(
            "[do setup][mock]\n" +
            "[command] [a parameter] [expecting] [a second parameter] [parameters] [a third parameter]"
        );

		context.checking(new Expectations()
		{{
			one(fixture).commandExpectingParameters("a parameter", "a second parameter", "a third parameter");
		}});
        interpreter.interpret( document() );
    }

    public void testKeywordsContainingSpacesAreCamelized() throws Exception
    {
		context.checking(new Expectations()
		{{
			one(fixture).commandExpectingASingleParameter("parameter");
		}});
        tables = Tables.parse(
            "[do setup][mock]\n" +
            "[command expecting a single parameter][parameter]" );

        interpreter.interpret( document() );
    }

    public void testLastNewCellsAreAnnotatedEnteredIfTheyReturnTrue() throws Exception
    {
		context.checking(new Expectations()
		{{
			one(fixture).commandExpectingASingleParameter("parameter");
			will(returnValue(true));
		}});
        tables = Tables.parse(
            "[do setup][mock]\n" +
            "[command expecting a single parameter][parameter]\n" +
			"[command expecting a single parameter][parameter]\n");

		FakeSpecification specification = document();

		interpreter.interpret( specification );

        assertNotAnnotated( tables.at( 0, 1, 0 ) );
        assertNotAnnotated( tables.at( 0, 1, 1 ) );
		assertAnnotatedEntered( tables.at( 0, 1, 2 ) );
		assertNotAnnotated( tables.at( 0, 2, 0 ) );
		assertNotAnnotated( tables.at( 0, 2, 1 ) );
		assertAnnotatedEntered( tables.at( 0, 2, 2 ) );

		assertFalse(specification.stats().hasFailed());
    }

	public void testLastNewCellsAreAnnotatedSkippedIfTheyReturnAnException() throws Exception
	{
		context.checking(new Expectations()
		{{
			one(fixture).commandExpectingASingleParameter("parameter");
			will(throwException(new RuntimeException()));
		}});
		tables = Tables.parse(
			"[do setup][mock]\n" +
			"[command expecting a single parameter][parameter]\n" +
			"[command expecting a single parameter][parameter]\n");

		FakeSpecification specification = document();

		interpreter.interpret( specification );

		assertNotAnnotated( tables.at( 0, 1, 0 ) );
		assertNotAnnotated( tables.at( 0, 1, 1 ) );
		assertAnnotatedSkipped( tables.at( 0, 1, 2 ) );
		assertNotAnnotated( tables.at( 0, 2, 0 ) );
		assertNotAnnotated( tables.at( 0, 2, 1 ) );
		assertAnnotatedSkipped( tables.at( 0, 2, 2 ) );

		assertFalse(specification.stats().hasFailed());
	}

	public void testLastNewCellsAreAnnotatedSkippedIfTheyReturnFalse() throws Exception
	{
		context.checking(new Expectations()
		{{
			one(fixture).commandExpectingASingleParameter("parameter");
			will(returnValue(false));
		}});
		tables = Tables.parse(
			"[do setup][mock]\n" +
			"[command expecting a single parameter][parameter]\n" +
			"[command expecting a single parameter][parameter]\n");

		FakeSpecification specification = document();

		interpreter.interpret( specification );

		assertNotAnnotated( tables.at( 0, 1, 0 ) );
		assertNotAnnotated( tables.at( 0, 1, 1 ) );
		assertAnnotatedSkipped( tables.at( 0, 1, 2 ) );
		assertNotAnnotated( tables.at( 0, 2, 0 ) );
		assertNotAnnotated( tables.at( 0, 2, 1 ) );
		assertAnnotatedSkipped( tables.at( 0, 2, 2 ) );

		assertFalse(specification.stats().hasFailed());
	}

	public void testLastNewCellsAreAnnotatedSkippedOnlyAfterAReturnFalse() throws Exception
	{
		context.checking(new Expectations()
		{{
			one(fixture).commandExpectingASingleParameter("parameter");
			will(returnValue(true));
			one(fixture).commandTakingNoParameter();
			will(returnValue(false));
		}});

		tables = Tables.parse(
			"[do setup][mock]\n" +
			"[command expecting a single parameter][parameter]\n" +
			"[commandTakingNoParameter]\n" +
			"[command expecting a single parameter][parameter]\n");

		FakeSpecification specification = document();

		interpreter.interpret( specification );

		assertNotAnnotated( tables.at( 0, 1, 0 ) );
		assertNotAnnotated( tables.at( 0, 1, 1 ) );
		assertAnnotatedEntered( tables.at( 0, 1, 2 ) );
		assertNotAnnotated( tables.at( 0, 2, 0 ) );
		assertAnnotatedSkipped( tables.at( 0, 2, 1 ) );
		assertNotAnnotated( tables.at( 0, 3, 0 ) );
		assertNotAnnotated( tables.at( 0, 3, 1 ) );
		assertAnnotatedSkipped( tables.at( 0, 3, 2 ) );

		assertFalse(specification.stats().hasFailed());
	}

	public void testLastNewCellsAreAnnotatedSkippedOnlyAfterAnException() throws Exception
	{
		context.checking(new Expectations()
		{{
			one(fixture).commandExpectingASingleParameter("parameter");
			will(returnValue(true));
			one(fixture).commandTakingNoParameter();
			will(throwException(new RuntimeException()));
		}});

		tables = Tables.parse(
			"[do setup][mock]\n" +
			"[command expecting a single parameter][parameter]\n" +
			"[commandTakingNoParameter]\n" +
			"[command expecting a single parameter][parameter]\n");

		FakeSpecification specification = document();

		interpreter.interpret( specification );

		assertNotAnnotated( tables.at( 0, 1, 0 ) );
		assertNotAnnotated( tables.at( 0, 1, 1 ) );
		assertAnnotatedEntered( tables.at( 0, 1, 2 ) );
		assertNotAnnotated( tables.at( 0, 2, 0 ) );
		assertAnnotatedSkipped( tables.at( 0, 2, 1 ) );
		assertNotAnnotated( tables.at( 0, 3, 0 ) );
		assertNotAnnotated( tables.at( 0, 3, 1 ) );
		assertAnnotatedSkipped( tables.at( 0, 3, 2 ) );

		assertFalse(specification.stats().hasFailed());
	}

    private Fixture code()
    {
        return new PlainOldFixture( fixture );
    }

    private FakeSpecification document()
    {
        return new FakeSpecification( tables );
    }

    public static interface Target
    {
        Object commandTakingNoParameter();

        Object commandExpectingParameters( String first, String second, String third );

        Object commandExpectingASingleParameter( String p );
    }
}