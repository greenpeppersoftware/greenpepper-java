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

public class NameUtilsTest extends TestCase
{
    public void testCanConvertAStringToLowerCamelCase()
    {
        assertEquals( "aLongClassName", NameUtils.toLowerCamelCase( "a lONG class nAMe" ) );
        assertEquals( "alongclassname", NameUtils.toLowerCamelCase( "aLongClassName" ) );
    }

    public void testCanConvertAStringToUpperCamelCase()
    {
        assertEquals( "ALongClassName", NameUtils.toUpperCamelCase( "a lONG class nAMe" ) );
    }

    public void testDecapitalizesNamesToValidJavaIdentifiers()
    {
        assertEquals( "URL", NameUtils.decapitalize( "URL" ) );
        assertEquals( "anURL", NameUtils.decapitalize( "an URL" ) );
        assertEquals( "anURL", NameUtils.decapitalize( "anURL" ) );

        assertEquals( "anUrl", NameUtils.decapitalize( "an url" ) );
    }

    public void testCapitalizesWordsToBuildClassNameFromWordList()
    {
        assertEquals( "ALongClassName", NameUtils.toClassName( " A   Long Class Name " ) );
        assertEquals( "ALongClassName", NameUtils.toClassName( "a long class name" ) );
    }

    public void testShouldRemoveDiacriticsFromClassNames()
    {
        assertEquals( "AClassNameWithoutDiacritics",
                NameUtils.toClassName( "\u00c0 Cl\u00e2ss Nam\u00e9 With\u00f4ut Di\u00e0critics" ));
    }

    public void testDoesNotCapitalizeFullyQualifiedClassNames()
    {
        assertEquals( "com.pyxis.test.Foo", NameUtils.toClassName( "com.pyxis.test.Foo" ) );
    }

    public void testCanTellIfAnIdentifierIsAValidJavaIdentifier()
    {
        assertTrue( NameUtils.isJavaIdentifier( "valididentifierName" ) );
        assertFalse( NameUtils.isJavaIdentifier( "0invalididentifierName" ) );
    }

    public void testHumanizesJavaIdentifierNames()
    {
        assertEquals( "an identifier name", NameUtils.humanize( "anIdentifierName" ) );
    }

    public void testHumanizesJavaClassNames()
    {
        assertEquals( "A class name", NameUtils.humanize( "AClassName" ) );
    }

    public void testToJavaIdentiferForm()
    {
        assertEquals( "aClassName", NameUtils.toJavaIdentifierForm( "\u00c0 Cl\u00e2ss N\u00e4m\u00e9" ));
    }

	public void testRemovingNonJavaIdentifiers() {
		assertEquals("AlongclassName", NameUtils.removeNonJavaIdentifierCharacters("A long class Name"));
		assertEquals("ab", NameUtils.removeNonJavaIdentifierCharacters("a'b"));
	}
}