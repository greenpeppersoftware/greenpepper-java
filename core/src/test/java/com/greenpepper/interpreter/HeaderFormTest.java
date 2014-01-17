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

import static org.hamcrest.Matchers.is;

import com.greenpepper.interpreter.column.Column;
import com.greenpepper.interpreter.column.ExpectedColumn;
import com.greenpepper.interpreter.column.GivenColumn;
import com.greenpepper.interpreter.column.NullColumn;
import com.greenpepper.interpreter.column.RecalledColumn;
import com.greenpepper.interpreter.column.SavedColumn;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.TestCase;

public class HeaderFormTest extends TestCase
{
    public void testExpectedColumnsEndWithQuestionMarkOrParentheses() throws Exception
    {
        assertThat( header( "expected?" ), is( ExpectedColumn.class) );
        assertThat( header( "expected()" ), is( ExpectedColumn.class) );
    }

    public void testSavedColumnsEndWithEqualSign() throws Exception
    {
        assertThat( header( "saved?=" ), is( SavedColumn.class) );
        assertThat( header( "saved=" ), is( SavedColumn.class) );
    }

    public void testRecalledColumnsStartWithEqualSign() throws Exception
    {
        assertThat( header( "=recalled" ), is( RecalledColumn.class) );
    }

    public void testGivenColumnsAreTheDefault() throws Exception
    {
        assertThat( header( "given" ), is ( GivenColumn.class) );
    }

    public void testBlankHeaderYieldsNullColumn() throws Exception
    {
        assertThat( header( null ), is( NullColumn.class ) );
        assertThat( header( "" ), is( NullColumn.class ) );
        assertThat( header( "    " ), is( NullColumn.class ) );
    }

    public void testStripsSpecialMarkers()
    {
        assertEquals( "expected", HeaderForm.parse( "expected?" ).message() );
        assertEquals( "expected", HeaderForm.parse( "expected()" ).message() );
        assertEquals( "saved", HeaderForm.parse( "saved=" ).message() );
        assertEquals( "saved", HeaderForm.parse( "saved?=" ).message() );
        assertEquals( "saved", HeaderForm.parse( "saved()=" ).message() );

        assertEquals( "given", HeaderForm.parse( "given" ).message() );
    }

    private Column header(String text)
            throws Exception
    {
        return HeaderForm.parse( text ).selectColumn( dummyFixture() );
    }

    private PlainOldFixture dummyFixture()
    {
        return new PlainOldFixture( new Object( ) {
            public String expected;
            public String saved;
            public String given;
            public String recalled;
        });
    }
}
