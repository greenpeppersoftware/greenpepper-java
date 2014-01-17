package com.greenpepper.document;

import java.util.Map;

import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.Variables;
import com.greenpepper.util.ExampleWrapper;

public abstract class AbstractSpecification implements Specification
{
    protected final Variables variables = new Variables();
    protected Example cursor;

    protected void setStart( Example example )
    {
        this.cursor = before( example );
    }

    private Example before( Example example )
    {
        return ExampleWrapper.empty( example );
    }

    public Example nextExample()
    {
        cursor = peek();
        return cursor;
    }
    
    public Map<String, Object> getAllVariables()
    {
    	return variables.getAllVariables();
    }
    
    protected abstract Example peek();

    public boolean hasMoreExamples()
    {
        return peek() != null;
    }

    public abstract void exampleDone( Statistics statistics );

    public Object getVariable(String symbol)
    {
        return variables.getVariable( symbol );
    }

    public void setVariable(String symbol, Object value)
    {
        variables.setVariable( symbol, value );
    }
}
