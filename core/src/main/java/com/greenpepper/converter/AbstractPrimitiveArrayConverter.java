package com.greenpepper.converter;

import com.greenpepper.TypeConversion;

public class AbstractPrimitiveArrayConverter extends ArrayConverter
{
    @Override
    public boolean canConvertTo( Class type )
    {
        return isArray( type ) && TypeConversion.supports( type.getComponentType() ) && type.getComponentType().isPrimitive();
    }
}
