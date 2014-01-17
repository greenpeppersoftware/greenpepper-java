package com.greenpepper.interpreter.column;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Statistics;
import com.greenpepper.TypeConversion;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.Compile;
import com.greenpepper.call.ResultIs;
import com.greenpepper.reflect.Message;
import com.greenpepper.util.ExampleUtil;

public class RecalledColumn extends Column
{
    private final Message message;

    public RecalledColumn(Message message)
    {
        this.message = message;
    }

    public Statistics doCell(Example cell) throws Exception
    {
        String symbol = ExampleUtil.contentOf( cell );
        Object variable = context.getVariable( symbol );
        cell.setContent( TypeConversion.toString( variable ) );

        Statistics stats = new Statistics();
        Call call = new Call( message );
        call.will(Annotate.exception( cell )).when( ResultIs.exception());
        call.will(Compile.statistics(stats)).when(ResultIs.exception());

        call.execute( TypeConversion.toString( variable ) );

        return stats;
    }
}
