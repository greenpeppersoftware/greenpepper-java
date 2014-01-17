package com.greenpepper.interpreter;

import java.util.ArrayList;

import com.greenpepper.interpreter.collection.CollectionInterpreter;
import com.greenpepper.reflect.CollectionProvider;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.ClassUtils;
import com.greenpepper.util.NameUtils;

public class CollectionResolutionFixture
{
    private Class fixture;

    private static Class getClass(String typename) throws Exception
    {
        return ClassUtils.loadClass("com.greenpepper.interpreter.CollectionResolutionFixture$"+NameUtils.toClassName(typename));
    }

    public void fixture(String typename) throws Exception
    {
        this.fixture = getClass(typename);
    }

    public String queryValues() throws Exception
    {
        CollectionInterpreter interpreter = new ListOfInterpreter(new PlainOldFixture(fixture.newInstance()));

        return interpreter.getFixtureList().get(0).getTarget().toString();
    }

    public static class FixtureWithACollectionProviderAnnotation  extends ArrayList
    {
        private static final long serialVersionUID = 1L;

        @CollectionProvider()
        public String[] annotated()
        {
            return new String[] {"collection"};
        }

        public String[] query()
        {
            throw new UnsupportedOperationException();
        }
    }


    public static class FixtureThatImplementsCollection extends ArrayList
    {
        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        public FixtureThatImplementsCollection()
        {
            add("collection");
        }

        public String[] query()
        {
            throw new UnsupportedOperationException();
        }

    }

    public static class FixtureWithAQueryMethod
    {
        public String[] query()
        {
            return new String[] {"collection"};
        }
    }

    public static class FixtureWithoutCollection
    {
    }
}
