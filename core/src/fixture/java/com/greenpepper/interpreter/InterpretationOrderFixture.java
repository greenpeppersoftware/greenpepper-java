package com.greenpepper.interpreter;

import com.greenpepper.Interpreter;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.Tables;

import java.util.ArrayList;
import java.util.List;

public class InterpretationOrderFixture
{
    public Tables table;

    public String[] orderOfInterpretation() throws Exception
    {
        FlowTracer tracer = new FlowTracer();
        Interpreter interpreter = interpreterFor( table, new PlainOldFixture( tracer ) );
        interpreter.interpret( new FakeSpecification( table ) );

        return tracer.trace.toArray( new String[tracer.trace.size()] );
    }

    private Interpreter interpreterFor(Tables table, Fixture fixture) throws Exception
    {
        String interpreterCalled = table.firstChild().firstChild().at( 0 ).getContent();

        if ("setup".equals( interpreterCalled ))
        {
            return new SetupInterpreter( fixture );
        }
        else if ("rule for".equals( interpreterCalled ))
        {
            return new RuleForInterpreter( fixture );
        }
        throw new Exception( "No interpreter found" );
    }

    public static class FlowTracer
    {
        public final List<String> trace = new ArrayList<String>();

        public void header(String cellValue)
        {
            trace.add( cellValue );
        }
    }
}
