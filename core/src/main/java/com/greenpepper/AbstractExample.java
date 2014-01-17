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

import com.greenpepper.util.ExampleUtil;

import java.util.Iterator;

/**
 * Abstract base class for <code>Specification</code> implementations. It
 * provides default implementations for some methods.
 */
public abstract class AbstractExample implements Example
{
    public Iterator<Example> iterator()
    {
        return new Iterator<Example>()
        {
            private int index = 0;

            public boolean hasNext()
            {
                return at( index ) != null;
            }

            public Example next()
            {
                return at( index++ );
            }

            public void remove()
            {
                throw new UnsupportedOperationException( "Specification not mutable" );
            }
        };
    }

    public boolean hasSibling()
    {
        return nextSibling() != null;
    }

    public boolean hasChild()
    {
        return firstChild() != null;
    }

    public Example at(int i)
    {
        if (i == 0) return this;
        return hasSibling() ? nextSibling().at( i - 1 ) : null;
    }

    public Example at(int i, int... indexes)
    {
        Example at = at( i );
        for (int j : indexes)
        {
            if (at == null || !at.hasChild())
                return null;
            at = at.firstChild().at( j );
        }
        return at;
    }

    public int remainings()
    {
        return hasSibling() ? nextSibling().remainings() + 1 : 1;
    }

    public String toString()
    {
        return ExampleUtil.asString( this );
    }

    public Example lastSibling()
    {
        return at( remainings() - 1 );
    }
}