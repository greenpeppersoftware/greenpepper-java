package com.greenpepper.call;

import com.greenpepper.Annotatable;
import com.greenpepper.annotation.Annotations;

public class AnnotateIgnored implements Stub
{
    private final Annotatable annotatable;

    public AnnotateIgnored( Annotatable annotatable )
    {
        this.annotatable = annotatable;
    }

    public void call( Result result )
    {
        annotatable.annotate( Annotations.ignored( result.getActual() ) );
    }
}
