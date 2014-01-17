package com.greenpepper.extensions.fit;

import com.greenpepper.Example;
import com.greenpepper.GreenPepper;
import com.greenpepper.Interpreter;
import com.greenpepper.document.GreenPepperInterpreterSelector;
import com.greenpepper.interpreter.SetOfInterpreter;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import fit.ActionFixture;
import fit.ColumnFixture;
import fit.RowFixture;
import fit.TimedActionFixture;

public class FitInterpreterSelector extends GreenPepperInterpreterSelector
{	
	public FitInterpreterSelector(SystemUnderDevelopment systemUnderDevelopment) 
	{
		super(systemUnderDevelopment);
    	GreenPepper.register(new DateConverter());
	}

	public Interpreter selectInterpreter(Example fitTable)
	{
        try
        {        	
    		Example cells = fitTable.at(0, 0, 0);
            Fixture fitFixture = systemUnderDevelopment.getFixture(cells.at(0).getContent());
                        
            if (fitFixture.getTarget() instanceof TimedActionFixture)
            {
            	Fixture targetedFixture = systemUnderDevelopment.getFixture(fitTable.at(0, 1, 1).at(0).getContent());
            	return new FitActionInterpreter(systemUnderDevelopment, targetedFixture, true);
            }
                        
            if (fitFixture.getTarget() instanceof ActionFixture)
            {
            	Fixture targetedFixture = systemUnderDevelopment.getFixture(fitTable.at(0, 1, 1).at(0).getContent());
            	return new FitActionInterpreter(systemUnderDevelopment, targetedFixture, false);
            }
            
            if (fitFixture.getTarget() instanceof RowFixture)
            	return GreenPepper.getInterpreter(SetOfInterpreter.class.getName(), fitFixture);
            
            if (fitFixture.getTarget() instanceof ColumnFixture)
            	return new FitRuleForInterpreter(fitFixture);
            
            //if(fitFixture.getTarget() instanceof SummaryFixture)
            //{
            	// Add Stats ?
            //	return new SkipInterpreter();
            //}
        }
        catch (Throwable t)
        {
        }
        
        return super.selectInterpreter(fitTable);
	}
}
