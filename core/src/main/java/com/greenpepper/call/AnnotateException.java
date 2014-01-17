package com.greenpepper.call;

import com.greenpepper.Annotatable;
import com.greenpepper.annotation.Annotations;

public class AnnotateException implements Stub
{
    private final Annotatable annotatable;

    public AnnotateException( Annotatable annotatable )
    {
        this.annotatable = annotatable;
    }

    public void call( Result result )
    {
        annotatable.annotate( Annotations.exception( result.getException() ) );
    }
}
