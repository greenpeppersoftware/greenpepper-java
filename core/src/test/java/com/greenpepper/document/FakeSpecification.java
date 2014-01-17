package com.greenpepper.document;

import com.greenpepper.Example;
import com.greenpepper.Statistics;

public class FakeSpecification extends AbstractSpecification
{
    public Statistics stats = new Statistics();

    public FakeSpecification( Example start )
    {
        setStart( start );
    }

    protected Example peek()
    {
        return cursor.nextSibling();
    }

    public void exampleDone( Statistics statistics )
    {
        stats.tally( statistics );
    }
    
    public Statistics stats()
    {
    	return stats;
    }
}
