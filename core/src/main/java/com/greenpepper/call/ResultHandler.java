package com.greenpepper.call;

public class ResultHandler implements StubSyntax
{
    private final Stub stub;
    private ResultMatcher matcher;

    public ResultHandler( Stub stub )
    {
        this.stub = stub;
        this.matcher = new AnyResult();
    }

    public void when( ResultMatcher matcher )
    {
        this.matcher = matcher;
    }

    public void handle( Result result )
    {
        if (matcher.matches( result )) stub.call( result );
    }
}
