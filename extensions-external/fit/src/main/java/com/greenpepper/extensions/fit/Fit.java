package com.greenpepper.extensions.fit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import fit.ActionFixture;
import fit.TimedActionFixture;

public class Fit 
{
    public static DateFormat format = new SimpleDateFormat("hh:mm:ss");
	
    
    public static boolean isATimedActionFitInterpreter(SystemUnderDevelopment sud, String name)
	{
        try
        {           
        	Object target = sud.getFixture(name).getTarget();
            if (target instanceof TimedActionFixture)
            	return true;
        }
        catch (Throwable t)
        {
        }
        
    	return false;
	}
    
	public static boolean isAnActionFitInterpreter(SystemUnderDevelopment sud, String name)
	{
        try
        {           
        	Object target = sud.getFixture(name).getTarget();
            if (target.getClass().equals(ActionFixture.class))
            	return false;
            if (target instanceof ActionFixture)
            	return true;
        }
        catch (Throwable t)
        {
        }
        
    	return false;
	}
	
	public static boolean isAFitInterpreter(SystemUnderDevelopment sud, String name)
	{
        try
        {
        	Object target = sud.getFixture(name).getTarget();
            if (target instanceof fit.Fixture && !target.getClass().equals(ActionFixture.class))
            	return true;
        }
        catch (Throwable t)
        {
        }
        
    	return false;
	}
}
