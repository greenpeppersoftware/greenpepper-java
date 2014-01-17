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
import com.greenpepper.call.Do;
import com.greenpepper.call.ResultIs;
import com.greenpepper.interpreter.flow.AbstractRow;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.ExampleUtil;
import com.greenpepper.util.Group;

import java.util.List;

public class DefaultRow extends AbstractRow
{
    public DefaultRow(Fixture fixture)
    {
        super(fixture);
    }

    public void interpret(Specification table)
    {
        Example row = table.nextExample();
        Action action = Action.parse(actionCells(row));
        try
        {
            Call call = action.checkAgainst(fixture);
            call.will(Do.both(Annotate.right(Group.composedOf(keywordCells(row)))).and(countRowOf(table).right())).
                    when(ResultIs.equalTo(true));
            call.will(Do.both(Annotate.wrong(Group.composedOf(keywordCells(row)))).and(countRowOf(table).wrong())).
                    when(ResultIs.equalTo(false));
            call.will(Do.both(Annotate.exception(CollectionUtil.first(keywordCells(row)))).and(countRowOf(table).exception())).
                    when(ResultIs.exception());
            call.execute();
        }
        catch (Exception e)
        {
            CollectionUtil.first(keywordCells(row)).annotate(Annotations.exception(e));
            reportException(table);
        }
    }

    private List<Example> keywordCells(Example row)
    {
        return CollectionUtil.even(row.firstChild());
    }

    public List<Example> actionCells(Example row)
    {
        return ExampleUtil.asList(row.firstChild());
    }
}
