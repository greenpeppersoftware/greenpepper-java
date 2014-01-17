/**
 *
 */
package com.greenpepper.interpreter.column;

import com.greenpepper.Example;
import com.greenpepper.ExecutionContext;
import com.greenpepper.Statistics;
import com.greenpepper.Variables;

public abstract class Column
{
    protected ExecutionContext context = new Variables();

    public void bindTo(ExecutionContext context)
    {
        this.context = context;
    }

    public abstract Statistics doCell(Example cell) throws Exception;
}
