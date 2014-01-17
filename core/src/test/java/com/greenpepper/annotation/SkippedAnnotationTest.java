package com.greenpepper.annotation;

import com.greenpepper.util.FakeText;
import junit.framework.TestCase;

public class SkippedAnnotationTest
		extends TestCase
{
    public void testColorsInOrangeAndDisplaysActualValueAsSkipped()
    {
        FakeText text = new FakeText( "text" );

        SkippedAnnotation annotation = new SkippedAnnotation();
        annotation.writeDown( text );
        assertEquals( Colors.ORANGE, text.getStyle( "background-color" ) );
        assertEquals( "<em>Skipped</em>", text.getContent() );
    }
}