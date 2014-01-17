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
package com.greenpepper.interpreter.flow.scenario;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.interpreter.flow.AbstractRow;
import com.greenpepper.reflect.Fixture;

import java.util.Arrays;
import java.util.List;

public class ScenarioRow extends AbstractRow
{
    public ScenarioRow(Fixture fixture)
    {
        super( fixture );
    }

    public List<Example> actionCells(Example row)
    {
        return Arrays.asList( row );
    }

    public void interpret(Specification table)
    {

        Statistics stats = new Statistics();
        Example row = table.nextExample();
        Example scenario = row.firstChild();

        try
        {
            ScenarioMessage message = new ScenarioMessage( fixture.getTarget(), scenario.getContent() );

            Call call = new Call( message );

            call.will( Annotate.given( scenario, message, stats ) ).when( message.annotationIs( Given.class ) );
            call.will( Annotate.then( scenario, message, stats ) ).when( message.annotationIs( Then.class ) );
            call.will( Annotate.when( scenario, stats ) ).when( message.annotationIs( When.class ) );
            call.will( Annotate.check( scenario, stats ) ).when( message.annotationIs( Check.class ) );
            call.will( Annotate.display( scenario ) ).when( message.annotationIs( Display.class ) );
            call.execute();

            table.exampleDone( stats );
        }
        catch (Exception e)
        {
            reportException( table );
            scenario.annotate( Annotations.exception( e ) );
        }
    }
}