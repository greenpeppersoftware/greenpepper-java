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
package com.greenpepper.reflect;

import com.greenpepper.Calculator;
import com.greenpepper.reflect.StaticInvocation;

import junit.framework.TestCase;

/**
 * ?: support for varargs methods
 */
public class StaticInvocationTest extends TestCase
{
    private Calculator calculator;

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(StaticInvocationTest.class);
    }

    protected void setUp() throws Exception
    {
        calculator = new Calculator();
    }

    public void testInvokesMethodOnTarget() throws Throwable
    {
        StaticInvocation sum = new StaticInvocation(calculator, Calculator.SUM);
        assertEquals( 5, sum.send("3", "2") );
    }

    public void testChecksInputsAgainstExpectedNumberOfArguments() throws Exception
    {
        StaticInvocation add = new StaticInvocation(calculator, Calculator.ADD);
        try
        {
            add.send("3", "2");
            fail( "Arguments mismatch not detected" );
        }
        catch (IllegalArgumentException expected)
        {
            assertTrue( true );
        }
    }
}
