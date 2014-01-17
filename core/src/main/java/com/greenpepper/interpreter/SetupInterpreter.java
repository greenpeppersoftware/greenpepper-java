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

import java.lang.reflect.Method;

import com.greenpepper.Call;
import com.greenpepper.Example;
import static com.greenpepper.GreenPepper.canContinue;
import static com.greenpepper.GreenPepper.shouldStop;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.annotation.Annotations;
import static com.greenpepper.annotation.Annotations.exception;
import com.greenpepper.call.AnnotateSetup;
import com.greenpepper.call.Compile;
import com.greenpepper.call.ResultIs;
import com.greenpepper.interpreter.column.Column;
import com.greenpepper.interpreter.column.NullColumn;
import com.greenpepper.reflect.EnterRow;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.StaticInvocation;

public class SetupInterpreter extends AbstractInterpreter
{
	private final Fixture fixture;
	private Column[] columns;
	private Message enterRowMessage;
	private Statistics stats;
	
	public SetupInterpreter(Fixture fixture )
	{
        super();
        
		this.fixture = fixture;
	}
	
	public void interpret( Specification specification )
	{
		stats = new Statistics();
		Example table = specification.nextExample();
		columns = parseColumns( table );
		enterRowMessage = parseEnterRowMessage( table );

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
	
	private Message parseEnterRowMessage(Example table) {

		for (Method method : fixture.getTarget().getClass().getMethods())
		{
			if (method.isAnnotationPresent(EnterRow.class))
			{
				return new StaticInvocation(fixture.getTarget(), method);
			}
		}

		try
		{
			return fixture.check( "enterRow" );
		}
		catch (Exception e)
		{
			table.at( 0, 1, 0 ).annotate( exception( e ) );
			stats.exception();
			return null;
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
		
		Example cells = row.firstChild();
		
		for (int i = 0; i < cells.remainings() && canContinue( stats ); i++)
		{
			final Example cell = cells.at( i );
			
			if (i < columns.length)
			{
				doCell( columns[i], cell );
			} 
		}

		if (canContinue( stats ))
		{
			callEnterRow(row);
		}
	}
	
	private void callEnterRow(Example row) 
	{
		try
		{
			Call call = new Call( enterRowMessage );
			call.will( new AnnotateSetup( row ) );
			call.will( Compile.statistics( stats ) ).when(ResultIs.exception());
			call.execute();
		}
		catch (Exception e)
		{
			stats.exception();
		}
	}
	
	private void doCell( Column column, Example cell )
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
