package com.greenpepper.call;

public class AnyResult implements ResultMatcher
{
    public boolean matches(Result result)
    {
        return true;
    }
}
