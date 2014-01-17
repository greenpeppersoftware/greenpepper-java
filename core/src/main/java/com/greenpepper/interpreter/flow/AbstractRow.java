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

package com.greenpepper.interpreter.flow;

import com.greenpepper.Example;
import com.greenpepper.Statistics;
import com.greenpepper.Specification;
import com.greenpepper.call.Compile;
import com.greenpepper.call.Result;
import com.greenpepper.call.Stub;
import com.greenpepper.reflect.Fixture;

import java.util.List;

public abstract class AbstractRow implements Row
{
    protected final Fixture fixture;
    protected Example reportCell;

    protected AbstractRow( Fixture fixture )
    {
        this.fixture = fixture;
    }

    public abstract List<Example> actionCells(Example row);

    protected void reportException( Specification table )
    {
        countRowOf( table ).exception().unconditionnaly();
    }

    protected CountStatistics countRowOf( Specification table )
    {
        return new CountStatistics( table );
    }

    protected Stub tallyStatistics( Specification table )
    {
        return new TallyStatistics( table );
    }

    protected static class TallyStatistics implements Stub
    {
        private final Specification table;

        public TallyStatistics( Specification table )
        {
            this.table = table;
        }

        public void call( Result result )
        {
            Statistics stats = new Statistics();
            Compile.statistics( stats ).call( result );
            table.exampleDone( stats );
        }
    }

    protected static class CountStatistics implements Stub
    {
        private final Specification table;
        private Statistics stats;

        public CountStatistics( Specification chain )
        {
            this.table = chain;
        }

        public CountStatistics stats( int right, int wrong, int exception, int ignored )
        {
            stats = new Statistics( right, wrong, exception, ignored );
            return this;
        }

        public void call( Result result )
        {
            unconditionnaly();
        }

        public void unconditionnaly()
        {
            table.exampleDone( stats );
        }

        public CountStatistics right()
        {
            return stats(1, 0, 0, 0);
        }

        public CountStatistics wrong()
        {
            return stats(0, 1, 0, 0);
        }

        public CountStatistics exception()
        {
            return stats(0, 0, 1, 0);
        }

        public CountStatistics ignored()
        {
            return stats(0, 0, 0, 1);
        }
    }

}



