package com.greenpepper.interpreter;

import com.greenpepper.Specification;
import com.greenpepper.Statistics;

public class SkipInterpreter extends AbstractInterpreter
{
    private final Statistics stats;

    public SkipInterpreter()
    {
        this( new Statistics() );
    }

    public SkipInterpreter( Statistics stats )
    {
        this.stats = stats;
    }

    public void interpret( Specification specification )
    {
        specification.nextExample();
        specification.exampleDone( stats );
    }
}
