package com.greenpepper.document;

import com.greenpepper.Interpreter;
import com.greenpepper.Example;

public interface InterpreterSelector
{
    Interpreter selectInterpreter(Example table);
}
