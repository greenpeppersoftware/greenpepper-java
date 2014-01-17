package com.greenpepper.util;

import com.greenpepper.Text;

import java.util.HashMap;
import java.util.Map;

public class FakeText implements Text
{
    private String text;
    private Map<String, String> styles = new HashMap<String, String>();

    public FakeText(String text)
    {
        this.text = text;
    }

    public void setStyle(String property, String value)
    {
        styles.put( property, value );
    }

    public String getStyle(String property)
    {
        return styles.get( property );
    }

    public void setContent(String content)
    {
        text = content;
    }

    public String getContent()
    {
        return text;
    }
}
