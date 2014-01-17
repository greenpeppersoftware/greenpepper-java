package com.greenpepper.annotation;

import com.greenpepper.expectation.ShouldBe;
import com.greenpepper.util.FakeText;
import junit.framework.TestCase;

public class WrongAnnotationTest extends TestCase
{
    private FakeText text;
    private WrongAnnotation wrong;

    protected void setUp() throws Exception
    {
        text = new FakeText( "expected" );
        wrong = new WrongAnnotation();
    }

    public void testColorsInRedAndDoNotReplaceTextByDefault()
    {
        wrong.writeDown( text );
        assertEquals( Colors.RED, text.getStyle( "background-color" ) );
        assertEquals( "expected", text.getContent() );
    }

    public void testCanReplaceTextWithExpectationAndActual()
    {
        wrong.giveDetails( ShouldBe.equal( "expected" ), "actual" );
        wrong.writeDown( text );
        assertEquals( Colors.RED, text.getStyle( "background-color" ) );
        assertEquals( "<b>Expected:</b> expected <b>Received:</b> actual", text.getContent() );
    }
}
