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
import junit.framework.TestCase;

/**
 * ?: what about static fields
 * ?: support for varargs
 */
public class FieldWriterTest extends TestCase
{
    private Calculator calculator;

    public FieldWriterTest( String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        calculator = new Calculator();
    }

    public void testAssignsValueToFieldOfTarget() throws Throwable
    {
        FieldWriter assignment = new FieldWriter( calculator, Calculator.X );
        assignment.send( "5" );
        assertEquals( 5, calculator.x );
    }

    public void testExpectsOneAndOnlyOneArgument() throws Throwable
    {
        FieldWriter assignment = new FieldWriter( calculator, Calculator.X );
        try
        {
            assignment.send();
            fail( "Should expect at least 1 argument" );
        }
        catch (Exception expected)
        {
            assertTrue( true );
        }
        try
        {
            assignment.send( "2", "3" );
            fail( "Should expect at most 1 argument" );
        }
        catch (Exception expected)
        {
            assertTrue( true );
        }
    }
}