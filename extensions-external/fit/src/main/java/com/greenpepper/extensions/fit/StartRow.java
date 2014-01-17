package com.greenpepper.extensions.fit;

import com.greenpepper.Specification;
import com.greenpepper.interpreter.flow.Row;
import com.greenpepper.reflect.Fixture;

public class StartRow implements Row 
{
	public StartRow(Fixture fixture, Boolean timed) {}
	
	public void interpret(Specification row) 
	{
		row.nextExample();
	}
}
