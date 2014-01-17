package com.greenpepper.interpreter;

import com.greenpepper.Statistics;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;

public class CommentInterpreter extends SkipInterpreter
{
    public CommentInterpreter(SystemUnderDevelopment sud)
    {
        this();
    }
    
    public CommentInterpreter()
    {
    	super(new Statistics( 0, 0, 0, 1 ));
    }
}
