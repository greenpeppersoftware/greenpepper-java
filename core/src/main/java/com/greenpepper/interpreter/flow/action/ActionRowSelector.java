package com.greenpepper.interpreter.flow.action;

import com.greenpepper.Example;
import com.greenpepper.interpreter.flow.Row;
import com.greenpepper.interpreter.flow.RowSelector;
import com.greenpepper.reflect.Fixture;

public class ActionRowSelector implements RowSelector
{
    private Fixture fixture;

    public ActionRowSelector(Fixture fixture)
    {
        super();
        this.fixture = fixture;
    }

    public Row select(Example example)
    {
        return new ActionRow( fixture );
    }
}
