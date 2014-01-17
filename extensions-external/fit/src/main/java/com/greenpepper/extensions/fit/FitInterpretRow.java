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

package com.greenpepper.extensions.fit;

import java.util.List;

import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.document.Document;
import com.greenpepper.interpreter.flow.AbstractRow;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.ExampleUtil;
import com.greenpepper.util.ExampleWrapper;

public class FitInterpretRow extends AbstractRow
{
	private SystemUnderDevelopment sud;
	
    public FitInterpretRow(SystemUnderDevelopment sud, Fixture fixture )
    {
        super( fixture );
        this.sud = sud;
    }

    public List<Example> actionCells(Example row)
    {
        return ExampleUtil.asList(row.at(0,1));
    }

    public void interpret( Specification table )
    {
        final Example row = table.nextExample();

        Document document = Document.text( ExampleWrapper.sandbox( row ) );
        document.execute(new FitInterpreterSelector(sud));
        table.exampleDone( document.getStatistics() );

        while (table.hasMoreExamples()) table.nextExample();
    }
}