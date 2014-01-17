package com.greenpepper.interpreter.flow;

import static com.greenpepper.util.CollectionUtil.filter;
import static com.greenpepper.util.CollectionUtil.joinAsString;
import static com.greenpepper.util.CollectionUtil.toArray;

import java.util.List;

import com.greenpepper.Call;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;
import com.greenpepper.util.NotBlank;

public abstract class AbstractAction
{
    private final Iterable<String> cells;

    public AbstractAction( Iterable<String> cells )
    {
        this.cells = cells;
    }

    protected Iterable<String> getCells()
    {
    	return this.cells;
    }
    
    public Call checkAgainst( Fixture fixture ) throws Exception
    {
        return call( fixture.check( name() ) );
    }

    protected Call call( Message message ) throws NoSuchMessageException
    {
        Call call = new Call( message );
        call.addInput( arguments() );
        return call;
    }

    protected abstract List<String> keywords();

    protected abstract List<String> parameters();

    public String[] arguments()
    {
        return toArray( parameters() );
    }

    public String name()
    {
        return joinAsString( toArray( filter ( keywords(), new NotBlank() ) ), " ");
    }
    
}
