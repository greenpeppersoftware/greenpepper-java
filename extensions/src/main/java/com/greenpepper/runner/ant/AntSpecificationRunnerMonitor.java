/*
 * Copyright (c) 2008 Pyxis Technologies inc.
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
package com.greenpepper.runner.ant;

import com.greenpepper.Statistics;
import com.greenpepper.runner.SpecificationRunnerMonitor;
import com.greenpepper.util.ExceptionUtils;

public class AntSpecificationRunnerMonitor
        implements SpecificationRunnerMonitor
{
    private final CommandLineRunnerMirror.CommandLineLogger logger;

    public AntSpecificationRunnerMonitor(CommandLineRunnerMirror.CommandLineLogger logger)
    {
        this.logger = logger;
    }

    public void testRunning(String s)
    {
        logger.info( "Running " + s );
    }

    public void testDone(int rightCount, int wrongCount, int exceptionCount, int ignoreCount)
    {
        Statistics stats = new Statistics( rightCount, wrongCount, exceptionCount, ignoreCount );
        logger.info( stats + ( stats.indicatesFailure() ? " <<< FAILURE!\n" : "\n" ) );
    }

    public void exceptionOccured(Throwable throwable)
    {
        logger.error( "Error: " + throwable.getMessage() );

        if (throwable.getCause() != null)
        {
            logger.error( "Caused by:" );
            logger.error( ExceptionUtils.stackTrace( throwable.getCause(), "\n" ) );
        }
    }
}
