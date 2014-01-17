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

package com.greenpepper.extensions.ognl;

import java.util.Collection;

import com.greenpepper.util.NameUtils;
import junit.framework.TestCase;

public class OgnlResolutionTest extends TestCase
{
    public void testThatWhenNoExpressionIsSpecifiedItComplain()
    {
        try
        {
            OgnlResolution resolution = new OgnlResolution(" ");
            resolution.expressionsListToResolve();
            fail();
        }
        catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
    }

    public void testThatOneTokenCodedAloneReturnThatToken()
    {
        String expression = "alone";
        OgnlResolution resolution = new OgnlResolution(expression);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(1, expressions.size());
        assertTrue(expressions.contains(expression));
    }

    public void testExpressionsGeneratedFromTwoTokens()
    {
        String token1 = "token1";
        String token2 = "token2";
        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(3, expressions.size());
        assertTrue(expressions.contains(token1 + "." + token2));
        assertTrue(expressions.contains(NameUtils.toLowerCamelCase(token1 + " " + token2)));
        assertTrue(expressions.contains(token1 + " " + token2));
    }


    public void testExpressionsGeneratedFromThreeTokens()
    {
        String token1 = "token1";
        String token2 = "token2";
        String token3 = "token3";
        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2 + " " + token3);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(5, expressions.size());
        assertTrue(expressions.contains(token1 + "." + token2 + "." + token3));
        assertTrue(expressions.contains(token1 + "." + NameUtils.toLowerCamelCase(token2 + " " + token3)));
        assertTrue(expressions.contains(NameUtils.toLowerCamelCase(token1 + " " + token2) + "." + token3));
        assertTrue(expressions.contains(NameUtils.toLowerCamelCase(token1 + " " + token2 + " " + token3)));
        assertTrue(expressions.contains(token1 + " " + token2 + " " + token3));
    }

    public void testExpressionsGeneratedFromTokensWithSpaces()
    {
        String token1 = "token1";
        String token2 = "token2";
        OgnlResolution resolution = new OgnlResolution("  " + token1 + "  " + token2 + "  ");
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(3, expressions.size());
        assertTrue(expressions.contains(token1 + "." + token2));
        assertTrue(expressions.contains(NameUtils.toLowerCamelCase(token1 + " " + token2)));
        assertTrue(expressions.contains(token1 + " " + token2));
    }

    public void testFirstTokenIsNotStartingAsAJavaIdentifier()
    {
        String token1 = "#token1";
        String token2 = "token2";

        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(2, expressions.size());
        assertTrue(expressions.contains(token1 + "." + token2));
        assertTrue(expressions.contains(token1 + " " + token2));
    }

    public void testTwoTokensWithSecondOneNotStartingAsAJavaIdentifier()
    {
        String token1 = "token1";
        String token2 = "#token2";

        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(1, expressions.size());
        assertTrue(expressions.contains(token1 + " " + token2));
    }

    public void testTwoTokensNotStartingAsAJavaIdentifier()
    {
        String token1 = "#token1";
        String token2 = "#token2";

        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(1, expressions.size());
        assertTrue(expressions.contains(token1 + " " + token2));
    }

    public void testThreeTokensWithOneNotIdentifier()
    {
        for (int i = 0; i < 3; i++)
        {
            String expression = "";
            for  (int x = 0; x < 3; x++)
            {
                if (x == i) expression += "#";
                expression += "token ";
            }

            OgnlResolution resolution = new OgnlResolution(expression);
            Collection<String> expressions = resolution.expressionsListToResolve();

            int size = 0;

            switch (i)
            {
                case 0: size = 5; break;
                case 1: size = 2; break;
                case 2: size = 3; break;
            }

            assertEquals(size, expressions.size());
        }
    }

    public void testThreeTokensWithTwoNotIdentifier()
    {
        for (int i = 0; i < 3; i++)
        {
            String expression = "";
            for  (int x = 0; x < 3; x++)
            {
                if (x != i) expression += "#";
                expression += "token ";
            }

            OgnlResolution resolution = new OgnlResolution(expression);
            Collection<String> expressions = resolution.expressionsListToResolve();

            int size = 0;

            switch (i)
            {
                case 0: size = 1; break;
                case 1: size = 2; break;
                case 2: size = 2; break;
            }

            assertEquals(size, expressions.size());
        }
    }

    public void testThreeTokensNotIdentifiers()
    {
        String token1 = "#token1";
        String token2 = "#token2";
        String token3 = "#token3";

        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2 + " " + token3);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(1, expressions.size());
        assertTrue(expressions.contains(token1 + " " + token2 + " " + token3));
    }

    public void testExpressionWithFormat()
    {
        OgnlResolution resolution = new OgnlResolution("call of method(parameter)");
        Collection<String> expressions = resolution.expressionsListToResolve("format(%s)");

        for (String expression : expressions)
        {
            assertTrue(expression.startsWith("format("));
        }
    }
}
