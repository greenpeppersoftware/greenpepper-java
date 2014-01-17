package com.greenpepper.document;

import com.greenpepper.Example;
import static com.greenpepper.util.ExampleUtil.contentOf;

public abstract class AbstractTableFilter implements ExampleFilter
{
    private final String[] keywords;

    protected AbstractTableFilter( String... keywords )
    {
        this.keywords = keywords;
    }

    public boolean canFilter( Example example )
    {
        for (String keyword : keywords)
        {
            if (keyword.equalsIgnoreCase( contentOf( example.at( 0, 0, 0 ) ) )) return true;
        }
        return false;
    }

    public Example filter( Example example )
    {
        if (!canFilter( example )) return example;
        return doFilter( example );
    }

    protected abstract Example doFilter( Example example );
}
