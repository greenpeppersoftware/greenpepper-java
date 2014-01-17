package com.greenpepper.reflect;

public interface TypeLoader<T>
{
    void searchPackage(String name);

    void addSuffix(String suffix);

    Type<T> loadType(String name);
}
