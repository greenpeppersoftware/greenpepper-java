package com.greenpepper;

import com.greenpepper.reflect.TypeLoader;
import com.greenpepper.reflect.JavaTypeLoader;
import com.greenpepper.reflect.PackageTypeLoader;
import com.greenpepper.reflect.SuffixTypeLoader;
import com.greenpepper.reflect.CamelCaseTypeLoader;
import com.greenpepper.reflect.Type;

public class TypeLoaderChain<T> implements TypeLoader<T>
{
    private final TypeLoader<T> typeLoader;

    public TypeLoaderChain(Class<? extends T> type)
    {
        this( type, Thread.currentThread().getContextClassLoader() );
    }

    public TypeLoaderChain(Class<? extends T> type, ClassLoader classLoader)
    {
        TypeLoader<T> typeLoaderAdapter = new JavaTypeLoader<T>(type, classLoader);
        PackageTypeLoader<T> packageClassLoader = new PackageTypeLoader<T>(typeLoaderAdapter);
        SuffixTypeLoader<T> suffixClassLoader = new SuffixTypeLoader<T>(packageClassLoader);
        typeLoader = new CamelCaseTypeLoader<T>(suffixClassLoader);
    }

    public void searchPackage(String prefix)
    {
        typeLoader.searchPackage(prefix);
    }

    public void addSuffix(String suffix)
    {
        typeLoader.addSuffix(suffix);
    }

    public Type<T> loadType(String name)
    {
        return typeLoader.loadType(name);
    }
}
