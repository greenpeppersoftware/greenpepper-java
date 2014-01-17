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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.greenpepper.converter.ArrayConverter;
import com.greenpepper.converter.BigDecimalConverter;
import com.greenpepper.converter.BooleanConverter;
import com.greenpepper.converter.DateConverter;
import com.greenpepper.converter.DoubleConverter;
import com.greenpepper.converter.EnumConverter;
import com.greenpepper.converter.FloatConverter;
import com.greenpepper.converter.IntegerConverter;
import com.greenpepper.converter.LongConverter;
import com.greenpepper.converter.PrimitiveBoolArrayConverter;
import com.greenpepper.converter.PrimitiveDoubleArrayConverter;
import com.greenpepper.converter.PrimitiveFloatArrayConverter;
import com.greenpepper.converter.PrimitiveIntArrayConverter;
import com.greenpepper.converter.PrimitiveLongArrayConverter;
import com.greenpepper.converter.StringConverter;
import com.greenpepper.converter.TypeConverter;
import com.greenpepper.interpreter.flow.scenario.ExpectationTypeConverter;
import com.greenpepper.util.ClassUtils;

public final class TypeConversion
{
    private static final List<TypeConverter> converters = new LinkedList<TypeConverter>();

    static
    {
		converters.add( new ExpectationTypeConverter() );
		converters.add( new EnumConverter() );
		converters.add( new IntegerConverter() );
		converters.add( new BigDecimalConverter() );
        converters.add( new LongConverter() );
        converters.add( new FloatConverter() );
        converters.add( new DoubleConverter() );
        converters.add( new DateConverter() );
        converters.add( new BooleanConverter() );
        converters.add( new ArrayConverter() );
        converters.add( new PrimitiveIntArrayConverter() );
        converters.add( new PrimitiveLongArrayConverter() );
        converters.add( new PrimitiveDoubleArrayConverter() );
        converters.add( new PrimitiveFloatArrayConverter() );
        converters.add( new PrimitiveBoolArrayConverter() );
        converters.add( new StringConverter() );
    }

    private TypeConversion() {}

    public static void register( TypeConverter converter )
    {
        converters.add( 0, converter );
    }

    public static boolean supports( Class type )
    {
        return converterRegisteredFor( type ) || canSelfConvert( "parse", type ) || canSelfConvert( "valueOf", type );
    }

    private static boolean converterRegisteredFor( Class type )
    {
        return converterForType( type ) != null;
    }

    private static boolean canSelfConvert( String parsingMethod, Class type )
    {
        try
        {
            Method method = type.getMethod( parsingMethod, String.class );
            return type.isAssignableFrom( method.getReturnType() )
                   && ClassUtils.isPublic( method )
                   && ClassUtils.isStatic( method );
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Converts <code>value</code> to the object type of <code>type</code> by
     * using the appropriate <code>TypeConverter</code>.
     *
     * @param value The string value to convert
     * @param type  The type to convert to
     * @return The converted value
     */
    public static Object parse( String value, Class type )
    {
        if (canSelfConvert( "parse", type ))
            return selfConvert( "parse", value, type );
        if (converterRegisteredFor( type ))
            return converterForType( type ).parse( value, type );
        if (canSelfConvert( "valueOf", type ))
            return selfConvert( "valueOf", value, type );
        
        throw new UnsupportedOperationException( "No converter registered for: " + type.getName() );
    }

    /**
     * SelfConversion implies that if a class has the given static method named
     * that receive a String and that returns a instance of the
     * class, then it can serve for conversion purpose.
     */
    private static Object selfConvert( String parsingMethod, String value, Class type )
    {
        try
        {
            Method method = type.getMethod( parsingMethod, String.class );
            return method.invoke( null, value );
        }
        catch (InvocationTargetException e)
        {
            throw new IllegalArgumentException( "Can't convert " + value + " to " + type.getName(), e.getCause() );
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException( "Can't convert " + value + " to " + type.getName(), e );
        }
    }

    /**
     * Converts <code>value</code> to a String by
     * using the appropriate <code>TypeConverter</code>.
     *
     * @param value The object value to convert
     * @return The string value
     */
    public static String toString( Object value )
    {
    	if (value == null) return "";

    	Class type = value.getClass();

        if (canSelfRevert( type ))
            return selfRevert( value);
        if (converterRegisteredFor( type ))
            return converterForType( type ).toString( value);

		return String.valueOf(value);
    }

	private static boolean canSelfRevert( Class type )
    {
        try
        {
            Method method = type.getMethod( "toString", type);
            return String.class.isAssignableFrom( method.getReturnType() )
                   && ClassUtils.isPublic( method )
                   && ClassUtils.isStatic( method );
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private static String selfRevert( Object value )
    {
    	Class type = value.getClass();
        try
        {
            Method method = type.getMethod( "toString", type);
            return (String)method.invoke( null, value );
        }
        catch (InvocationTargetException e)
        {
            throw new IllegalArgumentException( "Can't get a string for " + value + " of to " + type.getName(), e.getCause() );            
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException( "Can't get a string for " + value + " of to " + type.getName(), e );
        }
    }


    public static TypeConverter converterForType( Class type )
    {
        for (TypeConverter converter : converters)
        {
            if (converter.canConvertTo( type )) return converter;
        }

        return null;
    }

    public static Object[] convert( String[] values, Class[] toTypes )
    {
        Object[] converted = new Object[values.length];
        for (int i = 0; i < values.length; i++)
        {
            converted[i] = parse( values[i], toTypes[i] );
        }
        return converted;
    }
}