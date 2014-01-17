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

import java.util.Locale;

import static org.hamcrest.Matchers.is;

import com.greenpepper.interpreter.AbstractInterpreter;
import com.greenpepper.interpreter.RuleForInterpreter;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.TestCase;

public class GreenPepperTest extends TestCase
{
    protected void tearDown() throws Exception {
        GreenPepper.setLocale( Locale.ENGLISH );
    }

    public void testCanFindAnInterpreterGivenAClassName()
    {
        assertTrue( GreenPepper.isAnInterpreter( "com.greenpepper.interpreter.RuleForInterpreter" ) );
        assertFalse( GreenPepper.isAnInterpreter( "MissingInterpreter" ) );
    }

    public void testCanInstantiateAnInterpreterGivenAClassName() throws Throwable
    {
        Interpreter interpreter = GreenPepper.getInterpreter( "com.greenpepper.interpreter.RuleForInterpreter", dummyFixture() );
        assertThat( interpreter, is( RuleForInterpreter.class ) );
    }

    public void testComplainsWhenInterpreterCannotBeFound()
    {
        try
        {
            GreenPepper.getInterpreter( "MissingInterpreter" );
            fail();
        }
        catch (Throwable e)
        {
            assertTrue( true );
        }
    }

    public void testComplainsWhenInterpreterCannotBeInstantiated()
    {
        try
        {
            GreenPepper.getInterpreter( BadInterpreter.class.getName() );
            fail();
        }
        catch (Throwable e)
        {
            assertTrue( true );
        }
    }

    public void testInterpreterClassNamesCanBeAliased()
    {
        GreenPepper.aliasInterpreter( "Calculate", RuleForInterpreter.class.getName() );
        assertTrue( GreenPepper.isAnInterpreter( "Calculate" ) );
    }

    public void testShouldFindCoreInterpetersUsingTheirHumanName()
    {
        assertTrue( GreenPepper.isAnInterpreter( "rule for" ) );
    }

    private Fixture dummyFixture()
    {
        return new PlainOldFixture( new Object() );
    }

    public void testOnlyClassesThatImplementsInterpreterShouldBeConsideredInterpreters()
    {
        assertFalse( GreenPepper.isAnInterpreter( String.class.getName() ) );
    }

    public void testInterpreterInterfaceShouldNotBeConsideredAnInterpreter()
    {
        assertFalse( GreenPepper.isAnInterpreter( Interpreter.class.getName() ) );
    }

    public void testThatIfKeyNotInBundleTheKeyIsReturned()
    {
        String key = "a.key";
        assertEquals( key, GreenPepper.$( key ) );
    }

    public void testThatDefaultLocaleIsUsedToResolveKeys()
    {
        GreenPepper.setLocale( Locale.ENGLISH );

        assertEquals( "Expected", GreenPepper.$( "expected" ) );
        GreenPepper.setLocale( Locale.FRANCE );
        assertEquals( "Attendu", GreenPepper.$( "expected" ) );
    }

    public static class BadInterpreter extends AbstractInterpreter
    {
        // cannot be instantiated.
        private BadInterpreter()
        {
            super();
        }

        public void interpret( Specification specification )
        {
        }
    }
}
