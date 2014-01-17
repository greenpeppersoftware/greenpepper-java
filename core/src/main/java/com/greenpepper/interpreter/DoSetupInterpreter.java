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
package com.greenpepper.interpreter;

import java.util.List;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.annotation.EnteredAnnotation;
import com.greenpepper.annotation.SkippedAnnotation;
import com.greenpepper.call.Result;
import com.greenpepper.call.Stub;
import com.greenpepper.expectation.ShouldBe;
import static com.greenpepper.expectation.ShouldBe.FALSE;
import static com.greenpepper.expectation.ShouldBe.instanceOf;
import com.greenpepper.interpreter.flow.dowith.Action;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.util.ExampleUtil;

public class DoSetupInterpreter
		extends AbstractInterpreter
{
	private final Fixture fixture;
	private Statistics stats;
	private boolean skip = false;

	public DoSetupInterpreter(Fixture fixture)
	{
		this.fixture = fixture;
	}

	public void interpret(Specification specification)
	{
		stats = new Statistics();
		Example table = specification.nextExample();
		
		for (Example row = table.at( 0, 1 ); row != null; row = row.nextSibling())
		{
			if (skip)
			{
				annotateSkipped( row );
			}
			else if ( !doRow( row ))
			{
				skip = true;
			}
		}

		specification.exampleDone(stats);
	}

	private boolean doRow(Example row)
	{
        Action action = Action.parse( actionCells(row) );

        try
        {
			Call call = action.checkAgainst( fixture );
			call.expect( ShouldBe.either( FALSE ).or( instanceOf( Exception.class ) ).negate() );
			call.will( new AnnotateDoSetup( row ) );
			call.execute();

			return call.wasRight();
	    }
        catch (Exception e)
        {
			stats.exception();
			annotateException( row, e );
        }

		return false;
	}

	private void annotateException(Example row, Exception e)
	{
		Example newLastCell = row.addChild();
		newLastCell.annotate( Annotations.exception( e ) );
	}

	private void annotateEntered(Example row)
	{
		Example newLastCell = row.addChild();
		newLastCell.annotate( new EnteredAnnotation() );
	}

	private void annotateSkipped(Example row)
	{
		Example newLastCell = row.addChild();
		newLastCell.annotate( new SkippedAnnotation() );
	}

	private List<Example> actionCells(Example row) {
		return ExampleUtil.asList(row.firstChild());
	}

	private class AnnotateDoSetup implements Stub
	{
		private final Example row;

		public AnnotateDoSetup( Example row )
		{
			this.row = row;
		}

		public void call(Result result)
		{
			if (result.isRight())
			{
				annotateEntered( row );
			}
			else
			{
				annotateSkipped( row );
			}
		}
	}
}