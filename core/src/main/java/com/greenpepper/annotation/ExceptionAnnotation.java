package com.greenpepper.annotation;

import com.greenpepper.Text;
import com.greenpepper.util.ExceptionUtils;

public class ExceptionAnnotation implements Annotation
{
    private final Throwable error;

    public ExceptionAnnotation(Throwable error)
    {
        this.error = error;
    }

    public void writeDown(Text text)
    {
        text.setStyle( Styles.BACKGROUND_COLOR, Colors.YELLOW );
        text.setContent( text.getContent() + "<hr/><pre><font size=\"-2\">" + ExceptionUtils.stackTrace( error, "<br/>", 10 ) + "</font></pre>" );
    }

    public String toString()
    {
        return ExceptionUtils.stackTrace( error, "\n", 10 );
    }
}
