package com.greenpepper.extensions.selenium;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.thoughtworks.selenium.CommandProcessor;
import junit.framework.TestCase;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

public class SeleniumSystemUnderDevelopmentTest extends TestCase {

	private SystemUnderDevelopment sud = new SeleniumSystemUnderDevelopment();

	private SeleniumServer seleniumServer;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		RemoteControlConfiguration configuration = new RemoteControlConfiguration();
		configuration.setPort(4433);

		seleniumServer = new SeleniumServer(configuration);
		seleniumServer.start();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		seleniumServer.stop();
	}

    public void testEmptySoJUnitWontComplain() throws Throwable {

    }


	public void xtestCanRunSeleniumCommands() throws Throwable {
		Fixture fixture = sud.getFixture("selenium", "localhost", "4433",
				"*firefox", "http://www.google.com");
		CommandProcessor processor = (CommandProcessor) fixture.getTarget();
		try {
			processor.start();
			Message assertTitleMessage = fixture.check("assertTitle");
			Object result = assertTitleMessage.send("Google");
			assertEquals("OK", result);
		} catch (Throwable t) {
			if (processor != null)
				processor.stop();
		}
	}
}
