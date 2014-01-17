/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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

import com.greenpepper.runner.CommandLineRunner;
import com.greenpepper.runner.CompositeSpecificationRunnerMonitor;
import com.greenpepper.util.DuckType;

public class CommandLineRunnerMirrorImpl implements CommandLineRunnerMirror
{
    private CommandLineLogger logger;
    private CommandLineMonitor recorderMonitor;

    public CommandLineRunnerMirrorImpl(Object logger, Object monitor)
    {
        this.logger = DuckType.implement( CommandLineLogger.class, logger );
        this.recorderMonitor = DuckType.implement( CommandLineMonitor.class, monitor );
    }

    public void run(String[] args) throws Exception
    {
        CommandLineRunner runner = new CommandLineRunner();

        CompositeSpecificationRunnerMonitor aggregateMonitor = new CompositeSpecificationRunnerMonitor();
        aggregateMonitor.add( new AntSpecificationRunnerMonitor( logger ) );
        aggregateMonitor.add( recorderMonitor );

        runner.setMonitor( aggregateMonitor );

        runner.run( args );
    }
}