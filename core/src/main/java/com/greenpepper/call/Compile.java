package com.greenpepper.call;

import com.greenpepper.Statistics;

public final class Compile
{
    public static Stub statistics(Statistics stats)
    {
        return new CompileStatistics( stats );
    }

    private Compile()
    {
    }
}
