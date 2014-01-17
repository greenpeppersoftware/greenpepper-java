package com.greenpepper.extensions.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.greenpepper.reflect.Fixture;
import junit.framework.TestCase;

public class GuiceSystemUnderDevelopmentTest extends TestCase
{
	protected void setUp() throws Exception
	{
		super.setUp();
	}
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	private void assertFixture(Fixture fixture) {
		assertNotNull(fixture);
        assertNotNull(fixture.getTarget());
	}

    public void testThatWeCanRetrieveAPlainFixtureFromGuice() throws Throwable
    {
        GuiceSystemUnderDevelopment factory = new GuiceSystemUnderDevelopment();

        factory.addImport("com.greenpepper.extensions.guice");
        Fixture fixture = factory.getFixture("GuiceSystemUnderDevelopmentTest$MyPlain");
        assertFixture(fixture);

        assertEquals(MyPlainFixture.class, fixture.getTarget().getClass());
    }

    public void testThatWeCanRetrieveAnInjectedFixtureFromGuice() throws Throwable
    {
        GuiceSystemUnderDevelopment sud = new GuiceSystemUnderDevelopment();

        sud.addImport("com.greenpepper.extensions.guice");
        Fixture fixture = sud.getFixture("GuiceSystemUnderDevelopmentTest$MyInjected");
        assertFixture(fixture);

        assertEquals(MyInjectedFixture.class, fixture.getTarget().getClass());
        MyInjectedFixture target = (MyInjectedFixture) fixture.getTarget();
        assertNotNull(target.getFoo());
        assertNotNull(target.getFooInjectedMember());
    }

    public void testThatWeCanRetrieveAnInjectedFixtureFromGuiceNeedingModules() throws Throwable
    {
        GuiceSystemUnderDevelopment sud = new GuiceSystemUnderDevelopment();

        sud.addModules("GuiceSystemUnderDevelopmentTest$BarModule");
        sud.addImport("com.greenpepper.extensions.guice");
        Fixture fixture = sud.getFixture("GuiceSystemUnderDevelopmentTest$MyInjectedUsingModules");
        assertFixture(fixture);

        assertEquals(MyInjectedUsingModulesFixture.class, fixture.getTarget().getClass());
        MyInjectedUsingModulesFixture target = (MyInjectedUsingModulesFixture) fixture.getTarget();
        assertEquals(BarImpl.BAR_VALUE, target.getBar().doBar());
    }

    public void testThatWeCanRetrieveTheFixtureFromTheFallBackFactoryIfParametersArePassedAndStillInjectsMembers() throws Throwable
    {
    	GuiceSystemUnderDevelopment sud = new GuiceSystemUnderDevelopment();

        Fixture fixture = sud.getFixture(MyFixtureWithParams.class.getName(), "param");
        assertFixture(fixture);

        assertEquals(MyFixtureWithParams.class, fixture.getTarget().getClass());
        MyFixtureWithParams target = (MyFixtureWithParams) fixture.getTarget();
        assertEquals("param", target.getParam());
        assertNotNull(target.getFoo());
    }

    /******************************************************************************************/
    /********************************* DUMMY CLASSES ******************************************/
    public static class MyPlainFixture
    {
        public MyPlainFixture(){}
    }

    public static class FooImpl
    {
        public FooImpl(){}
    }

    public static class MyInjectedFixture
    {
    	private FooImpl foo;
    	private FooImpl fooInjectedMember;

    	@Inject
    	public MyInjectedFixture(FooImpl foo)
    	{
    		this.foo = foo;
    	}

		public FooImpl getFoo() {
			return foo;
		}

        @Inject
        public void setFooInjectedMember(FooImpl foo)
        {
        	this.fooInjectedMember = foo;
        }
        public FooImpl getFooInjectedMember(){return fooInjectedMember;}
    }

    public static interface Bar
    {
    	public int doBar();
    }
    public static class BarImpl implements Bar
    {
    	public static final int BAR_VALUE = 1;

		public int doBar() {
			return BAR_VALUE;
		}
    }

    public static class MyInjectedUsingModulesFixture
    {
    	private Bar bar;

    	@Inject
    	public MyInjectedUsingModulesFixture(Bar bar)
    	{
    		this.bar = bar;
    	}

		public Bar getBar() {
			return bar;
		}
    }
    public static class BarModule extends AbstractModule
    {
		@Override
		protected void configure()
		{
			bind(Bar.class).to(BarImpl.class);
		}
    }

    public static class MyFixtureWithParams
    {
        private String param;
        private FooImpl foo;

        public MyFixtureWithParams(String param){this.param = param;}
        public String getParam(){return param;}

        @Inject
        public void setFoo(FooImpl foo)
        {
        	this.foo = foo;
        }
        public FooImpl getFoo(){return foo;}
    }
}
