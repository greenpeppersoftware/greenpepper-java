package com.greenpepper.extensions.dynabeans;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.NameUtils;
import com.greenpepper.util.TestCase;
import junit.framework.Assert;
import junit.framework.TestSuite;

public class DynaBeanFixtureTest extends junit.framework.TestCase {
    private static Class[] contexts = new Class[]{
            WhateverTheTarget.class,
            WhenTargetIsALazyDynaBean.class,
            WhenTargetIsABasicDynaBean.class,
    };

    public static TestSuite suite() {
        TestSuite suite = new TestSuite(NameUtils.humanize(DynaBeanFixture.class.getSimpleName()));
        for (Class context : contexts) {
            suite.addTest(new TestSuite(context, NameUtils.humanize(context.getSimpleName())));
        }
        return suite;
    }

    public static class WhateverTheTarget extends TestCase {

        private Fixture fixture;
        private Target target;

        protected void setUp() throws Exception {
            target = new Target();
            fixture = new DynaBeanFixture(new PlainOldFixture(target));
        }

        public void testCheckMessagesAreDelegatedToDecoratedFixtureIfPossible() throws Exception {
            assertTrue(fixture.canCheck("javaProperty"));
            target.javaProperty = "value";
            assertEquals("value", fixture.check("javaProperty").send());
        }

        public void testSendMessagesAreDelegatedToDecoratedFixtureIfPossible() throws Exception {
            assertTrue(fixture.canSend("javaProperty"));
            fixture.send("javaProperty").send("value");
            assertEquals("value", target.javaProperty);
        }

        public void testFixtureShouldHandleNonDynaBeansProperly() {
            assertFalse(fixture.canSend("property"));
            try {
                fixture.send("property");
                fail();
            } catch (NoSuchMessageException expected) {
                assertTrue(true);
            }
            assertFalse(fixture.canCheck("property"));
            try {
                fixture.check("property");
                fail();
            } catch (NoSuchMessageException expected) {
                assertTrue(true);
            }
        }
    }

    public static class WhenTargetIsALazyDynaBean extends TestCase {
		private Mockery context = new JUnit4Mockery();
        private DynaBeanFixture fixture;
        private DynaBean target;

		@Override
        protected void setUp() throws Exception {
            target = new LazyDynaBean();

			final Fixture decorated = context.mock(Fixture.class);

			context.checking(new Expectations()
			{{
				between(0, 2).of(decorated).canCheck("property");
				will(returnValue(false));
				between(0, 2).of(decorated).canSend("property");
				will(returnValue(false));
				between(0, 2).of(decorated).getTarget();
				will(returnValue(target));
			}});
            fixture = new DynaBeanFixture(decorated);
        }

		@Override
		protected void tearDown() throws Exception {
			context.assertIsSatisfied();
		}

        public void testRespondsToAnyProperty() throws Exception {
            assertTrue(fixture.canCheck("property"));
            assertTrue(fixture.canSend("property"));
        }

        public void testCheckMessagesRetrievePropertyValues() throws Exception {
            target.set("property", "value");
            Message check = fixture.check("property");
            assertEquals("value", check.send());
        }

        public void testSendMessagesSetPropertyValues() throws Exception {
            Message set = fixture.send("property");
            set.send("value");
            Assert.assertEquals("value", target.get("property"));
        }
    }

    public static class WhenTargetIsABasicDynaBean extends TestCase {
		private Mockery context = new JUnit4Mockery();
		private Fixture decorated;
        private Fixture fixture;
        private DynaBean target;

		@Override
        protected void setUp() throws Exception {
            target = new BasicDynaBean(new BasicDynaClass("target", null, new DynaProperty[] {
                    new DynaProperty("string", String.class),
                    new DynaProperty("integer", int.class)
            }));

			decorated = context.mock(Fixture.class);
            fixture = new DynaBeanFixture(decorated);
        }

		@Override
		protected void tearDown() throws Exception {
			context.assertIsSatisfied();
		}

		public void testRespondsToDeclaredProperties() throws Exception {
			context.checking(new Expectations()
			{{
				oneOf(decorated).canCheck("string");
				will(returnValue(true));
				oneOf(decorated).canSend("integer");
				will(returnValue(true));
			}});

            assertTrue(fixture.canCheck("string"));
            assertTrue(fixture.canSend("integer"));
        }

        public void testComplainsAboutUndeclaredProperties() {

			context.checking(new Expectations()
			{{
				atLeast(2).of(decorated).canSend("property");
				will(returnValue(false));

				atLeast(2).of(decorated).canCheck("property");
				will(returnValue(false));

				atLeast(2).of(decorated).getTarget();
				will(returnValue(target));
			}});

            assertFalse(fixture.canSend("property"));
            try {
                fixture.send("property");
                fail();
            } catch (NoSuchMessageException expected) {
                assertTrue(true);
            }
            assertFalse(fixture.canCheck("property"));
            try {
                fixture.check("property");
                fail();
            } catch (NoSuchMessageException expected) {
                assertTrue(true);
            }
        }

//        public void testCheckMessagesRetrievePropertyValues() throws Exception {
//            target.set("property", "value");
//            Message check = fixture.check("property");
//            assertEquals("value", check.send());
//        }
//
//        public void testSendMessagesSetPropertyValues() throws Exception {
//            Message set = fixture.send("property");
//            set.send("value");
//            assertEquals("value", target.get("property"));
//        }
//
    }

    public static class Target
    {
        public String javaProperty;

        public void setJavaProperty( String s )
        {
            javaProperty = s;
        }

        public String getGetter()
        {
            return javaProperty;
        }
    }
}