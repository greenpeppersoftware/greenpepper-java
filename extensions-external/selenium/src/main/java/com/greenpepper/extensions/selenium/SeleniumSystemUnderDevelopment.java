package com.greenpepper.extensions.selenium;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.thoughtworks.selenium.HttpCommandProcessor;

public class SeleniumSystemUnderDevelopment extends DefaultSystemUnderDevelopment
{

	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 4444;
	private static final String DEFAULT_URL = "http://www.greenpeppersoftware.com";
	private static final String DEFAULT_BROWSER_COMMAND = "*firefox";

	public SeleniumSystemUnderDevelopment() {
		addImport(SeleniumFixture.class.getPackage().getName());
	}

	@Override
	public Fixture getFixture(String name, String... params) throws Throwable {
		if(name != null && !name.equals("selenium"))
			return super.getFixture(name, params);
		
		if(params.length == 4)
			return new SeleniumFixture(new HttpCommandProcessor(params[0], Integer.parseInt(params[1]), params[2], params[3]));
		
		return new SeleniumFixture(new HttpCommandProcessor(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_BROWSER_COMMAND, DEFAULT_URL));
	}

}
