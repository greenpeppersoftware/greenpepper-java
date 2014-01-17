package com.greenpepper.reflect;

public class JavaTypeLoader<T> implements TypeLoader<T>
{
    private final Class<? extends T> type;
    private final java.lang.ClassLoader classLoader;

    public JavaTypeLoader( Class<? extends T> type )
    {
        this( type, Thread.currentThread().getContextClassLoader() );
    }

    public JavaTypeLoader(Class<? extends T> type, java.lang.ClassLoader classLoader)
    {
        this.type = type;
        this.classLoader = classLoader;
    }

    public void searchPackage(String prefix)
    {
        throw new UnsupportedOperationException();
    }

    public void addSuffix(String suffix)
    {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public Type<T> loadType(String name)
    {
        Class clazz = loadClass( name );
        return classLoaded(clazz) && typeMatches(clazz) ? new Type<T>(clazz) : null;
    }

    private boolean classLoaded(Class clazz)
    {
        return clazz != null;
    }

    private boolean typeMatches(Class clazz)
    {
        return type.isAssignableFrom(clazz);
    }

    public Class loadClass(String name)
    {
        try
        {
            return classLoader.loadClass( name );
        }
        catch (ClassNotFoundException notFound)
        {
            return null;
        }
        catch (NoClassDefFoundError notFound)
        {
            return null;
        }
    }
}
