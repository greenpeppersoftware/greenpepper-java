package com.greenpepper.extensions.ognl;

import java.util.ArrayList;
import java.util.List;

public class MemberResolutionPrecedenceFixture
{
    private List<String> list = new ArrayList<String>();

    public MemberResolutionPrecedenceFixture(String param)
            throws Exception
    {
        OgnlResolution resolver = new OgnlResolution(param);
        list.addAll(resolver.expressionsListToResolve());
    }

    public List<String> query()
    {
        return list;
    }
}
