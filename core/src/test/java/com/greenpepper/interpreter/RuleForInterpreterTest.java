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

import static com.greenpepper.Assertions.assertAnnotatedException;
import static com.greenpepper.Assertions.assertAnnotatedIgnored;
import static com.greenpepper.Assertions.assertAnnotatedRight;
import static com.greenpepper.Assertions.assertAnnotatedWrongWithDetails;
import static com.greenpepper.Assertions.assertNotAnnotated;
import static com.greenpepper.util.Tables.parse;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.greenpepper.AlternateCalculator;
import com.greenpepper.Assertions;
import com.greenpepper.GreenPepper;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.FixtureWithRowAndFirstExpectationAnnotation;
import com.greenpepper.util.Tables;
import com.greenpepper.util.TestCase;

public class RuleForInterpreterTest
    extends TestCase
{
    private AlternateCalculator calculator;
    private RuleForInterpreter interpreter;
    private Tables tables;

	@Override
    protected void setUp() throws Exception
    {
        super.setUp();
        calculator = new AlternateCalculator();
        interpreter = new RuleForInterpreter( new PlainOldFixture( calculator ) );
		GreenPepper.setStopOnFirstFailure(false); //reset
    }

	@Override
	protected void tearDown() throws Exception
	{
		GreenPepper.setStopOnFirstFailure(false); // reset
	}

    public void testColumnHeaderSpecifiesAnInputValue() throws Exception
    {
        tables = parse(
            "[rule for][calculator]\n" +
            "[a][b]\n" +
            "[5][3]"
        );

        interpreter.interpret( spec() );
        assertEquals( 5, calculator.a );
        assertEquals( 3, calculator.b );
    }

    public void testShouldTestColumnWhenHeaderEndsWithQuestionMark() throws Exception
    {
        tables = parse(
            "[rule for][calculator]\n" +
            "[sum?][product?]\n" +
            "[5]   [7]"
        );
        calculator.a = 3;
        calculator.b = 2;
        interpreter.interpret( spec() );
        assertAnnotatedRight( tables.at( 0, 2, 0 ) );
        assertAnnotatedWrongWithDetails( tables.at( 0, 2, 1 ) );
    }

    public void testWillMarkCellInErrorWhenExceptionOccursInTest() throws Exception
    {
        tables = parse(
            "[rule for][calculator]\n" +
            "[division?]\n" +
            "[3]"
        );

        calculator.a = 3;
        calculator.b = 0;
        interpreter.interpret( spec() );
        assertAnnotatedException( tables.at( 0, 2, 0 ) );
    }

    public void testSkipsColumnWhenHeadingIsInvalid() throws Exception
    {
        tables = parse(
            "[rule for][calculator]\n" +
            "[no such input]\n" +
            "[3]"
        );
        interpreter.interpret( spec() );
        assertAnnotatedException( tables.at( 0, 1, 0 ) );
        assertNotAnnotated( tables.at( 0, 2, 0 ) );
    }

    public void testShouldIgnoreCellIfEmpty() throws Exception
    {
        tables = parse(
            "[rule for][calculator]\n" +
            "[sum?]\n" +
            "[]"
        );
        interpreter.interpret( spec() );
        assertAnnotatedIgnored( tables.at( 0, 2, 0 ) );
    }

    public void testExtraColumnsAreSilentlyIgnored() throws Exception
    {
        tables = parse(
            "[rule for][calculator]\n" +
            "[a][b][sum?]\n" +
            "[2][3][5][7]"
        );
        interpreter.interpret( spec() );
    }

    public void testShouldProcessAllRows() throws Exception
    {
        tables = parse(
            "[rule for][calculator]\n" +
            "[a][b][sum?][division?]\n" +
            "[6][2][8][3]\n" +
            "[10][5][15][2]"
        );
        interpreter.interpret( spec() );

        assertAnnotatedRight( tables.at( 0, 2, 2 ) );
        assertAnnotatedRight( tables.at( 0, 2, 3 ) );
        assertAnnotatedRight( tables.at( 0, 3, 2 ) );
        assertAnnotatedRight( tables.at( 0, 3, 3 ) );
    }

    public void testShouldCompileStatistics() throws Exception
    {
        tables = parse(
            "[rule for][calculator]\n" +
            "[a][b][sum?][division?]\n" +
            "[9][3][12][2]\n" +
            "[6][2][8][2]\n" +
            "[10][0][][0]"
        );
        FakeSpecification spec = spec();
        interpreter.interpret( spec );

		assertThat( spec.stats.rightCount(), is(equalTo( 2 ) ) );
		assertThat( spec.stats.wrongCount(), is(equalTo( 2 ) ) );
		assertThat( spec.stats.exceptionCount(), is(equalTo( 1 ) ) );
		assertThat( spec.stats.ignoredCount(), is(equalTo( 1 ) ) );
    }

    public void testCanProcessATableWithNoRowsAfterHeader()
    {
        tables = parse(
            "[rule for][calculator]\n" +
            "[a][b][sum?][division?]"
        );

        FakeSpecification document = spec();
        interpreter.interpret( document );

        assertThat( document.stats.rightCount(), equalTo( 0 ) );
        assertThat( document.stats.wrongCount(), is(equalTo( 0 ) ) );
        assertThat( document.stats.exceptionCount(), is(equalTo( 0 ) ) );
        assertThat( document.stats.ignoredCount(), is(equalTo( 0 ) ) );
    }

    public void testWillSaveResultInVariableAndDisplayItWhenColumnHeaderEndsWithEqualSign()
    {
        tables = parse(
            "[rule for][calculator]\n" +
            "[a][b][sum?][division?=]\n" +
            "[9][3][12][quotient]"
        );

        FakeSpecification document = spec();
        interpreter.interpret( document );

        assertThat( document.stats.exceptionCount(), is(equalTo( 0 ) ) );
        Assertions.assertAnnotatedIgnored( tables.at(0, 2, 3) );

        assertEquals( 3, document.getVariable( "quotient") );
    }

    public void testWillRecallVariableAndUseItAsInputWhenColumnHeaderStartsWithEqualSign()
    {
        tables = parse(
            "[rule for][calculator]\n" +
            "[=a][b][product?]\n" +
            "[quotient][3][18]"
        );

        FakeSpecification document = spec();
        document.setVariable( "quotient", 6 );
        interpreter.interpret( document );

        assertThat( document.stats.exceptionCount(), is(equalTo( 0 ) ) );
        assertAnnotatedRight( tables.at( 0, 2, 2) );
    }

	public void testThatHeaderContainingUnresolvableMethodIsAnnotatedStoppedWhenOptionsIsStopOnFirstFailure()
	{
		GreenPepper.setStopOnFirstFailure(true);
		
		tables = parse(
			"[rule for][calculator]\n" +
			"[a][z]\n" +
			"[5][3]"
		);

		FakeSpecification document = spec();
		interpreter.interpret( document );

		assertTrue(document.stats().hasFailed());
		Assertions.assertAnnotatedStopped( tables.at(0,1,2) );
	}

	public void testThatCellContainingBadValueIsAnnotatedStoppedWhenOptionsIsStopOnFirstFailure()
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = parse(
			"[rule for][calculator]\n" +
			"[a][b]\n" +
			"[fail][3]"
		);

		FakeSpecification document = spec();
		interpreter.interpret( document );

		assertTrue(document.stats().hasFailed());
		Assertions.assertAnnotatedStopped( tables.at(0,2,2) );
	}
	
	public void testThatTheBeforeFirstExpectationIsCalledBeforeTheFirstExpectation() {
		GreenPepper.setStopOnFirstFailure(true);

		FixtureWithRowAndFirstExpectationAnnotation fixture = new FixtureWithRowAndFirstExpectationAnnotation();
        interpreter = new RuleForInterpreter( new PlainOldFixture( fixture ) );
		
		tables = parse(
			"[rule for][myOwnFixture]\n" +
			"[a][b?]\n" +
			"[3][3]\n" + 
			"[2][2]"
		);

		FakeSpecification document = spec();
		interpreter.interpret( document );

		assertFalse("beforeExpectation was not call on all lines", document.stats().hasFailed());
	}

	public void testThatTheBeforeRowIsCalled() {
		GreenPepper.setStopOnFirstFailure(true);

		FixtureWithRowAndFirstExpectationAnnotation fixture = new FixtureWithRowAndFirstExpectationAnnotation();
        interpreter = new RuleForInterpreter( new PlainOldFixture( fixture ) );
		
		tables = parse(
			"[rule for][myOwnFixture]\n" +
			"[c?]\n" +
			"[1]\n" + 
			"[2]"
		);

		FakeSpecification document = spec();
		interpreter.interpret( document );

		assertFalse("beforeRow should increment c by 1", document.stats().hasFailed());
	}

	public void testThatTheAfterRowIsCalled() {
		GreenPepper.setStopOnFirstFailure(true);

		FixtureWithRowAndFirstExpectationAnnotation fixture = new FixtureWithRowAndFirstExpectationAnnotation();
        interpreter = new RuleForInterpreter( new PlainOldFixture( fixture ) );
		
		tables = parse(
			"[rule for][myOwnFixture]\n" +
			"[c?][d?]\n" +
			"[1][1]\n" + 
			"[2][1]"
		);

		FakeSpecification document = spec();
		interpreter.interpret( document );

		assertFalse("afterRow should reset d to 0", document.stats().hasFailed());
	}
	
    private FakeSpecification spec()
    {
        return new FakeSpecification( tables );
    }
}
