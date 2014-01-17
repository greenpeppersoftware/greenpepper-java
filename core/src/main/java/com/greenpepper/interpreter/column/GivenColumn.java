/**
 * 
 */
package com.greenpepper.interpreter.column;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Statistics;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.Compile;
import com.greenpepper.call.ResultIs;
import com.greenpepper.reflect.Message;

public class GivenColumn extends Column
{
    private Message send;

    public GivenColumn( Message send ) throws Exception
    {
        this.send = send;
    }

    public Statistics doCell( Example cell ) throws Exception
    {
    	Statistics stats = new Statistics();
        
    	Call call = new Call( send );
        call.will(Annotate.exception( cell )).when(ResultIs.exception());
        call.will(Compile.statistics(stats)).when(ResultIs.exception());
        call.execute( cell.getContent() );
        
        return stats;
    }
}
