package com.greenpepper.annotation;

import com.greenpepper.util.FakeText;
import junit.framework.TestCase;

public class MissingAnnotationTest extends TestCase
{
    public void testColorsInRedAndDisplaysActualValueAsMissing()
    {
        FakeText text = new FakeText( "given" );

        MissingAnnotation wrong = new MissingAnnotation();
        wrong.writeDown( text );
        assertEquals( Colors.RED, text.getStyle( "background-color" ) );
        assertEquals( "<em>Missing</em> given", text.getContent() );
    }
}
