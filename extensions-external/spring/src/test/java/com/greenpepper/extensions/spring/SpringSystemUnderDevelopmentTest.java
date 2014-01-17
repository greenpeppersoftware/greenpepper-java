package com.greenpepper.extensions.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;

import com.greenpepper.reflect.Fixture;
import junit.framework.TestCase;

public class SpringSystemUnderDevelopmentTest extends TestCase
{
    private static final String APPLICATION_CTX1 = "classpath:aplication-ctx1-test.xml";
    private static final String APPLICATION_CTX2 = "classpath:aplication-ctx2-test.xml";

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    private void assertFixture(Fixture fixture)
    {
        assertNotNull(fixture);
        assertNotNull(fixture.getTarget());
    }

    public void testThatWeCanRetrieveTheFixtureFromTheSpringAlias() throws Throwable
    {
        SpringSystemUnderDevelopment factory = new SpringSystemUnderDevelopment(getStaticApplicationContext());

        Fixture fixture = factory.getFixture("MyBean1");
        assertFixture(fixture);

        assertEquals(MyBean1.class, fixture.getTarget().getClass());
    }

    public void testThatWeCanRetrieveTheFixtureFromTheFallBackFactoryIfParametersArePassed() throws Throwable
    {
        SpringSystemUnderDevelopment sud = new SpringSystemUnderDevelopment(getStaticApplicationContext());

        sud.addImport("com.greenpepper.extensions.spring");
        Fixture fixture = sud.getFixture("SpringSystemUnderDevelopmentTest$MyFixtureWithParams", "param");
        assertFixture(fixture);

        assertEquals(MyFixtureWithParams.class, fixture.getTarget().getClass());
        assertEquals("param", ((MyFixtureWithParams) fixture.getTarget()).getParam());
    }

    public void testThatIfTheAliasIsNotFoundTheFallBackFactoryIsUsed() throws Throwable
    {
        SpringSystemUnderDevelopment sud = new SpringSystemUnderDevelopment(getStaticApplicationContext());

        sud.addImport("com.greenpepper.extensions.spring");
        Fixture fixture = sud.getFixture("SpringSystemUnderDevelopmentTest$MyFixture");
        assertFixture(fixture);
        assertEquals(MyFixture.class, fixture.getTarget().getClass());
    }

    public void testThatWeCanRetrieveTheFixtureFromTheSpringAliasFromAnApplicationXmlCtx() throws Throwable
    {
        SpringSystemUnderDevelopment sud = new SpringSystemUnderDevelopment(APPLICATION_CTX1);

        Fixture fixture = sud.getFixture("MyBean1");
        assertFixture(fixture);
        assertEquals(MyBean1.class, fixture.getTarget().getClass());
    }

    public void testThatWeCanRetrieveTheAutowiredFixtureFromAnApplicationXmlCtx() throws Throwable
    {
        SpringSystemUnderDevelopment sud = new SpringSystemUnderDevelopment(APPLICATION_CTX1);

        Fixture fixture = sud.getFixture(MyFixtureAutowired.class.getName());
        assertFixture(fixture);
        assertEquals(MyFixtureAutowired.class, fixture.getTarget().getClass());
        assertTrue(((MyFixtureAutowired) fixture.getTarget()).getService1() instanceof MyBean1);
    }

    public void testThatWeCanRetrieveTheAutowiredFixtureFromAn2ClasspathApplicationXmlCtx() throws Throwable
    {
        SpringSystemUnderDevelopment asm = new SpringSystemUnderDevelopment(APPLICATION_CTX1, APPLICATION_CTX2);

        Fixture fixture = asm.getFixture(MyFixtureAutowired.class.getName());
        assertFixture(fixture);
        assertEquals(MyFixtureAutowired.class, fixture.getTarget().getClass());

        MyFixtureAutowired myFixture = (MyFixtureAutowired) fixture.getTarget();

        assertTrue(myFixture.getService1() instanceof MyBean1);
        assertTrue(myFixture.getService2() instanceof MyBean2);
    }

    public void testThatWeCanRetrieveTheAutowiredFixtureFromAn2FileApplicationXmlCtx() throws Throwable
    {
        String ctxPath1 = SpringSystemUnderDevelopmentTest.class.getResource("/aplication-ctx1-test.xml").toString();
        String ctxPath2 = SpringSystemUnderDevelopmentTest.class.getResource("/aplication-ctx2-test.xml").toString();
        SpringSystemUnderDevelopment sud = new SpringSystemUnderDevelopment(ctxPath1, ctxPath2);

        Fixture fixture = sud.getFixture(MyFixtureAutowired.class.getName());

        assertFixture(fixture);
        assertEquals(MyFixtureAutowired.class, fixture.getTarget().getClass());

        MyFixtureAutowired myFixture = (MyFixtureAutowired) fixture.getTarget();
        assertTrue(myFixture.getService1() instanceof MyBean1);
        assertTrue(myFixture.getService2() instanceof MyBean2);
    }

    public void testThatWeCanRetrieveTheAutowiredFixtureFromAnClasspathAndFileApplicationXmlCtx() throws Throwable
    {
        String ctxPath2 = SpringSystemUnderDevelopmentTest.class.getResource("/aplication-ctx2-test.xml").toString();
        SpringSystemUnderDevelopment sud = new SpringSystemUnderDevelopment(APPLICATION_CTX1, ctxPath2);

        Fixture fixture = sud.getFixture(MyFixtureAutowired.class.getName());

        assertFixture(fixture);
        assertEquals(MyFixtureAutowired.class, fixture.getTarget().getClass());

        MyFixtureAutowired myFixture = (MyFixtureAutowired) fixture.getTarget();
        assertTrue(myFixture.getService1() instanceof MyBean1);
        assertTrue(myFixture.getService2() instanceof MyBean2);
    }

    private StaticApplicationContext getStaticApplicationContext()
    {
        BeanDefinition beanDef = new RootBeanDefinition(MyBean1.class);
        StaticApplicationContext beanFactory = new StaticApplicationContext();
        beanFactory.registerBeanDefinition("MyBean1", beanDef);
        return beanFactory;
    }

    /******************************************************************************************/
    /**
     * ****************************** DUMMY CLASSES *****************************************
     */

    public static class MyBean1
    {
        public MyBean1()
        {
        }
    }

    public static class MyBean2
    {
        public MyBean2()
        {
        }
    }

    public static class MyFixture
    {
        public MyFixture()
        {
        }
    }

    public static class MyFixtureWithParams
    {
        private String param;

        public MyFixtureWithParams(String param)
        {
            this.param = param;
        }

        public String getParam()
        {
            return param;
        }
    }

    public static class MyFixtureAutowired
    {
        private MyBean1 service1;
        private MyBean2 service2;

        public MyFixtureAutowired()
        {
        }

        public MyBean1 getService1()
        {
            return service1;
        }

        public void setService1(MyBean1 service1)
        {
            this.service1 = service1;
        }

        public MyBean2 getService2()
        {
            return service2;
        }

        public void setService2(MyBean2 service2)
        {
            this.service2 = service2;
        }
    }
}
