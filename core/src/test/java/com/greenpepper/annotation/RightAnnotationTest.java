package com.greenpepper.annotation;

import com.greenpepper.util.FakeText;
import junit.framework.TestCase;

public class RightAnnotationTest extends TestCase
{
    private FakeText text;
    private Annotation annotation;

    protected void setUp() throws Exception
    {
        text = new FakeText( "expected" );
        annotation = new RightAnnotation();
    }

    public void testColorsExampleGreenAndDoesNotChangeText()
    {
        annotation.writeDown( text );
        assertEquals( Colors.GREEN, text.getStyle( "background-color" ) );
        assertEquals( "expected", text.getContent() );
    }
}
