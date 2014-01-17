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
package com.greenpepper.util;

import java.util.Arrays;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public abstract class TestCase extends junit.framework.TestCase
{
    private Object it;

    public String getName()
    {
        String name = super.getName().replaceFirst( "test", "" );
        return NameUtils.humanize( name );
    }

    public void assertThat( Object value, Matcher c )
    {
        it = value;
        that( c.matches( value ), description( value, c ) );
    }

    public void andThatIt( Matcher c )
    {
        assertThat( it, c );
    }

    private void that( boolean value, String message )
    {
        junit.framework.Assert.assertTrue( message, value );
    }

    private String description( Object value, Matcher c )
    {
        StringBuffer message = new StringBuffer( "\nExpected: " );
        c.describeTo( new StringDescription(message) );
        message.append( "\nbut got: " ).append( value ).append( "\n" );
        return message.toString();
    }

    public void assertEquals( Object[] expected, Object[] actual )
    {
        StringBuffer message = new StringBuffer( "\nExpected: " );
        message.append( Arrays.toString( expected ) );
        message.append( "\nbut got: " ).append( Arrays.toString( actual ) ).append( "\n" );

        assertTrue( message.toString(), Arrays.equals( expected, actual ) );
    }

    public static void pass()
    {
        assertTrue( true );
    }
}
