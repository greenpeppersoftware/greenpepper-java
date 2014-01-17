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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.greenpepper.converter.TypeConverter;
import com.greenpepper.expectation.Expectation;
import com.greenpepper.expectation.ShouldBe;
import com.greenpepper.reflect.Type;
import com.greenpepper.reflect.TypeLoader;
import com.greenpepper.reflect.TypeNotFoundException;
import com.greenpepper.util.Bundle;

public final class GreenPepper
{
    private static final String BUNDLE_NAME = "greenpepper_resource";
    private static final Map<String, String> aliases = new HashMap<String, String>();
    private static boolean debug;
	private static boolean stopOnFirstFailure;

    private static Locale locale = Locale.getDefault();
    private static ClassLoader classLoader = contextClassLoader();
    private static TypeLoader<Interpreter> interpreterLoader = interpreterTypeLoader();
    private static Bundle bundle;

    private GreenPepper() {}

    private static TypeLoader<Interpreter> interpreterTypeLoader()
    {
        interpreterLoader = new TypeLoaderChain<Interpreter>(Interpreter.class, classLoader);
        interpreterLoader.searchPackage("com.greenpepper.interpreter");
        interpreterLoader.addSuffix("Interpreter");
        return interpreterLoader;
    }

    private static Bundle getBundle()
    {
        synchronized ( GreenPepper.class )
        {
            if ( bundle == null )
            {
                bundle = new Bundle( getResourceBundle( locale, classLoader ) );
            }
        }

        return bundle;
    }

    public static void setLocale( Locale locale )
    {
        GreenPepper.locale = locale;
        bundle = null;
    }

    public static Interpreter getInterpreter( String name, Object... args ) throws Throwable
    {
        Type<Interpreter> type = resolveInterpreterType(name);
        if (type == null) throw new TypeNotFoundException(name);
        return type.newInstance( args );
    }

    private static Type<Interpreter> resolveInterpreterType(String name)
    {
        return interpreterLoader.loadType( resolveAlias( name ));
    }

    public static boolean isAnInterpreter( String name )
    {
        Type<Interpreter> type = resolveInterpreterType(name);
        return type != null && !type.getUnderlyingClass().equals(Interpreter.class);
    }

    public static void addImport( String prefix )
    {
        interpreterLoader.searchPackage( prefix );
    }

    public static void addInterpreterSuffix( String suffix )
    {
        interpreterLoader.addSuffix( suffix );
    }

    public static String $( String key, Object... params )
    {
        return getBundle().format( key, params );
    }

    public static void aliasInterpreter( String alias, Class<? extends Interpreter> type )
    {
        aliasInterpreter( alias, type.getName() );
    }

    public static void aliasInterpreter( String alias, String name )
    {
        aliases.put( alias, name );
    }

    public static void register( TypeConverter converter )
    {
        TypeConversion.register( converter );
    }

    public static void register( Class<? extends Expectation> factoryClass )
    {
        ShouldBe.register( factoryClass );
    }

    private static String resolveAlias( String name )
    {
        return isAlias( name ) ? aliases.get( name ) : name;
    }

    private static boolean isAlias( String name )
    {
        return aliases.containsKey( name );
    }

    private static ClassLoader contextClassLoader()
    {
        return Thread.currentThread().getContextClassLoader();
    }

    private static ResourceBundle getResourceBundle( Locale locale, ClassLoader classLoader )
    {
        return ResourceBundle.getBundle( BUNDLE_NAME, locale, classLoader );
    }

    public static boolean isDebugEnabled()
    {
        return debug;
    }

    public static void setDebugEnabled(boolean enabled)
    {
       debug = enabled;
    }

	public static boolean isStopOnFirstFailure()
	{
		return stopOnFirstFailure;
	}

	public static void setStopOnFirstFailure( boolean stop )
	{
		stopOnFirstFailure = stop;
	}

	public static boolean shouldStop( Statistics stats )
	{
		return stopOnFirstFailure && stats.indicatesFailure();
	}

	public static boolean canContinue( Statistics stats )
	{
		return !shouldStop( stats );
	}
}