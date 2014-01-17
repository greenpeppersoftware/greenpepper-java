package com.greenpepper.systemunderdevelopment;

import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;

public class GreenPepperSystemUnderDevelopment extends DefaultSystemUnderDevelopment
{

    public GreenPepperSystemUnderDevelopment()
    {
		addImports();
    }

    public GreenPepperSystemUnderDevelopment(String... params)
    {
        this();
    }
	
	private void addImports()
	{
        addImport("com.greenpepper.document");
        addImport("com.greenpepper.interpreter");
        addImport("com.greenpepper.interpreter.flow.dowith");
        addImport("com.greenpepper.interpreter.flow.action");
        addImport("com.greenpepper.interpreter.seeds");
        addImport("com.greenpepper.interpreter.seeds.action");
        addImport("com.greenpepper.interpreter.seeds.comparison");
        addImport("com.greenpepper.ogn");
	}
}
