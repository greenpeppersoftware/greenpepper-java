package com.greenpepper;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class Variables implements ExecutionContext
{
    private final Map<String, Object> variables = new HashMap<String, Object>();

    public void setVariable(String symbol, Object value)
    {
        variables.put( symbol, value );
    }

    public Object getVariable(String symbol)
    {
        return variables.get( symbol );
    }

    public Map<String, Object> getAllVariables()
    {
        return Collections.unmodifiableMap( variables );
    }
}
