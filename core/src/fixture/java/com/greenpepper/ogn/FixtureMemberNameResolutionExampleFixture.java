package com.greenpepper.ogn;

import com.greenpepper.Call;
import com.greenpepper.reflect.DefaultFixture;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.seeds.action.ActionExampleResolution;

public class FixtureMemberNameResolutionExampleFixture
{
    public String call;
    private Fixture target;

    public FixtureMemberNameResolutionExampleFixture()
    {
        this.target = new DefaultFixture(new ActionExampleResolution());
    }

    public String returnString() throws Exception
    {
        Message msg = target.check(call);
        Call call = new Call(msg);
        return call.execute(args(msg)).toString();
    }

    private String[] args(Message msg)
    {
        int arity = msg.getArity();
        String[] args = new String[arity];
        for(int i = 0; i < arity; i++)
        {
            args[i] = "1";
        }

        return args;
    }
}
