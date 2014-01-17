package com.greenpepper.interpreter;

import com.greenpepper.interpreter.flow.AbstractFlowInterpreter;
import com.greenpepper.interpreter.flow.action.ActionRowSelector;
import com.greenpepper.reflect.Fixture;

public class ActionInterpreter extends AbstractFlowInterpreter
{
    public ActionInterpreter(Fixture fixture)
    {
        setRowSelector( new ActionRowSelector( fixture ) );
    }
}
