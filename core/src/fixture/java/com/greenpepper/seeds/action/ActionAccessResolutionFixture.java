package com.greenpepper.seeds.action;

import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.seeds.action.ActionExampleResolution;

public class ActionAccessResolutionFixture
{
    public String access;
    private PlainOldFixture target;
    
    public ActionAccessResolutionFixture()
    {
        this.target = new PlainOldFixture(new ActionExampleResolution());
    }
    
    public boolean canSetAValueUsing() throws Exception
    {
        try
        {
        	target.send(access).send("1");
        	return true;
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }
        catch (NoSuchMessageException e)
        {
            return false;
        }
    }
    
    public boolean canGetAValueUsing() throws Exception
    {
        try
        {
        	Message msg = target.check(access);
        	return msg.send(args(msg)) != null;
        }
        catch (NoSuchMessageException e)
        {
            return false;
        }
    }
    
    private String[] args(Message msg)
    {
    	int arity = msg.getArity();
    	String[] args = new String[arity];
    	for(int i = 0; i < arity; i++)
    	{
    		args[i] = "1";
    	}
    	
    	return args;
    }
}
