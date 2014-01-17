package com.greenpepper.document;

import com.greenpepper.Example;

public class EagerFilter implements ExampleFilter
{
    private final ExampleFilter filter;

    public EagerFilter( ExampleFilter filter )
    {
        this.filter = filter;
    }

    public boolean canFilter( Example example )
    {
        return filter.canFilter( example );
    }

    public Example filter( Example example )
    {
        Example next = example;
        while (next != null && canFilter( next ))
        {
            next = filter.filter( next );
        }
        return next;
    }
}
