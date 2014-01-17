package com.greenpepper.interpreter.flow;

import com.greenpepper.Example;

public interface RowSelector
{
    Row select(Example example);
}