package com.greenpepper.extensions.fit;

import com.greenpepper.Example;
import com.greenpepper.Statistics;
import static com.greenpepper.annotation.Annotations.exception;
import com.greenpepper.interpreter.RuleForInterpreter;
import com.greenpepper.interpreter.column.Column;
import com.greenpepper.interpreter.column.ExpectedColumn;
import com.greenpepper.reflect.Fixture;
import fit.ColumnFixture;

public class FitRuleForInterpreter extends RuleForInterpreter
{
    protected boolean hasExecuted = false;

    public FitRuleForInterpreter( Fixture fixture )
    {
        super(fixture);
    }

    @Override
    protected void doCell( Column column, Example cell )
    {
        try
        {
        	if(column instanceof ExpectedColumn && !hasExecuted)
        	{
        		((ColumnFixture)fixture.getTarget()).execute();
        		hasExecuted = true;
        	}
        	
        	Statistics cellStats = column.doCell( cell );
        	
        	if(!cell.hasSibling())
            	((ColumnFixture)fixture.getTarget()).reset();
        	
            stats.tally( cellStats );
        }
        catch (Exception e)
        {
            cell.annotate( exception( e ) );
            stats.exception();
        }
        finally
        {
        	if(!cell.hasSibling())
        		hasExecuted = false;
        }
    }
}
