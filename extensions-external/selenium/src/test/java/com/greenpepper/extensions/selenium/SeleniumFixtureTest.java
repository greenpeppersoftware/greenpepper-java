package com.greenpepper.extensions.selenium;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.thoughtworks.selenium.CommandProcessor;
import junit.framework.TestCase;

public class SeleniumFixtureTest extends TestCase {

	private Mockery context = new JUnit4Mockery();
	private Fixture fixture;
	private CommandProcessor commandProcessor;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		commandProcessor = context.mock(CommandProcessor.class);
		fixture = new SeleniumFixture(commandProcessor);
	}

	@Override
	protected void tearDown() throws Exception {
		context.assertIsSatisfied();
	}

	public void testCanCheckAnyNonNullMessage(){
		assertTrue(fixture.canCheck("any"));
		assertTrue(fixture.canCheck(""));
		assertFalse(fixture.canCheck(null));
	}

	public void testCanSendAnyNonNullMessage(){
		assertTrue(fixture.canSend("any"));
		assertTrue(fixture.canSend(""));
		assertFalse(fixture.canSend(null));
	}

	public void testThatGettingACheckMessageReturnsASeleniumCommand(){
		assertTrue(fixture.check("assertTitle") instanceof SeleniumCommand);
	}
	
	public void testThatGettingASendMessageReturnsASeleniumCommand(){
		assertTrue(fixture.send("assertTitle") instanceof SeleniumCommand);
	}

	public void testThatSendingASeleniumCommandExecutesItUsingAnCommandProcessor() throws Exception{

		context.checking(new Expectations()
		{{
			oneOf(commandProcessor).doCommand("assertTitle", new String[]{"Google"});
			will(returnValue("OK"));
				
		}});
		Message message = fixture.check("assertTitle");
		assertEquals("OK", message.send("Google"));
	}
	
	public void testThatGettingTheTargetReturnsTheCommandProcessor(){
		assertEquals(commandProcessor, fixture.getTarget());
	}
	
	public void testCanObtainASeleniumFixtureForAnObjectOfTypeCommandProcessor(){
		assertTrue(fixture.fixtureFor(commandProcessor) instanceof SeleniumFixture);
	}
	
	public void testThatTryingToObtainASeleniumFixtureForAnObjectThatIsNotACommandProcessorThrowsAnException(){
		try{
			fixture.fixtureFor("string");
			fail("Should have thrown an exception.");
		}
		catch(IllegalArgumentException e){
			assertTrue(true);
		}
	}
}