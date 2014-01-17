package com.greenpepper.annotation;

import com.greenpepper.util.FakeText;
import junit.framework.TestCase;

public class SurplusAnnotationTest extends TestCase
{
    public void testColorsInRedAndDisplaysGivenValueAsSurplus()
    {
        FakeText text = new FakeText( "actual" );

        SurplusAnnotation wrong = new SurplusAnnotation();
        wrong.writeDown( text );
        assertEquals( Colors.RED, text.getStyle( "background-color" ) );
        assertEquals( "<em>Surplus</em> actual", text.getContent() );
    }
}
