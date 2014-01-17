package com.greenpepper.reflect;

import com.greenpepper.util.ClassUtils;
import com.greenpepper.TypeConversion;

import java.lang.reflect.Constructor;

public class Type<T>
{
    private final Class<? extends T> klass;

    public Type(Class<? extends T> klass)
    {
        if (klass == null) throw new NullPointerException("klass");
        this.klass = klass;
    }

    public Class<? extends T> getUnderlyingClass()
    {
        return klass;
    }

    public T newInstance(Object... args) throws Throwable
    {
        Constructor<? extends T> constructor = ClassUtils.findBestTypedConstructor(klass, args);
        return ClassUtils.invoke(constructor, args);
    }

    public T newInstanceUsingCoercion(String... args) throws Throwable
    {
        Constructor<? extends T> constructor = ClassUtils.findPossibleConstructor(klass, args);
        Class[] types = constructor.getParameterTypes();
        return newInstance( TypeConversion.convert( args, types ) );
    }
}
