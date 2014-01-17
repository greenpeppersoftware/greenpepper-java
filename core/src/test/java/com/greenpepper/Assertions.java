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

import com.greenpepper.annotation.Annotation;
import com.greenpepper.annotation.EnteredAnnotation;
import com.greenpepper.annotation.ExceptionAnnotation;
import com.greenpepper.annotation.IgnoredAnnotation;
import com.greenpepper.annotation.MissingAnnotation;
import com.greenpepper.annotation.RightAnnotation;
import com.greenpepper.annotation.SkippedAnnotation;
import com.greenpepper.annotation.StoppedAnnotation;
import com.greenpepper.annotation.SurplusAnnotation;
import com.greenpepper.annotation.WrongAnnotation;
import com.greenpepper.util.BreachSpecificationEncapsulation;
import com.greenpepper.util.DuckType;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class Assertions
{
    private Assertions()
    {
    }

    public static void assertNotAnnotated(Example example)
    {
        assertNull( String.valueOf( readAnnotation( example ) ), readAnnotation( example ) );
    }

    public static void assertAnnotatedRight(Example example)
    {
        assertAnnotatedWith( example, RightAnnotation.class );
    }

    public static void assertAnnotatedWrongWithoutDetail(Example example)
    {
        assertAnnotatedWrong( example );
        assertFalse( ((WrongAnnotation) readAnnotation( example )).isDetailed() );
    }

    public static void assertAnnotatedWrong(Example example)
    {
        assertAnnotatedWith( example, WrongAnnotation.class );
    }

    public static void assertAnnotatedException(Example example)
    {
        assertAnnotatedWith( example, ExceptionAnnotation.class );
    }

    public static void assertAnnotatedIgnored(Example example)
    {
        assertAnnotatedWith( example, IgnoredAnnotation.class );
    }

    public static void assertAnnotatedMissing(Example example)
    {
        assertAnnotatedWith( example, MissingAnnotation.class );
    }

    public static void assertAnnotatedSurplus(Example example)
    {
        assertAnnotatedWith( example, SurplusAnnotation.class );
    }

    public static void assertAnnotatedEntered(Example example)
    {
        assertAnnotatedWith( example, EnteredAnnotation.class );
    }

	public static void assertAnnotatedSkipped(Example example)
	{
		assertAnnotatedWith( example, SkippedAnnotation.class );
	}

	public static void assertAnnotatedStopped(Example example)
	{
		assertAnnotatedWith( example, StoppedAnnotation.class );
	}

    public static void assertAnnotatedWrongWithDetails(Example example)
    {
        assertAnnotatedWrong( example );
        assertTrue( ((WrongAnnotation) readAnnotation( example )).isDetailed() );
    }

    public static void assertAnnotatedWith(Example example, Class<? extends Annotation> annotationType)
    {
        assertTrue( String.valueOf( readAnnotation( example ) ), annotationType.isInstance( readAnnotation( example ) ) );
    }

    public static Annotation readAnnotation(Example example)
    {
        return breachEncapsulation( example ).getAnnotation();
    }

    private static BreachSpecificationEncapsulation breachEncapsulation(Example example)
    {
        if (!DuckType.instanceOf( BreachSpecificationEncapsulation.class, example ))
            throw new IllegalArgumentException( "Cant' breach encapsulation of " + example.getClass().getName() );
        return DuckType.implement( BreachSpecificationEncapsulation.class, example );
    }
}
