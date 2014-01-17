package com.greenpepper.annotation;

import com.greenpepper.Text;

public class RightAnnotation implements Annotation
{
    public void writeDown(Text text)
    {
        text.setStyle( Styles.BACKGROUND_COLOR, Colors.GREEN );
    }
}
