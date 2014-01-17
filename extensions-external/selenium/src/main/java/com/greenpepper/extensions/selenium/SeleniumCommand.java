package com.greenpepper.extensions.selenium;

import com.greenpepper.reflect.Message;
import com.thoughtworks.selenium.CommandProcessor;

public class SeleniumCommand extends Message {

	private String commandName;
	
	private CommandProcessor commandProcessor;
	
	public SeleniumCommand(CommandProcessor commandProcessor, String commandName) {
		this.commandProcessor = commandProcessor;
		this.commandName = commandName;
	}

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public String send(String... args) throws Exception {
		return commandProcessor.doCommand(commandName, args);
	}

}
