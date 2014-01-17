package com.greenpepper.reflect;

public class TypeNotFoundException extends RuntimeException
{
    private final String name;

    public TypeNotFoundException(String name)
    {
        super();
        
        this.name = name;
    }

    public String getMessage()
    {
        return name + " does not match any known type";
    }
}
