package com.greenpepper.extensions.dynabeans;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaClass;

import com.greenpepper.util.TestCase;
import junit.framework.Assert;

public class DynaPropertyWriterTest extends TestCase {

    private DynaBean bean;

    protected void setUp() throws Exception {
        DynaProperty name = new DynaProperty("name", int.class);
        bean = new LazyDynaBean(new LazyDynaClass("target", new DynaProperty[] { name } ));
    }

    public void testShouldConvertArgumentToPropertyType() throws Throwable
    {
        DynaPropertyWriter writer = new DynaPropertyWriter( bean, "name" );
        writer.send( "5" );
        Assert.assertEquals( 5, bean.get("name") );
    }

    public void testShouldSupportLazyPropertiesAsString() throws Throwable
    {
        DynaPropertyWriter writer = new DynaPropertyWriter( bean, "lazy" );
        writer.send( "An string" );
        Assert.assertEquals( "An string", bean.get("lazy") );
    }

    public void testExpectsOneAndOnlyOneArgument() throws Throwable
    {
        DynaPropertyWriter writer = new DynaPropertyWriter( bean, "name" );
        try
        {
            writer.send();
            fail( "Should expect at least 1 argument" );
        }
        catch (Exception expected)
        {
            assertTrue( true );
        }
        try
        {
            writer.send( "2", "3" );
            fail( "Should expect at most 1 argument" );
        }
        catch (Exception expected)
        {
            assertTrue( true );
        }
    }
}
