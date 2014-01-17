package com.greenpepper.annotation;

import com.greenpepper.util.FakeText;
import junit.framework.TestCase;

public class EnteredAnnotationTest extends TestCase
{
    public void testColorsInRedAndDisplaysActualValueAsMissing()
    {
        FakeText text = new FakeText( "text" );

        EnteredAnnotation annotation = new EnteredAnnotation();
        annotation.writeDown( text );
        assertEquals( Colors.GREEN, text.getStyle( "background-color" ) );
        assertEquals( "<em>Entered</em>", text.getContent() );
    }
}
