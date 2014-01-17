package com.greenpepper.call;

public class ActualEquals implements ResultMatcher
{
    private final Object value;

    public ActualEquals( Object value )
    {
        this.value = value;
    }

    public boolean matches( Result result )
    {
        return value.equals( result.getActual() );
    }
}
