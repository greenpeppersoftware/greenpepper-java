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

import com.greenpepper.interpreter.column.Column;
import com.greenpepper.interpreter.column.ExpectedColumn;
import com.greenpepper.interpreter.column.GivenColumn;
import com.greenpepper.interpreter.column.NullColumn;
import com.greenpepper.interpreter.column.RecalledColumn;
import com.greenpepper.interpreter.column.SavedColumn;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.util.StringUtil;

public final class HeaderForm
{
    private final String text;

    public static HeaderForm parse(String text)
    {
        return new HeaderForm( text );
    }

    private HeaderForm(String text)
    {
        this.text = text;
    }

    public boolean isGiven()
    {
        return !isExpected() && !isSaved();
    }

    public boolean isExpected()
    {
        return header().endsWith( "()" ) || header().endsWith( "?" );
    }

    private String header()
    {
        return text.trim();
    }

    public boolean isSaved()
    {
        return header().endsWith( "=" );
    }

    public boolean isRecalled()
    {
        return header().startsWith( "=" );
    }

    public String message()
    {
        return header().replaceAll( "=", "" ).replaceAll( "\\?", "" ).replaceAll( "\\(\\)", "" );
    }

    public Column selectColumn(Fixture fixture) throws Exception
    {
        if (isNull()) return new NullColumn();

        if (isSaved()) return new SavedColumn( fixture.check( message() ) );
        if (isExpected()) return new ExpectedColumn( fixture.check( message() ) );
        if (isRecalled()) return new RecalledColumn( fixture.send( message() ));

        return new GivenColumn( fixture.send( message() ) );
    }

    public boolean isNull()
    {
        return StringUtil.isBlank( text );
    }
}
