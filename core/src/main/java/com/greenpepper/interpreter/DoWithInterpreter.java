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

import com.greenpepper.interpreter.flow.AbstractFlowInterpreter;
import com.greenpepper.interpreter.flow.dowith.DoWithRowSelector;
import com.greenpepper.reflect.Fixture;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;

public class DoWithInterpreter extends AbstractFlowInterpreter
{
    private static final Collection<String> suffixes =
            Collections.synchronizedCollection( new ArrayList<String>() );
    private static final Collection<String> packages =
            Collections.synchronizedCollection( new ArrayList<String>() );

    public DoWithInterpreter( Fixture fixture )
    {
        DoWithRowSelector selector = new DoWithRowSelector(fixture);
        selector.addSuffixes(suffixes);
        selector.addPackages(packages);
        setRowSelector(selector);
    }

    public static void addRowSuffix( String suffix )
    {
        suffixes.add( suffix );
    }

    public static void addRowsInPackage( String packageName )
    {
        packages.add( packageName );
    }
}