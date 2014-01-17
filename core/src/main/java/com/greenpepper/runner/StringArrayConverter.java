package com.greenpepper.runner;

import com.greenpepper.util.StringUtil;
import com.greenpepper.util.cli.Converter;

public class StringArrayConverter implements Converter<String[]>
{
    private String separators;

    public StringArrayConverter( String separators )
    {
        this.separators = separators;
    }

    public String[] convert( String value ) throws Exception
    {
        return StringUtil.isBlank( value ) ? new String[0] : value.split( separators );
    }
}
