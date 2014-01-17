package com.greenpepper.annotation;

import com.greenpepper.util.FakeText;
import junit.framework.TestCase;

public class StoppedAnnotationTest
		extends TestCase
{
    public void testColorsInRedAndDisplaysActualValueAsStopped()
    {
        FakeText text = new FakeText( "text" );

        StoppedAnnotation annotation = new StoppedAnnotation();
        annotation.writeDown( text );
        assertEquals( Colors.RED, text.getStyle( "background-color" ) );
        assertEquals( "<em>Stopped</em>", text.getContent() );
    }
}