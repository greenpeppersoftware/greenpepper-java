package com.greenpepper.interpreter.column;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Statistics;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.Compile;
import com.greenpepper.call.Result;
import com.greenpepper.call.Stub;
import com.greenpepper.reflect.Message;
import com.greenpepper.util.ExampleUtil;

public class SavedColumn extends Column
{
    private final Message message;

    public SavedColumn(Message message)
    {
        this.message = message;
    }

    public Statistics doCell(final Example cell) throws Exception
    {
        final String symbol = ExampleUtil.contentOf( cell );
        Statistics stats = new Statistics();
        Call call = new Call( message );
        call.will( Annotate.ignored( cell ) );
        call.will( Compile.statistics( stats ) );
        call.will( new SaveResultAs( symbol ) );
        call.execute();

        return stats;
    }

    private class SaveResultAs implements Stub
    {
        private final String symbol;

        public SaveResultAs(String symbol)
        {
            this.symbol = symbol;
        }

        public void call(Result result)
        {
            context.setVariable( symbol, result.getActual() );
        }
    }
}
