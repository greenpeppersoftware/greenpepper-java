package com.greenpepper.extensions.fit;


import com.greenpepper.Example;
import com.greenpepper.TypeLoaderChain;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.interpreter.flow.Row;
import com.greenpepper.interpreter.flow.dowith.DoWithRowSelector;
import com.greenpepper.interpreter.flow.dowith.SkipRow;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Type;
import com.greenpepper.reflect.TypeLoader;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.ExampleUtil;

public class FitActionRowSelector extends DoWithRowSelector
{
	private Fixture fitFixture;
	private SystemUnderDevelopment sud;
	private boolean timed;
    private TypeLoader<Row> typeLoader;
	
    public FitActionRowSelector(SystemUnderDevelopment sud, Fixture fixture, boolean timed) 
    {
		super(fixture);
		this.sud = sud;
		this.timed = timed;
        this.typeLoader = new TypeLoaderChain<Row>(Row.class);
        typeLoader.searchPackage(getClass().getPackage().getName());
        typeLoader.addSuffix("Row");
	}

	public Row select(Example example)
    {
        if (isARow(identifier(example)))
            return instantiateRow(example);

        try 
        {
			if (Fit.isAnActionFitInterpreter(sud, identifier(example)))
			{
				timed = Fit.isATimedActionFitInterpreter(sud, identifier(example));
				if(timed)
				{
					example.addChild().setContent("time");
					example.addChild().setContent("split");
				}
				
				fitFixture = sud.getFixture(identifier(example));
				return new SkipRow();
			}
			
			if (Fit.isAFitInterpreter(sud, identifier(example)))
				return new FitInterpretRow(sud, fixture);
		}
        catch (Throwable e) 
		{
		}

        
        return new FitDefaultRow(fitFixture, fixture, timed);
    }

    protected Row instantiateRow(Example row)
    {
        Type<Row> rowClass = loadRowType(identifier(row));
        try
        {
            return rowClass.newInstance(fixture, timed);
        }
        catch (Throwable throwable)
        {
            row.firstChild().annotate(Annotations.exception(throwable));
            return new SkipRow();
        }
    }

    protected boolean isARow(String name)
    {
        Type<Row> type = loadRowType(name);
        return type != null && !type.getUnderlyingClass().equals(Row.class);
    }

    private Type<Row> loadRowType(String name)
    {
        return typeLoader.loadType(name);
    }

    protected String identifier(Example row)
    {
        return ExampleUtil.contentOf(row.firstChild());
    }
}
