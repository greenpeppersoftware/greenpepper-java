package com.greenpepper.annotation;

import com.greenpepper.util.FakeText;
import junit.framework.TestCase;

public class IgnoredAnnotationTest extends TestCase
{
    public void testColorsInGrayAndReplacesTextWithActualValue()
    {
        FakeText text = new FakeText( "given" );
        IgnoredAnnotation ignored = new IgnoredAnnotation( "actual" );
        ignored.writeDown( text );
        assertEquals( Colors.GRAY, text.getStyle( "background-color" ) );
        assertEquals( "actual", text.getContent() );
    }
}
