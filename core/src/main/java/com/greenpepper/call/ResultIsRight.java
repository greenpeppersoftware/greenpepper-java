package com.greenpepper.call;

public class ResultIsRight implements ResultMatcher
{
    public boolean matches(Result result)
    {
        return result.isRight();
    }
}
