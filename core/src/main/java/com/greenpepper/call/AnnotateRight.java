package com.greenpepper.call;

import com.greenpepper.Annotatable;
import com.greenpepper.annotation.RightAnnotation;

public class AnnotateRight implements Stub
{
    private Annotatable annotatable;

    public AnnotateRight( Annotatable annotatable )
    {
        this.annotatable = annotatable;
    }

    public void call( Result result )
    {
        annotatable.annotate( new RightAnnotation() );
    }
}