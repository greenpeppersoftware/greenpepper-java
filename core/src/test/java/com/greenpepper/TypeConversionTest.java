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

import java.math.BigDecimal;
import com.greenpepper.converter.TypeConverter;
import junit.framework.TestCase;

public class TypeConversionTest
        extends TestCase
{
    public void testConversionForNullOrEmptyValue()
    {
        assertNull(TypeConversion.parse(null, Integer.class));
        assertNull(TypeConversion.parse("", Integer.class));
        assertNull(TypeConversion.parse(" ", Integer.class));
    }

    public void testComplainsWhenNoConverterMatches() 
    {
        try
        {
            TypeConversion.parse("value", Object.class);
            fail();
        }
        catch (UnsupportedOperationException expected)
        {
            assertTrue(true);
        }
    }

    public void testValueOfIsNoConverterMatches() 
    {
    	assertEquals(MyObject.DUMMY, TypeConversion.parse("dummy", MyObject.class));
    }

    public void testConversion()
    {
        assertEquals(5, TypeConversion.parse("5", Integer.class));
    }

    public void testMoreConvertersCanBeRegistered()
    {
        TypeConversion.register(new TypeConverter()
        {
            public Object parse(String value, Class type)
            {
                return new BigDecimal(value);
            }

            public boolean canConvertTo(Class type)
            {
                return BigDecimal.class.isAssignableFrom(type);
            }

			public String toString(Object value)
			{
				return String.valueOf(value);
			}

        });

        assertTrue(TypeConversion.parse("5", BigDecimal.class) instanceof BigDecimal);
    }

    public void testThatCanConvertObjectThatHaveAStaticParseStaringMethod()
    {
        MonetaryAmount fiveDollars = new MonetaryAmount(5);
        assertTrue(TypeConversion.parse("$5", MonetaryAmount.class) instanceof MonetaryAmount);
        assertEquals(fiveDollars, TypeConversion.parse("$5", MonetaryAmount.class));
    }

    public void testThatCanConvertAStringToAnArray()
    {
    	Integer[] sample = {1, 2, 3};

    	Integer[] converted = (Integer[])TypeConversion.parse("1, 2, 3", sample.getClass() );
    	assertNotNull(converted);
    	assertEquals(sample.length, converted.length );
    	assertEquals(sample[0], converted[0]);
    	assertEquals(sample[1], converted[1]);
    	assertEquals(sample[2], converted[2]);
    }

    public void testThatCanConvertArraysOfObjectThatHaveAStaticParseMethod()
    {
        MonetaryAmount[] dollars = new MonetaryAmount[0];

        dollars = (MonetaryAmount[])TypeConversion.parse("[$5, $20, $30]", dollars.getClass());
        assertEquals(3, dollars.length);
        assertEquals("$5", dollars[0].toString());
        assertEquals("$20", dollars[1].toString());
        assertEquals("$30", dollars[2].toString());
    }

    public void testThatCanConvertArrayOrSimpleType()
    {
    	assertEquals("", TypeConversion.toString(null));

    	assertEquals("10", TypeConversion.toString(new Integer(10)));
    	assertEquals("10", TypeConversion.toString(10));
    	assertEquals("$5", TypeConversion.toString(new MonetaryAmount(5)));


    	assertEquals("", TypeConversion.toString(new Integer[0]));
    	assertEquals("", TypeConversion.toString((Object)new String[0]));

    	assertEquals("10, 20, 30, 40", TypeConversion.toString(new Integer[]{10, 20, 30, 40}));
    	assertEquals("$5, $10", TypeConversion.toString(new MonetaryAmount[]{new MonetaryAmount(5), new MonetaryAmount(10)}));

    	assertEquals("1, , 3", TypeConversion.toString(new Integer[]{1, null, 3}));
    }

    public static class MyAutoConvertedClass
    {
    	private String val;

    	public MyAutoConvertedClass(String val)
    	{
    		this.val = val;
    	}

    	public static String toString(MyAutoConvertedClass value)
    	{
    		return "val=" + value.val;
    	}
    }

    public void testThatCanUseASelfRevertedClass()
    {
    	assertEquals("val=hello", TypeConversion.toString(new MyAutoConvertedClass("hello")));
    }
    
    public static class MyObject
    {
    	public static MyObject DUMMY = new MyObject();
    	
    	public static MyObject valueOf(String value)
    	{
    		return MyObject.DUMMY;
    	}
    }
}
