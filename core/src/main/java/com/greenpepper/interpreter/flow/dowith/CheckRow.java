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

package com.greenpepper.interpreter.flow.dowith;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.ResultIs;
import com.greenpepper.interpreter.flow.AbstractRow;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.ExampleUtil;

import java.util.List;

public class CheckRow extends AbstractRow
{
    public static boolean mathes( String keyword )
    {
        return "check".equalsIgnoreCase( keyword );
    }

    public CheckRow( Fixture fixture )
    {
        super( fixture );
    }

    public void interpret( Specification table )
    {
        Example row = table.nextExample();

        Example expectedCell = row.firstChild().lastSibling();
        Action action = Action.parse( actionCells(row));

        try
        {
            Call call = action.checkAgainst( fixture );
            call.expect( expectedCell.getContent() );
            call.will( Annotate.right( expectedCell ) ).when( ResultIs.right() );
            call.will( Annotate.wrongWithDetails( expectedCell ) ).when( ResultIs.wrong() );
            call.will( Annotate.exception( CollectionUtil.first( keywordCells(row) ) ) ).when( ResultIs.exception() );
            call.will( tallyStatistics( table ) );
            call.execute();
        }
        catch (Exception e)
        {
            reportException( table );
            CollectionUtil.first( keywordCells(row) ).annotate( Annotations.exception( e ) );
        }
    }

    private List<Example> keywordCells(Example row)
    {
        return CollectionUtil.odd( row.firstChild() );
    }

    public List<Example> actionCells(Example row)
    {
        List<Example> actionCells = ExampleUtil.asList( row.at(0,1) );
        CollectionUtil.removeLast( actionCells );
        return actionCells;
	}
}
