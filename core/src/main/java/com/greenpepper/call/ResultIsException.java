package com.greenpepper.call;

public class ResultIsException implements ResultMatcher
{
    public boolean matches(Result result)
    {
        return result.isException();
    }
}
