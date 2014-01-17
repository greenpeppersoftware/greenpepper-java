/**
 * 
 */
package com.greenpepper.interpreter.column;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Statistics;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.Compile;
import com.greenpepper.expectation.ShouldBe;
import com.greenpepper.reflect.Message;
import com.greenpepper.util.StringUtil;

public class ExpectedColumn extends Column
{
    private Message check;

    public ExpectedColumn( Message check ) throws Exception
    {
        this.check = check;
    }

    public Statistics doCell( Example cell ) throws Exception
    {
        Statistics stats = new Statistics();
        Call call = new Call( check );
        if (!StringUtil.isBlank( cell.getContent() ))
        {
            call.expect( ShouldBe.literal( cell.getContent() ) );
        }

        call.will( Annotate.withDetails( cell ) );
        call.will( Compile.statistics( stats ) );
        call.execute();
        return stats;
    }
}