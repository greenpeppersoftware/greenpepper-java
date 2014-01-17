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
package com.greenpepper.interpreter;

import static com.greenpepper.GreenPepper.canContinue;
import static com.greenpepper.GreenPepper.shouldStop;
import static com.greenpepper.annotation.Annotations.exception;

import java.lang.reflect.Method;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.ExecutionContext;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.call.Compile;
import com.greenpepper.call.ResultIs;
import com.greenpepper.interpreter.column.Column;
import com.greenpepper.interpreter.column.ExpectedColumn;
import com.greenpepper.interpreter.column.NullColumn;
import com.greenpepper.reflect.AfterRow;
import com.greenpepper.reflect.BeforeFirstExpectation;
import com.greenpepper.reflect.BeforeRow;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.StaticInvocation;

public class RuleForInterpreter extends AbstractInterpreter
{
	protected final Fixture fixture;
    protected Statistics stats;
    private Column[] columns;
    private Message beforeRowMessage = null;
    private Message beforeFirstExpectationMessage = null;
    private Message afterRowMessage = null;

    public RuleForInterpreter( Fixture fixture )
    {
        super();
        this.fixture = fixture;
    }

    public void interpret( Specification specification )
    {
        stats = new Statistics();
        Example table = specification.nextExample();
        columns = parseColumns( table );
        bindColumnsTo( specification );
        parseMessages();
        
        for (Example row = table.at( 0, 2 ); row != null && canContinue( stats ); row = row.nextSibling())
        {
            doRow( row );

			if (shouldStop( stats ))
			{
				row.firstChild().lastSibling().addSibling().annotate(Annotations.stopped());
			}
        }

        specification.exampleDone( stats );
    }

    private void bindColumnsTo(ExecutionContext context)
    {
		for (Column column : columns)
		{
			column.bindTo(context);
		}
    }

    private Column[] parseColumns( Example table )
    {
        Example headers = table.at( 0, 1, 0 );
        if (headers == null) return new Column[0];

        Column[] columns = new Column[headers.remainings()];
        for (int i = 0; i < headers.remainings(); i++)
        {
            columns[i] = parseColumn( headers.at( i ) );
        }

		if (shouldStop( stats ))
		{
			headers.lastSibling().addSibling().annotate(Annotations.stopped());
		}

        return columns;
    }

    private Column parseColumn( Example header )
    {
        try
        {
            return HeaderForm.parse(header.getContent()).selectColumn(fixture);
        }
        catch (Exception e)
        {
            header.annotate( exception( e ) );
            stats.exception();
            return new NullColumn();
        }
    }

    private void doRow( Example row )
    {
        if (!row.hasChild()) return;

        boolean beforeFirstExpectationAlreadyCalled = false;
        callBeforeRow();
        
        Example cells = row.firstChild();
        for (int i = 0; i < cells.remainings(); i++)
        {
            final Example cell = cells.at( i );

            if (i < columns.length)
            {
            	if (!beforeFirstExpectationAlreadyCalled) 
            	{
            		beforeFirstExpectationAlreadyCalled = checkFirstExpectation(columns[i]);
            	}
                doCell( columns[i], cell );
            }
        }
        callAfterRow();
    }

    private boolean checkFirstExpectation(Column column) {
    	if (isExpectation(column)) 
    	{
    		callBeforeFirstExpectation();
    		return true;
    	}
    	
    	return false;
	}

	private boolean isExpectation(Column column) {
		return column instanceof ExpectedColumn;
	}

	private void callBeforeRow() {
		if (beforeRowMessage != null) 
		{
			callMessage(beforeRowMessage);
		}
	}
	
	private void callBeforeFirstExpectation() {
		if (beforeFirstExpectationMessage != null) 
		{
			callMessage(beforeFirstExpectationMessage);
		}
	}

	private void callAfterRow() {
		if (afterRowMessage != null) 
		{
			callMessage(afterRowMessage);
		}
	}
	
	private void callMessage(Message message) {
		try
		{
				Call call = new Call( message);
				call.will( Compile.statistics( stats ) ).when(ResultIs.exception());
				call.execute();
		}
		catch (Exception e)
		{
			stats.exception();
		}
	}
	
	private void parseMessages() {
		if (fixture != null) 
		{
			for (Method method : fixture.getTarget().getClass().getMethods())
			{
				if (method.isAnnotationPresent(BeforeRow.class))
				{
					beforeRowMessage = new StaticInvocation(fixture.getTarget(), method);
				}
				
				if (method.isAnnotationPresent(BeforeFirstExpectation.class))
				{
					beforeFirstExpectationMessage = new StaticInvocation(fixture.getTarget(), method);
				}
				
				if (method.isAnnotationPresent(AfterRow.class))
				{
					afterRowMessage = new StaticInvocation(fixture.getTarget(), method);
				}
			}
		}
	}
	

	protected void doCell( Column column, Example cell )
    {
        try
        {
            stats.tally( column.doCell( cell ) );
        }
        catch (Exception e)
        {
            cell.annotate( exception( e ) );
            stats.exception();
        }
    }
}
