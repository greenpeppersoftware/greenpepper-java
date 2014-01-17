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
package com.greenpepper.document;

import com.greenpepper.Example;
import static com.greenpepper.util.Tables.parse;
import junit.framework.TestCase;

public class CommentTableFilterTest extends TestCase
{
    private CommentTableFilter filter;

    public void setUp()
    {
        filter = new CommentTableFilter();
    }

    public void testSkipsInformativeSections()
    {
        Example tables = parse(
            "[First table]\n" +
            "****\n" +
            "[Begin Info]\n" +
            "****\n" +
            "[Should be skipped]\n" +
            "****\n" +
            "[End Info]\n" +
            "****\n" +
            "[Next table]\n" +
            "****\n" +
            "[Begin Info]\n" +
            "****\n" +
            "[Should be skipped too]\n" +
            "****\n" +
            "[End Info]\n" +
            "****\n" +
            "[Begin Info]\n" +
            "****\n" +
            "[Should be skipped]\n" +
            "****\n" +
            "[End Info]\n" +
            "****\n" +
            "[Last table]" );

        assertEquals( "First table", tables.at( 0, 0, 0 ).getContent() );
        tables = filter.filter( tables.nextSibling() );
        assertEquals( "Next table", tables.at( 0, 0, 0 ).getContent() );
        tables = filter.filter( tables.nextSibling() );
        tables = filter.filter( tables );
        assertEquals( "Last table", tables.at( 0, 0, 0 ).getContent() );
    }
}
