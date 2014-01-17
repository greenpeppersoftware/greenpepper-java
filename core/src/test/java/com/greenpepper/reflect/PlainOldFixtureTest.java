package com.greenpepper.reflect;

import com.greenpepper.util.TestCase;

public class PlainOldFixtureTest extends TestCase
{
    private Fixture fixture;
    private Target target;

    protected void setUp() throws Exception
    {
        target = new Target();
        fixture = new PlainOldFixture( target );
    }

    public void testCheckMessagesIncludeMethods() throws Exception
    {
        target.field = "value";
        Message check = fixture.check( "methodReturningField" );
        assertEquals( target.field, check.send() );
    }

    public void testCheckMessageNamesAreNotCaseSensitive() throws Exception
    {
        target.field = "value";
        Message check = fixture.check( "methodReturningField".toUpperCase() );
        assertEquals( target.field, check.send() );
    }

    public void testCheckMessageNamesCanBeHumanized() throws Exception
    {
        target.field = "value";
        Message check = fixture.check( "method returning field" );
        assertEquals( target.field, check.send() );
    }

    public void testCheckMessagesIncludeGetters() throws Exception
    {
        target.field = "value";
        Message check = fixture.check( "getter" );
        assertEquals( target.field, check.send() );
    }

    public void testCheckMessagesIncludeFields() throws Exception
    {
        target.field = "value";
        Message check = fixture.check( "field" );
        assertEquals( target.field, check.send() );
    }

    public void testSendMessagesIncludeMethods() throws Exception
    {
        target.field = "value";
        Message send = fixture.send( "methodReturningField" );
        assertEquals( target.field, send.send() );
    }

    public void testSendMessageNamesAreNotCaseSensitive() throws Exception
    {
        target.field = "value";
        Message send = fixture.send( "methodReturningField".toUpperCase() );
        assertEquals( target.field, send.send() );
    }

    public void testSendMessageNamesCanBeHumanized() throws Exception
    {
        target.field = "value";
        Message send = fixture.send( "method returning field" );
        assertEquals( target.field, send.send() );
    }

    public void testSendMessageIncludeSetters() throws Exception
    {
        Message send = fixture.send( "setter" );
        send.send( "value" );
        assertEquals( "value", target.field );
    }

    public void testSendMessagesIncludeFields() throws Exception
    {
        Message send = fixture.send( "field" );
        send.send( "value" );
        assertEquals( "value", target.field );
    }

    public void testComplainsIfCheckMessageIsNotUnderstood() throws Exception
    {
        try
        {
            fixture.check( "no such message" );
            fail();
        }
        catch (NoSuchMessageException e)
        {
            pass();
        }
    }

    public void testComplainsIfSendMessageIsNotUnderstood() throws Exception
    {
        try
        {
            fixture.send( "no such message" );
            fail();
        }
        catch (NoSuchMessageException e)
        {
            pass();
        }
    }

    public void testLooksForMessageOnSystemUnderTestIfNotFoundOnFixture() throws Exception
    {
        Message check = fixture.check( "name" );
        assertEquals( "sut", check.send() );
        Message send = fixture.send( "name" );
        assertEquals( "sut", send.send() );
    }

    public void testLooksForAGetSystemUnderTestMethodIfAnnotationNotFound() throws Exception
    {
        Fixture fixture = new PlainOldFixture( new WithoutSystemUnderTestAnnotation() );
        Message check = fixture.check( "name" );
        assertEquals( "sut", check.send() );
    }

    public void testGetterMethodCallANoParametersMethod()
    {
        try
        {
            target.field = "something";
            Message getter = fixture.check("methodReturningField");
            assertEquals(target.field, getter.send());
        }
        catch(Exception e)
        {
            fail(e.getMessage());
        }
    }
    
     public void testCanCheckTheProperlyMethodDependingOnArity() throws Exception
     {
         Message message = fixture.check("functionWithDifferentArities");
         
         assertNotNull(message);

         assertEquals(new Integer(0), (Integer)message.send());

         assertEquals(new Integer(1), (Integer)message.send(new String[] {"1"}));

         assertEquals(new Integer(2), (Integer)message.send(new String[] { "1", "2" }));

         try
         {
             message.send(new String[] { "1", "2", "3" });
             fail("Must throw an exception");                
         }
         catch(Exception e)
         {
             assertTrue(true);
         }
     }

     public void testCanSendTheProperlyMethodDependingOnArity() throws Exception
     {
         Message message = fixture.send("AttributeWithDifferentArities");
         assertNotNull(message);

         message.send(new String[] {"1"});

         assertEquals(1, target.attributeWithDifferentArities);

         try
         {
             message.send();
             assertTrue(true);
         }
         catch(Exception e)
         {
             fail(e.getMessage());
         }
     }
     
    public class Target
    {
        public String field;

        public Object sut = new Sut();

        public int attributeWithDifferentArities = 0;

        public void attributeWithDifferentArities()
        {
        }

        public int functionWithDifferentArities()
        {
            return 0;
        }

        public int functionWithDifferentArities(int aParam)
        {
            return 1;
        }

        public int functionWithDifferentArities(int aParam, int anotherParam)
        {
            return 2;
        }

        public void setSetter( String s )
        {
            field = s;
        }

        public String getGetter()
        {
            return field;
        }

        public String methodReturningField()
        {
            return field;
        }

        public void voidMethod()
        {
        }

        @SystemUnderTest
        public Object systemUnderTest()
        {
            return sut;
        }

    }

    public static class WithoutSystemUnderTestAnnotation {

        public Object sut = new Sut();

        public Object getSystemUnderTest() {
            return sut;
        }
    }

    public static class Sut
    {
        public String name()
        {
            return "sut";
        }
    }

}
