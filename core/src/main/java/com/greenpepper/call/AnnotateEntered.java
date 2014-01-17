package com.greenpepper.call;

import com.greenpepper.Example;
import com.greenpepper.annotation.Annotations;

public class AnnotateEntered implements Stub
{
    private final Example example;

    public AnnotateEntered( Example example )
    {
        this.example = example;
    }

    public void call( Result result )
    {
        example.annotate( Annotations.entered() );
    }
}
