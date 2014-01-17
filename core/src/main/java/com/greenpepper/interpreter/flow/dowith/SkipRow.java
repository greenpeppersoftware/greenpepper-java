package com.greenpepper.interpreter.flow.dowith;

import com.greenpepper.Specification;
import com.greenpepper.interpreter.flow.Row;

public class SkipRow implements Row
{

    public void interpret(Specification row)
    {
        row.nextExample();
    }
}
