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

package com.greenpepper.interpreter.flow.action;

import com.greenpepper.interpreter.flow.RowSelector;
import com.greenpepper.interpreter.flow.action.Action;
import com.greenpepper.interpreter.flow.action.ActionRow;
import com.greenpepper.interpreter.flow.action.ActionRowSelector;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.NameUtils;
import com.greenpepper.util.Tables;

// TODO refactor this to extract common code with RowFormFixture??
public class RowStructureFixture
{
    private Tables tables;
    private ActionRow row;

    public void setRow(Tables row)
    {
        this.tables = row;
        this.row = parseCommand();
    }

    public String typeOfAction()
    {
        return row.getClass().getSimpleName().replaceAll("Row", "");
    }

    // Changed this behavior
    private ActionRow parseCommand()
    {
        RowSelector selector = new ActionRowSelector(new PlainOldFixture(new Object()));

        return (ActionRow) selector.select(tables.at(0, 0));
    }


    // Changed this behavior
    public String actionName()
    {
        Action action = Action.parse(row.actionCells(tables.at(0, 0)));
        return NameUtils.toLowerCamelCase(action.name());

    }

    public String[] parameters()
    {
        Action action = Action.parse(row.actionCells(tables.at(0, 0)));
        return action.arguments();
    }
}
