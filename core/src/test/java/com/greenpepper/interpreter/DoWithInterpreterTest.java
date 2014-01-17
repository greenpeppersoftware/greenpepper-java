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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import static com.greenpepper.Assertions.assertAnnotatedException;
import static com.greenpepper.Assertions.assertAnnotatedIgnored;
import static com.greenpepper.Assertions.assertAnnotatedRight;
import static com.greenpepper.Assertions.assertAnnotatedStopped;
import static com.greenpepper.Assertions.assertAnnotatedWrongWithDetails;
import static com.greenpepper.Assertions.assertAnnotatedWrongWithoutDetail;
import static com.greenpepper.Assertions.assertNotAnnotated;
import com.greenpepper.Example;
import com.greenpepper.GreenPepper;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.annotation.RightAnnotation;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.ExampleUtil;
import com.greenpepper.util.Tables;
import com.greenpepper.util.TestCase;

public class DoWithInterpreterTest extends TestCase
{
    private Tables tables;
    private DoWithInterpreter interpreter;
	private Mockery context = new JUnit4Mockery();
    private Target fixture;

    protected void setUp() throws Exception
    {
		fixture = context.mock( Target.class );
        interpreter = new DoWithInterpreter( code() );
		GreenPepper.setStopOnFirstFailure(false); //reset
    }

	protected void tearDown() throws Exception
	{
		GreenPepper.setStopOnFirstFailure(false); //reset
	}

	public void testATableDefinesASequenceOfActions() throws Exception
    {
		context.checking(new Expectations()
		{{
			exactly(2).of(fixture).commandTakingNoParameter();
			will(returnValue(true));
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "[commandTakingNoParameter]\n" +
            "[commandTakingNoParameter]"
        );
        interpreter.interpret( document() );
    }

    public void testShouldCompileTestStatistics()
    {
    }

    public void testActionsAcceptParametersInEveryOtherCell() throws Exception
    {
        tables = Tables.parse(
            "[do with][mock]\n" +
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
            "[do with][mock]\n" +
            "[command expecting a single parameter][parameter]" );

        interpreter.interpret( document() );
    }

    public void testDefaultActionsAreAnnotatedRightIfTheyReturnTrue() throws Exception
    {
		context.checking(new Expectations()
		{{
			one(fixture).commandExpectingParameters("a parameter", "a second parameter", "a third parameter");
			will(returnValue(true));
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "[command][a parameter][expecting][a second parameter][parameters][a third parameter]" );

        interpreter.interpret( document() );
        assertAnnotatedRight( tables.at( 0, 1, 0 ) );
        assertNotAnnotated( tables.at( 0, 1, 1 ) );
        assertAnnotatedRight( tables.at( 0, 1, 2 ) );
        assertNotAnnotated( tables.at( 0, 1, 3 ) );
        assertAnnotatedRight( tables.at( 0, 1, 4 ) );
        assertNotAnnotated( tables.at( 0, 1, 5 ) );
    }

    public void testDefaultActionsAreAnnotatedWrongIfTheyReturnFalse() throws Exception
    {
		context.checking(new Expectations()
		{{
			one(fixture).commandTakingNoParameter();
			will(returnValue(false));
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "[commandTakingNoParameter]" );

        interpreter.interpret( document() );
        assertAnnotatedWrongWithoutDetail( tables.at( 0, 1, 0 ) );
    }

    public void testDefaultActionsColorFirstKeywordOnException() throws Exception
    {
		context.checking(new Expectations()
		{{
			one(fixture).commandExpectingParameters("a parameter", "a second parameter", "a third parameter");
			will(throwException(new RuntimeException("Command failed")));
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "[command][a parameter][expecting][a second parameter][parameters][a third parameter]" );

        interpreter.interpret( document() );
        assertAnnotatedException( tables.at( 0, 1, 0 ) );
        assertNotAnnotated( tables.at( 0, 1, 1 ) );
        assertNotAnnotated( tables.at( 0, 1, 2 ) );
        assertNotAnnotated( tables.at( 0, 1, 3 ) );
        assertNotAnnotated( tables.at( 0, 1, 4 ) );
        assertNotAnnotated( tables.at( 0, 1, 5 ) );
    }

    public void testActionsThatReturnAnotherValueOrVoidShouldNotBeAnnotated() throws Exception
    {
		context.checking(new Expectations()
		{{
			one(fixture).commandExpectingParameters("a parameter", "a second parameter", "a third parameter");
			will(returnValue(new Object()));

			one(fixture).commandReturningNothing();
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "[command][a parameter][expecting][a second parameter][parameters][a third parameter]\n" +
            "[commandReturningNothing]" );

        interpreter.interpret( document() );
        assertNotAnnotated( tables.at( 0, 1, 0 ) );
        assertNotAnnotated( tables.at( 0, 1, 1 ) );
        assertNotAnnotated( tables.at( 0, 1, 2 ) );
        assertNotAnnotated( tables.at( 0, 1, 3 ) );
        assertNotAnnotated( tables.at( 0, 1, 4 ) );
        assertNotAnnotated( tables.at( 0, 1, 5 ) );
        assertNotAnnotated( tables.at( 0, 2, 0 ) );
    }

    public void testActionsCanReturnAFixtureToInterpretRestOfTable() throws Exception
    {
        final Object anotherFixture = new Object();

		context.checking(new Expectations()
		{{
			one(fixture).commandTakingNoParameter();
			will(returnValue(anotherFixture));
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "[" + RightInterpreter.class.getName() + "][commandTakingNoParameter]\n" +
            "[actionOnReturnedFixture]\n" +
            "[actionOnReturnedFixture]" );

        Example rows = tables.at( 0, 1 );
        interpreter.interpret( document() );
        assertAnnotatedRight( rows.at( 1, 0 ) );
        assertAnnotatedRight( rows.at( 2, 0 ) );
    }

    public void testCheckSpecialActionExpectsValueOfLastCellAndColorsLastCell() throws Exception
    {
		context.checking(new Expectations()
		{{
		   exactly(2).of(fixture).thatValueOfIs("balance");
		   will(onConsecutiveCalls(returnValue("100"), returnValue("150")));
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "[check][that value of][balance][is][100]\n" +
            "[check][that value of][balance][is][120]" );

        interpreter.interpret( document() );
        assertAnnotatedRight( tables.at( 0, 1, 4 ) );
        assertAnnotatedWrongWithDetails( tables.at( 0, 2, 4 ) );
    }

    public void testCheckSpecialActionColorsFirstKeywordCellOnException() throws Exception
    {
		context.checking(new Expectations()
		{{
		   one(fixture).commandThrowingException();
			will(throwException(new Exception("Command failed")));
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "[check][commandThrowingException][value]" );

        interpreter.interpret( document() );
        assertAnnotatedException( tables.at( 0, 1, 1 ) );
        assertNotAnnotated( tables.at( 0, 1, 2 ) );
    }

    public void testRejectSpecialActionExpectsFailureAndColorTheKeyword() throws Exception
    {
		context.checking(new Expectations()
		{{
			exactly(3).of(fixture).commandExpectingASingleParameter("parameter");
			will(onConsecutiveCalls(returnValue(true), returnValue(false), throwException(new RuntimeException())));
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "[reject][commandExpectingASingleParameter][parameter]\n" +
            "[reject][commandExpectingASingleParameter][parameter]\n" +
            "[reject][commandExpectingASingleParameter][parameter]" );

        interpreter.interpret( document() );
        assertNotAnnotated( tables.at( 0, 1, 1 ) );
        assertAnnotatedWrongWithoutDetail( tables.at( 0, 1, 0 ) );
        assertNotAnnotated( tables.at( 0, 2, 1 ) );
        assertAnnotatedRight( tables.at( 0, 2, 0 ) );
        assertNotAnnotated( tables.at( 0, 3, 1 ) );
        assertAnnotatedRight( tables.at( 0, 3, 0 ) );
    }

    public void testSpecialActionsCanHaveAnyCase() throws Exception
    {
		context.checking(new Expectations()
		{{
			exactly(2).of(fixture).thatValueOfIs("balance");
			will(onConsecutiveCalls(returnValue("1"), returnValue("2")));

			exactly(2).of(fixture).commandExpectingASingleParameter("parameter");
			will(onConsecutiveCalls(returnValue(true), returnValue(false)));
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "[CHECK][that value of][balance][is][1]\n" +
            "[Check][that value of][balance][is][3]\n" +
            "[REjeCT][commandExpectingASingleParameter][parameter]\n" +
            "[Reject][commandExpectingASingleParameter][parameter]" );

        interpreter.interpret( document() );
        assertAnnotatedRight( tables.at( 0, 1, 4 ) );
        assertAnnotatedWrongWithDetails( tables.at( 0, 2, 4 ) );
        assertAnnotatedWrongWithoutDetail( tables.at( 0, 3, 0 ) );
        assertAnnotatedRight( tables.at( 0, 4, 0 ) );
    }

    public void testWillConsumeAllTablesInDocumentAndCompileStatistics()
    {
		context.checking(new Expectations()
		{{
			one(fixture).openAccount("123456");
			will(returnValue(true));
			exactly(2).of(fixture).thatBalanceOfAccountIs("123456");
			will(onConsecutiveCalls(returnValue("0.00"), returnValue("100.00")));
			one(fixture).depositInAccount("100.00", "123456");
			will(returnValue(true));
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "****\n" +
            "[open account][123456]\n" +
            "****\n" +
            "[check][that balance of account][123456][is][0.00]\n" +
            "****\n" +
            "[deposit][100.00][in account][123456]\n" +
            "****\n" +
            "[check][that balance of account][123456][is][100.00]"
        );

        FakeSpecification specification = document();
        interpreter.interpret( specification );
        assertAnnotatedRight( tables.at( 1, 0, 0 ) );
        assertAnnotatedRight( tables.at( 2, 0, 4 ) );
        assertAnnotatedRight( tables.at( 3, 0, 0 ) );
        assertAnnotatedRight( tables.at( 4, 0, 4 ) );

        assertFalse( specification.hasMoreExamples() );

        assertEquals( new Statistics(4, 0, 0, 0), specification.stats );
    }

    public void testEndKeywordStopsFlow()
    {
		context.checking(new Expectations()
		{{
			one(fixture).openAccount("123456");
			will(returnValue(true));
			exactly(2).of(fixture).thatBalanceOfAccountIs("123456");
			will(onConsecutiveCalls(returnValue("0.00"), returnValue("100.00")));
		}});

        tables = Tables.parse(
            "[do with][mock]\n" +
            "****\n" +
            "[open account][123456]\n" +
            "****\n" +
            "[check][that balance of account][123456][is][0.00]\n" +
            "****\n" +
            "[end]\n" +
            "*****" +
            "[deposit][100.00][in account][654321]\n" +
            "****\n" +
            "[check][that balance of account][654321][is][100.00]"
        );

        Specification specification = document();
        interpreter.interpret( specification );
        assertTrue( specification.hasMoreExamples() );
        Example next = specification.nextExample();
        assertEquals( "deposit", ExampleUtil.contentOf( next.at( 0, 0, 0 ) ) );
    }

	public void testDisplayKeywordFlow()
	{
		context.checking(new Expectations()
		{{
			one(fixture).thatBalanceOfAccountIs("123456");
			will(returnValue("0.00"));
			one(fixture).actualMortgageRate();
			will(returnValue("5.45"));
		}});

		tables = Tables.parse(
			"[do with][mock]\n" +
			"****\n" +
			"[display][the balance of account][123456]\n" +
			"****\n" +
			"[display][actual mortgage rate]"
		);

		Specification specification = document();
		interpreter.interpret( specification );

		assertAnnotatedIgnored( tables.at( 1, 0, 3 ) );
		assertAnnotatedIgnored( tables.at( 2, 0, 2 ) );
	}

	public void testThatThrownedExceptionWithOptionsStopOnFailureShouldAnnotedTheRowStopped()
	{
		context.checking(new Expectations()
		{{
			one(fixture).commandExpectingASingleParameter("parameter");
			will(throwException(new RuntimeException()));
		}});

		GreenPepper.setStopOnFirstFailure(true);

		tables = Tables.parse(
			"[do with][mock]\n" +
			"[command expecting a single parameter][parameter]\n" +
			"[command expecting a single parameter][parameter]"
		);

		FakeSpecification specification = document();

		interpreter.interpret( specification );

		assertTrue(specification.stats().hasFailed());

		assertAnnotatedException( tables.at( 0, 1, 0 ) );
		assertNotAnnotated( tables.at( 0, 1, 1 ) );
		assertAnnotatedStopped( tables.at( 0, 1, 2 ) );
		assertNotAnnotated( tables.at( 0, 2, 0 ) );
		assertNotAnnotated( tables.at( 0, 2, 1 ) );
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
        void commandReturningNothing();

        boolean commandThrowingException() throws Exception;

        Object commandTakingNoParameter();

        Object thatValueOfIs( String first );

        String thatBalanceOfAccountIs( String accountNumber );

        boolean openAccount( String accountNumber );

        boolean depositInAccount( String amount, String accountNumber );

        Object commandExpectingParameters( String first, String second, String third );

        Object commandExpectingASingleParameter( String p );

		Object theBalanceOfAccount( String accountNumber );

		Object actualMortgageRate();
    }

    public static class RightInterpreter extends AbstractInterpreter
    {
        public RightInterpreter( Fixture fixture )
        {
        }

        public void interpret( Specification specification )
        {
            execute( specification.nextExample().firstChild() );
        }

        public void execute( Example rows )
        {
            if (!rows.hasChild()) return;
            for (Example row : rows)
            {
                row.firstChild().annotate( new RightAnnotation() );
            }
        }
    }
}