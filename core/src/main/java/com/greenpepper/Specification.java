package com.greenpepper;

public interface Specification extends ExecutionContext
{
    Example nextExample();

    boolean hasMoreExamples();

    void exampleDone( Statistics statistics );
}
