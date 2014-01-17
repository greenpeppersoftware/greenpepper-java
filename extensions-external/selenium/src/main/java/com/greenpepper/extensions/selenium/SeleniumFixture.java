package com.greenpepper.extensions.selenium;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;
import com.thoughtworks.selenium.CommandProcessor;

public class SeleniumFixture implements Fixture {

	private final CommandProcessor processor;
	
	public SeleniumFixture(CommandProcessor processor) {
		this.processor = processor;
	}

	public boolean canCheck(String message) {
		return message != null;
	}

	public boolean canSend(String message) {
		return canCheck(message);
	}

	public Message check(String message) throws NoSuchMessageException {
		return new SeleniumCommand(processor, message);
	}

	public Fixture fixtureFor(Object target) {
		if(!(target instanceof CommandProcessor))
			throw new IllegalArgumentException("Can only get a SeleniumFixture for an instance of CommandProcessor.");
		
		return new SeleniumFixture((CommandProcessor)target);
	}

	public CommandProcessor getTarget() {
		return processor;
	}

	public Message send(String message) throws NoSuchMessageException {
		return check(message);
	}

}
