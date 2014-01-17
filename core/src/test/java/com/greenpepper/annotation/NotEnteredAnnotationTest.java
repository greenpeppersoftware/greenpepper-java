package com.greenpepper.annotation;

import com.greenpepper.util.FakeText;
import junit.framework.TestCase;

public class NotEnteredAnnotationTest extends TestCase
{
    public void testColorsInRedAndDisplaysActualValueAsMissing()
    {
        FakeText text = new FakeText( "text" );

        NotEnteredAnnotation annotation = new NotEnteredAnnotation();
        annotation.writeDown( text );
        assertEquals( Colors.RED, text.getStyle( "background-color" ) );
        assertEquals( "<em>Not entered</em>", text.getContent() );
    }
}
