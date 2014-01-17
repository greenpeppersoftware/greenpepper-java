package com.greenpepper.extensions.ognl;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.reflect.SystemUnderTest;
import junit.framework.TestCase;

public class OgnlFixtureTest extends TestCase
{
    private Fixture fixture;
    private Target target;

    protected void setUp() throws Exception
    {
        target = new Target();
        fixture = new OgnlFixture( new PlainOldFixture( target ) );
    }

    public void testLooksForCommandOgnlFixtureExpressionIfNotFoundOnFixtureAndSut()
        throws Exception
    {
        Message send = fixture.send( "methodReturningObject().publicField" );
        send.send( "1" );
        assertEquals( 1, target.methodReturningObject().publicField );
    }

    public void testLooksForCommandOgnlSutExpressionIfNotFoundOnFixtureAndSut()
        throws Exception
    {
        Message send = fixture.send( "sutQuery().publicField" );
        send.send( "1" );
        assertEquals( 1, target.getSystemUnderTest().sutQuery().publicField );
    }

    public void testCommandExpressionThatCantBeResolveShouldComplain() throws Exception
    {
        try
        {
            Message send = fixture.send( "unexistentFixtureAndSutMethod().field" );
            send.send( "dummy" );
            fail();
        }
        catch (UnresolvableExpressionException e)
        {
            assertTrue( true );
        }
    }

    public void testCommandExpressionThatCauseAParseErrorComplain() throws Exception
    {
        try
        {
            fixture.send( "no such action" );
            fail();
        }
        catch (NoSuchMessageException e)
        {
            assertTrue( true );
        }
    }

    public void testLooksForQueryOgnlFixtureExpressionIfNotFoundOnFixtureAndSut()
        throws Exception
    {
        Message check = fixture.check( "methodReturningObject().publicField" );
        assertEquals( 0, check.send() );

        check = fixture.check( "methodReturningObject().methodReturningValue()" );
        assertEquals( true, check.send() );

        check = fixture.check( "methodReturningObject().methodReturningObject()" );
        assertEquals( target.methodReturningObject().methodReturningObject(), check.send() );
    }

    public void testLooksForQueryOgnlSutExpressionIfNotFoundOnFixtureAndSut()
        throws Exception
    {
        Message check = fixture.check( "sutQuery().publicField" );
        assertEquals( 0, check.send() );
    }

    public void testQueryExpressionThatCantBeResolveShouldComplain() throws Exception
    {
        try
        {
            Message check = fixture.check( "unexistentFixtureAndSutMethod().field" );
            check.send();
            fail();
        }
        catch (UnresolvableExpressionException e)
        {
            assertTrue( true );
        }
    }

    public void testQueryExpressionThatCauseAParseErrorComplain() throws Exception
    {
        try
        {
            fixture.check( "no such action" );
            fail();
        }
        catch (NoSuchMessageException e)
        {
            assertTrue( true );
        }
    }

    public class Target
    {
        public int publicField;

        public Sut sut = new OgnlFixtureTest.Sut();
        public Obj obj = new OgnlFixtureTest.Obj();

        public void setProperty( int value )
        {
            publicField = value;
        }

        public int getProperty()
        {
            return publicField;
        }

        public void voidMethod()
        {
        }

        public boolean methodReturningValue()
        {
            return true;
        }

        public Obj methodReturningObject()
        {
            return obj;
        }

        @SystemUnderTest
        public Sut getSystemUnderTest()
        {
            return sut;
        }

        public void methodWithArityOverloaded()
        {
        }

        public void methodWithArityOverloaded( String s1 )
        {
        }

        public void methodWithArityOverloaded( String s1, String s2 )
        {
        }

        public boolean methodReturningValueWithArityOverloaded()
        {
            return true;
        }

        public boolean methodReturningValueWithArityOverloaded( String s1 )
        {
            return true;
        }

        public boolean methodReturningValueWithArityOverloaded( String s1, String s2 )
        {
            return true;
        }
    }

    public class Sut
    {
        public Obj obj = new Obj();

        public boolean action()
        {
            return true;
        }

        public OgnlFixtureTest.Obj sutQuery()
        {
            return obj;
        }
    }

    public class Obj
    {
        public int publicField;
        private Object obj = new Object();

        public void setProperty( int value )
        {
            publicField = value;
        }

        public int getProperty()
        {
            return publicField;
        }

        public void voidMethod()
        {
        }

        public boolean methodReturningValue()
        {
            return true;
        }

        public Object methodReturningObject()
        {
            return obj;
        }
    }
}
