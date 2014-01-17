/*
 * Copyright (c) 2007 Pyxis Technologies inc.
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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Introspector
{
    private final Comparator comparator;
    private Class target;

    public static Introspector respectingCase( Class target )
    {
        return new Introspector( target, new NaturalComparator() );
    }

    public static Introspector ignoringCase( Class target )
    {
        return new Introspector( target, new IgnoringCaseComparator() );
    }

    public Introspector( Class target, Comparator<String> comparator )
    {
        this.target = target;
        this.comparator = comparator;
    }

    public Field getField( String name )
    {
        for (Field field : target.getFields())
        {
            if (compare( field.getName(), name )) return field;
        }
        return null;
    }

    public Method getSetter( String name )
    {
        for (PropertyDescriptor descriptor : getPropertyDescriptors( target ))
        {
            Method setter = descriptor.getWriteMethod();
            if (setter == null) continue;
            if (compare( descriptor.getName(), name )) return setter;
        }
        return null;
    }

    public Method getGetter( String name )
    {
        for (PropertyDescriptor descriptor : getPropertyDescriptors( target ))
        {
            Method getter = descriptor.getReadMethod();
            if (getter == null) continue;
            if (compare( descriptor.getName(), name )) return getter;
        }
        return null;
    }

    public List<Method> getMethods( String name )
    {
        List<Method> methods = new ArrayList<Method>();
        
        for (Method method : target.getMethods())
        {
            if (compare( method.getName(), name )) methods.add(method);
        }
        return methods;
    }

    @SuppressWarnings("unchecked")
    private boolean compare( String key, String name )
    {
        return comparator.compare( key, name ) == 0;
    }

    private PropertyDescriptor[] getPropertyDescriptors( Class clazz )
    {
        BeanInfo bean;
        try
        {
            bean = java.beans.Introspector.getBeanInfo( clazz, Object.class );
        }
        catch (IntrospectionException e)
        {
            return new PropertyDescriptor[0];
        }

        return bean.getPropertyDescriptors();
    }
}
