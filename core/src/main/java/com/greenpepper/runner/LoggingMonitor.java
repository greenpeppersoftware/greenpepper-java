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

package com.greenpepper.runner;

import com.greenpepper.Statistics;

import java.io.PrintStream;

public class LoggingMonitor implements SpecificationRunnerMonitor
{
    private PrintStream out;
    private PrintStream err;

    public LoggingMonitor( PrintStream out, PrintStream err )
    {
        this.err = err;
        this.out = out;
    }

    public void testRunning( String location )
    {
        out.println( "Running " + location );
    }

    public void testDone( int rightCount, int wrongCount, int exceptionCount, int ignoreCount )
    {
        Statistics stats = new Statistics( rightCount, wrongCount, exceptionCount, ignoreCount );
        out.println( stats.toString() + (stats.indicatesFailure() ? " <<< FAILURE! " : "") );
    }

    public void exceptionOccured( Throwable t )
    {
        if (t instanceof RuntimeException)
        {
            err.println( "An unexpected error occured" );
            t.printStackTrace( err );
        }
        else
        {
            err.println( "Error: " + t.getMessage() );
            if (t.getCause() != null)
            {
                err.println( "Caused by:" );
                t.getCause().printStackTrace( err );
            }
        }
    }
}
