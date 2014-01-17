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

package com.greenpepper.interpreter.flow.dowith;

import com.greenpepper.interpreter.MockFixture;
import com.greenpepper.interpreter.flow.AbstractRow;
import com.greenpepper.interpreter.flow.RowSelector;
import com.greenpepper.interpreter.flow.Table;
import com.greenpepper.util.AnnotationUtil;
import com.greenpepper.util.Tables;

public class CellAnnotationFixture
{
    private Tables tables;
    private AbstractRow command;
    private MockFixture fixture;

    public void row(Tables row)
    {
        this.tables = row;
        this.command = parseCommand();
    }

    public void actionReturns(String actionReturns)
    {
        fixture.willRespondTo( actionName(), actionReturns );
        command.interpret( new Table( tables.at( 0, 0 ) ) );
    }

    private AbstractRow parseCommand()
    {
        fixture = new MockFixture();
        RowSelector selector = new DoWithRowSelector( fixture );
        return (AbstractRow) selector.select( tables.at( 0, 0 ) );
    }

    private String actionName()
    {
        Action action = Action.parse( command.actionCells( tables.at( 0, 0 ) ) );
        return action.name();
    }

    public Integer[] cellsMarkedRight()
    {
        return AnnotationUtil.cellsMarkedRight( tables.at( 0, 0 ) );
    }

    public Integer[] cellsMarkedWrong()
    {
        return AnnotationUtil.cellsMarkedWrong( tables.at( 0, 0 ) );
    }

    public Integer[] cellsMarkedException()
    {
        return AnnotationUtil.cellsMarkedException( tables.at( 0, 0 ) );
    }

    public Integer[] cellsMarkedIgnored()
    {
        return AnnotationUtil.cellsMarkedIgnored( tables.at( 0, 0 ) );
    }
}
