package com.greenpepper.extensions.ognl;

import com.greenpepper.Call;
import com.greenpepper.reflect.DefaultFixture;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;

public class OgnlSyntaxFixture
{
    private Message msg;

    private Fixture target;

    public OgnlSyntaxFixture()
    {
        this.target = new OgnlFixture(new DefaultFixture(
                new ActionExampleResolution()));
    }

    public void check(String action) throws Exception
    {
        msg = target.check(action);
    }

    public String result() throws Exception
    {
        Call call = new Call(msg);
        return call.execute(args(msg)).toString();
    }
    
    public boolean isUsingOGNL()
    {
        return msg instanceof OgnlSetter || msg instanceof OgnlGetter;
    }

    private String[] args(Message msg)
    {
        return new String[0];
    }
}
