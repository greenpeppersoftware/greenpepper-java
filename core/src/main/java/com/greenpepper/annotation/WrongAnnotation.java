package com.greenpepper.annotation;

import static com.greenpepper.GreenPepper.$;
import com.greenpepper.Text;
import com.greenpepper.TypeConversion;
import com.greenpepper.expectation.Expectation;

public class WrongAnnotation implements Annotation
{
    private Expectation expected;
    private Object actual;
    private boolean detailed;

    public void writeDown( Text text )
    {
        text.setStyle( Styles.BACKGROUND_COLOR, Colors.RED );
        if (detailed) text.setContent( message() );
    }

    private String message()
    {
        StringBuilder message = new StringBuilder();
        message.append( "<b>" ).append( $( "expected" ) ).append( ":</b> " );
        expected.describeTo( message );
        message.append( " <b>" ).append( $( "received" ) ).append( ":</b> " ).append( TypeConversion.toString( actual ) );
        return message.toString();
    }

    public void giveDetails( Expectation expected, Object actual )
    {
        this.expected = expected;
        this.actual = actual;
        this.detailed = true;
    }

    public boolean isDetailed()
    {
        return detailed;
    }
}
