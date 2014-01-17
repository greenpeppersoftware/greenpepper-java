package com.greenpepper.interpreter.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import com.greenpepper.reflect.CollectionProvider;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.PlainOldFixture;

public class CollectionInterpreterTest extends TestCase
{
    CollectionInterpreter collectionInterpreter;
    Collection collection;


    private CollectionInterpreter interpreterFor(Object target)
    {
        return  new CollectionInterpreter(new PlainOldFixture(target)) {};
    }

    public void testThatATargetWithNoWayToGetACollectionThrowsAnException() throws Exception
    {
        try {
            collectionInterpreter = interpreterFor(this);
            collectionInterpreter.getFixtureList();
            fail();
        }
        catch (Exception ex)
        {
            // ok;
        }
    }

    public void testThatTheQueryMethodIsCalledForAClass() throws Exception
    {
        String firstElement = "TargetWithAQueryMethod-FirstElement";
        TargetWithAQueryMethod target = new TargetWithAQueryMethod(firstElement);

        collectionInterpreter = interpreterFor(target);

        assertEquals(firstElement, firstElement(collectionInterpreter.getFixtureList()));

    }

    public void testThatATargetThatIsACollectionIsUsed() throws Exception
    {
        String firstElement = "TargetThatIsACollection-FirstElement";

        TargetThatIsACollection target = new TargetThatIsACollection(firstElement);

        collectionInterpreter = interpreterFor(target);

        assertEquals(firstElement, firstElement(collectionInterpreter.getFixtureList()));
    }

    public void testThatACollectionProviderAnnotationIsThePreferedCall() throws Exception
    {
        String firstElement = "TargetWithACollectionProviderAnnotation-FirstElement";
        TargetWithACollectionProviderAnnotation target = new TargetWithACollectionProviderAnnotation(firstElement);

        collectionInterpreter = interpreterFor(target);

        assertEquals(firstElement, firstElement(collectionInterpreter.getFixtureList()));
    }

    private String firstElement(List<Fixture> list)
    {
        return (String)list.get(0).getTarget();
    }

    public class TargetWithAQueryMethod {
        Collection<Object> collection = new ArrayList<Object>();

        public TargetWithAQueryMethod(String firstElement)
        {
            collection.add(firstElement);
        }

        public Collection query()
        {
            return collection;
        }
    }

    public class TargetThatIsACollection extends ArrayList
    {
        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        TargetThatIsACollection(String firstElement)
        {
            super(Arrays.asList(new String[]{firstElement}));
        }

        public Collection query()
        {
            throw new UnsupportedOperationException();
        }
    }


    public class TargetWithACollectionProviderAnnotation {
        Collection<Object> collection = new ArrayList<Object>();

        public TargetWithACollectionProviderAnnotation(String firstElement)
        {
            collection.add(firstElement);
        }

        public Collection query()
        {
            throw new UnsupportedOperationException();
        }

        @CollectionProvider
        public Collection otherMethod()
        {
            return collection;
        }

    }

}
