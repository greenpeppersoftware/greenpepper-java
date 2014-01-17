package com.greenpepper.samples.application.bank;

import com.greenpepper.extensions.guice.GuiceSystemUnderDevelopment;

public class BankSystemUnderDevelopment
		extends GuiceSystemUnderDevelopment
{

	public BankSystemUnderDevelopment()
			throws Exception
	{
		super();
		addImport("com.greenpepper.samples.application.bank");
		addImport("com.greenpepper.samples.application.mortgage");
	}
}