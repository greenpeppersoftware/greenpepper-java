package com.greenpepper.document;

import com.greenpepper.Example;

import java.util.ArrayList;
import java.util.List;

public class CompositeFilter implements ExampleFilter
{
    private final List<ExampleFilter> filters;

    public CompositeFilter()
    {
        this.filters = new ArrayList<ExampleFilter>();
    }

    public void add( ExampleFilter filter )
    {
        filters.add( filter );
    }

    public boolean canFilter( Example example )
    {
        for (int i = filters.size() - 1; i >= 0; i--)
        {
            if (filters.get( i ).canFilter( example )) return true;
        }
        return false;
    }

    public Example filter( Example example )
    {
        Example next = example;
        for (int i = filters.size() - 1; next != null && i >= 0; i--)
        {
            next = filters.get( i ).filter( next );
        }
        return next;
    }
}
