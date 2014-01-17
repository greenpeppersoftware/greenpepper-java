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
package com.greenpepper;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import com.greenpepper.annotation.RightAnnotation;
import com.greenpepper.annotation.WrongAnnotation;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.ResultIs;
import com.greenpepper.expectation.ShouldBe;
import com.greenpepper.reflect.StaticInvocation;
import com.greenpepper.util.TestCase;

public class CallTest extends TestCase
{
	private Mockery context = new JUnit4Mockery();
    private Calculator calculator = new Calculator();

    public void testIgnoresIfNoExpectationIsSet() throws Exception
    {
        Call call = new Call( new StaticInvocation( calculator, Calculator.TOTAL ) );
        call.execute();
        assertTrue( call.wasIgnored() );
    }

    public void testReportsASuccessIfOuputMatchesExpectation() throws Exception
    {
        Call call = new Call( new StaticInvocation( calculator, Calculator.SUM ) );
        call.addInput( "5", "2" );
        call.expect( ShouldBe.equal( 7 ) );

        final Annotatable annotatable = context.mock( Annotatable.class );
		context.checking(new Expectations()
		{{
			one(annotatable).annotate(with(any(RightAnnotation.class)));
		}});
        call.will( Annotate.right( annotatable ) ).when( ResultIs.right() );
        call.execute();
		context.assertIsSatisfied();
    }

    public void testReportsAFailureIfOuputIsUnexpected() throws Exception
    {
        Call call = new Call( new StaticInvocation( calculator, Calculator.ADD ) );
        call.addInput( "3" );
        call.expect( ShouldBe.equal( 3 ) );

		final Annotatable annotatable = context.mock( Annotatable.class );
		context.checking(new Expectations()
		{{
			one(annotatable).annotate(with(any(WrongAnnotation.class)));
		}});
        call.will( Annotate.wrong( annotatable ) ).when( ResultIs.wrong() );
        call.execute();
		context.assertIsSatisfied();
    }

    public void testAcceptsExceptionsAsValidExpectations() throws Exception
    {
        StaticInvocation divide = new StaticInvocation( calculator, Calculator.DIVIDE );
        Call call = new Call( divide );
        call.addInput( "7", "0" );
        call.expect( ShouldBe.instanceOf( ArithmeticException.class ) );

        call.execute();
        assertTrue( call.wasRight() );
    }

    public void testUnexpectedExceptionsAreReportedAsErrors() throws Exception
    {
        StaticInvocation error = new StaticInvocation( calculator, Calculator.MULTIPLY );
        Call call = new Call( error );

        try
        {
            call.execute();
            fail();
        }
        catch (IllegalArgumentException expected)
        {
            assertTrue( true );
        }
    }

    public void testConvertsExpectationValueToActionReturnType() throws Exception
    {
        Call call = new Call( new StaticInvocation( calculator, Calculator.SUM ) );
        call.addInput( "2", "1" );
        call.expect( "3" );
        call.execute();
        assertTrue( call.wasRight() );
    }
}