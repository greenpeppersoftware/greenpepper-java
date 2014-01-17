package com.greenpepper.converter;

public class HexadecimalConverter extends IntegerConverter
{
    protected String doToString(Object value)
    {
        return String.format("%x", value);
    }
}
