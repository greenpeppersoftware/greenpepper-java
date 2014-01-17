package com.greenpepper.annotation;

import com.greenpepper.Text;
import com.greenpepper.TypeConversion;

public class IgnoredAnnotation implements Annotation
{
    private final Object actual;

    public IgnoredAnnotation(Object actual)
    {
        this.actual = actual;
    }

    public void writeDown(Text text)
    {
        text.setStyle( Styles.BACKGROUND_COLOR, Colors.GRAY );
        text.setContent( TypeConversion.toString( actual ) );
    }
}
