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
package com.greenpepper;

import com.greenpepper.call.Result;
import com.greenpepper.call.ResultHandler;
import com.greenpepper.call.Stub;
import com.greenpepper.call.StubSyntax;
import com.greenpepper.expectation.Collator;
import com.greenpepper.expectation.Expectation;
import com.greenpepper.expectation.ShouldBe;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.SystemUnderDevelopmentException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A test to be executed against the SUT.
 */
public class Call
{
    private final Message message;
    private final Collection<String> inputs = new ArrayList<String>();

    private Expectation expectation;
    private Result result;
    private List<ResultHandler> handlers = new ArrayList<ResultHandler>();

    public Call( Message message )
    {
        this.message = message;
    }

    public void addInput( String... values )
    {
        inputs.addAll( Arrays.asList( values ) );
    }

    public Object execute( String... args ) throws Exception
    {
        result = new Result( expectation );
        try
        {
            result.setActual( message.send( mergeInputsWith( args ) ) );
        }
        catch (SystemUnderDevelopmentException e)
        {
            result.exceptionOccured( e.getCause() );
        }

        dispatchForHandling( result );

        return result.getActual();
    }

    private void dispatchForHandling( Result result )
    {
        for (ResultHandler handler : handlers)
        {
            handler.handle( result );
        }
    }

    public void expect( Collator collator )
    {
        expect( collator.toExpectation() );
    }

    public void expect( Expectation expected )
    {
        this.expectation = expected;
    }

    public void expect( String value )
    {
        expect( ShouldBe.literal( value ) );
    }

    private String[] mergeInputsWith( String... args )
    {
        inputs.addAll( Arrays.asList( args ) );
        return inputs.toArray( new String[inputs.size()] );
    }

    public Result getResult()
    {
        return result;
    }

    public boolean wasRight()
    {
        return result.isRight();
    }

    public boolean wasWrong()
    {
        return result.isWrong();
    }

    public boolean wasIgnored()
    {
        return result.isIgnored();
    }

    public boolean hasFailed()
    {
        return result.isException();
    }

    public Throwable getFailure()
    {
        return result.getException();
    }

    public StubSyntax will( Stub stub )
    {
        ResultHandler handler = new ResultHandler( stub );
        handlers.add( handler );
        return handler;
    }
}