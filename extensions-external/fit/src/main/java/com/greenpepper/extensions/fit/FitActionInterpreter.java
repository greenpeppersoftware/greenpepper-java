package com.greenpepper.extensions.fit;

import com.greenpepper.Example;
import com.greenpepper.interpreter.flow.AbstractFlowInterpreter;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.ExampleUtil;

public class FitActionInterpreter extends AbstractFlowInterpreter
{
	private SystemUnderDevelopment sud;
	private FitActionRowSelector selector;
	
	public FitActionInterpreter(SystemUnderDevelopment sud, Fixture fixture, boolean timed)
	{
        selector = new FitActionRowSelector(sud, fixture, timed);
        setRowSelector(selector);
		this.sud = sud;
	}

	@Override
    protected Example firstRowOf(Example next)
    {
		if(Fit.isAFitInterpreter(sud, ExampleUtil.contentOf(next.firstChild())))
			return next.at( 0, 0 );
		
		return next.at( 0, 1 );
	}
	
	protected FitActionRowSelector getRowSelector()
	{
		return selector;
	}
}
