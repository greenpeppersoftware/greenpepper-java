package com.greenpepper.extensions.fit;

import java.util.Date;

import com.greenpepper.converter.AbstractTypeConverter;

public class DateConverter extends AbstractTypeConverter
{
    @SuppressWarnings("deprecation")
	protected Object doConvert(String value)
    {
    	return new Date(Date.parse(value));
    }

    public boolean canConvertTo(Class type)
    {
        return Date.class.isAssignableFrom(type);
    }
}