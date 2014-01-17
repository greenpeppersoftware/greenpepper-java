package com.greenpepper.annotation;

import com.greenpepper.util.FakeText;
import junit.framework.TestCase;

public class ExceptionAnnotationTest extends TestCase
{
    private FakeText text;

    protected void setUp() throws Exception
    {
        text = new FakeText( "content" );
    }

    public void testColorsInYellowAndAddErrorBacktraceToText()
    {
        Throwable error = new RuntimeException( "error message" );
        error.setStackTrace( new StackTraceElement[]{new StackTraceElement( "Class", "method", "Class.java", -1 )} );

        ExceptionAnnotation exception = new ExceptionAnnotation( error );
        exception.writeDown( text );

        assertEquals( "content<hr/><pre><font size=\"-2\">" + error.toString() + "<br/>Class.method(Class.java)</font></pre>", text.getContent() );
    }
}
