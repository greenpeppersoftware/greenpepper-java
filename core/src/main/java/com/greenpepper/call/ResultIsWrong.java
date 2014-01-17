package com.greenpepper.call;

public class ResultIsWrong implements ResultMatcher
{
    public boolean matches(Result result)
    {
        return result.isWrong();
    }
}
