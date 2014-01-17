package com.greenpepper.util.cli;

import java.io.File;

public class FileConverter implements Converter
{
    public Object convert( String value ) throws Exception
    {
        return new File( value );
    }
}
