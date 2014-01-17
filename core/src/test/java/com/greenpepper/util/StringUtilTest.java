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

import junit.framework.TestCase;

public class StringUtilTest extends TestCase
{
    public void testShouldDetectEmptyStrings()
    {
        assertTrue( StringUtil.isEmpty( "" ) );
        assertTrue( StringUtil.isEmpty( null ) );
        assertFalse( StringUtil.isEmpty( "allo" ) );
    }

	public void testStringEquality()
	{
		assertTrue( StringUtil.isEquals(null, null));
		assertTrue( StringUtil.isEquals("a", "a"));
		assertFalse( StringUtil.isEquals("a", null));
		assertFalse( StringUtil.isEquals(null, "b"));
		assertFalse( StringUtil.isEquals("a", "b"));
	}

	public void testShouldCompareStrings()
	{
		assertEquals(0, StringUtil.compare(null, null));
		assertEquals(0, StringUtil.compare("a", "a"));
		assertEquals(-1, StringUtil.compare(null, "b"));
		assertEquals(1, StringUtil.compare("a", null));
	}

	public void testShouldConvertEmptyStringToNullRef()
	{
		assertNull(StringUtil.toNullIfEmpty(null));
		assertNull(StringUtil.toNullIfEmpty(""));
		assertNotNull(StringUtil.toNullIfEmpty("a"));
	}

	public void testShouldConvertNullToEmpty()
	{
		assertEquals("", StringUtil.toEmptyIfNull(""));
		assertEquals("a", StringUtil.toEmptyIfNull("a"));
		assertEquals("", StringUtil.toEmptyIfNull(null));
	}

    public void testShouldRemoveDiacritics()
    {
        assertEquals("aaa", StringUtil.removeDiacritics("a\u00e0\u00e2"));
        assertEquals("eeee", StringUtil.removeDiacritics("e\u00e9\u00e8\u00ea"));
        assertEquals("cc", StringUtil.removeDiacritics("c\u00e7"));
    }

    public void testEscapingSemiColumn()
    {
        assertNull(StringUtil.escapeSemiColon( null ));
        assertEquals("", StringUtil.escapeSemiColon( "" ));
        assertEquals("a", StringUtil.escapeSemiColon( "a" ));
        assertEquals("a%3Bb", StringUtil.escapeSemiColon( "a;b" ));
        assertEquals("a%3Bb%3Bc", StringUtil.escapeSemiColon( "a;b;c" ));

        assertNull(StringUtil.unescapeSemiColon( null ));
        assertEquals("", StringUtil.unescapeSemiColon( "" ));
        assertEquals("a", StringUtil.unescapeSemiColon( "a" ));
        assertEquals("a;b", StringUtil.unescapeSemiColon( "a%3Bb" ));
        assertEquals("a;b;c", StringUtil.unescapeSemiColon( "a%3Bb%3Bc" ));
    }
}
