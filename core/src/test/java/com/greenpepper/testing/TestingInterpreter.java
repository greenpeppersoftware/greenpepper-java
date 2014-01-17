package com.greenpepper.testing;

import com.greenpepper.Interpreter;
import com.greenpepper.Specification;
import com.greenpepper.reflect.Fixture;

public class TestingInterpreter implements Interpreter
{
    public TestingInterpreter( Fixture fixture )
    {
    }

    // So that maven does not complain. It seems to consider this
    // class as a test, maybe because of the Testing fixture
    public TestingInterpreter()
    {
    }

    public void interpret(Specification specification)
    {
    }
}
