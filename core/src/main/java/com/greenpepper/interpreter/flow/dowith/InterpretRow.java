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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.document.Document;
import com.greenpepper.document.GreenPepperInterpreterSelector;
import com.greenpepper.interpreter.flow.AbstractRow;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.ExampleUtil;
import com.greenpepper.util.ExampleWrapper;

public class InterpretRow extends AbstractRow
{
    public InterpretRow( Fixture fixture )
    {
        super( fixture );
    }

    public List<Example> actionCells(Example row)
    {
        return ExampleUtil.asList(row.at(0,1));
    }

    public void interpret( Specification table )
    {
        final Example row = table.nextExample();

        Document document = Document.text( ExampleWrapper.sandbox( row ) );
        document.execute(new GreenPepperInterpreterSelector(systemUnderDevelopment()));
        table.exampleDone( document.getStatistics() );

        while (table.hasMoreExamples())
        {
            table.nextExample();
        }
    }

    private SystemUnderDevelopment systemUnderDevelopment()
    {
        return new DefaultSystemUnderDevelopment() {
            public Fixture getFixture(String name, String... params) throws Throwable
            {
                List<String> cells = new ArrayList<String>();
                cells.add(name);
                cells.addAll(Arrays.asList(params));
                Action action = new Action( cells );
                Call call = action.checkAgainst( fixture );
                Object target = call.execute();
                return fixture.fixtureFor( target );
            }

            public void addImport(String packageName) 
            {
            }
        };
    }
}