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

package com.greenpepper.expectation;

import com.greenpepper.TypeConversion;
import com.greenpepper.util.FactoryMethod;

import java.util.regex.Pattern;

/**
 * @version $Revision: $ $Date: $
 */
public class DuckExpectation implements Expectation
{
    private final String expected;

    @FactoryMethod
    public static DuckExpectation create( String expected )
    {
        return new DuckExpectation( expected );
    }

    public DuckExpectation( String expected )
    {
        this.expected = removeDoubleQuotes( expected );
    }

    private String removeDoubleQuotes( String expected )
    {
        if (Pattern.matches( "(?s)^\\s*\".*?\"\\s*$", expected ))
        {
            return expected.trim().substring( 1, expected.length() - 1 );
        }
        return expected;
    }

    public StringBuilder describeTo( StringBuilder sb )
    {
        return sb.append( expected );
    }

    public boolean meets( Object result )
    {
        Object expectedValue = canCoerceTo( result ) ? coerceTo( result ) : expected;
        return ShouldBe.equal( expectedValue ).meets( result );
    }

    private Object coerceTo( Object result )
    {
        return TypeConversion.parse( expected, result.getClass() );
    }

    private boolean canCoerceTo( Object result )
    {
        return result != null && TypeConversion.supports( result.getClass() );
    }

    public String toString()
    {
        return expected;
    }
}
