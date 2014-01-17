package com.greenpepper.extensions.ognl;

public abstract class OgnlExpressionException extends RuntimeException
{
    private final String expression;

    public OgnlExpressionException( String expression )
    {
        this.expression = expression;
    }

    public String getExpression()
    {
        return expression;
    }
}
