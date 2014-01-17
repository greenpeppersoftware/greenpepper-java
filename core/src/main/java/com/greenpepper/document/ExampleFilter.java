package com.greenpepper.document;

import com.greenpepper.Example;

public interface ExampleFilter
{
    boolean canFilter( Example example );

    Example filter( Example example );
}
