package com.greenpepper.interpreter;

import java.util.ArrayList;
import java.util.Collection;

import com.greenpepper.GreenPepper;
import com.greenpepper.Interpreter;
import com.greenpepper.Statistics;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.AnnotationTable;
import com.greenpepper.util.FakeExample;
import com.greenpepper.util.Rows;
import com.greenpepper.util.Tables;

public class StatisticsAndAnnotationsFixture
{
    public Tables table;
    public String[] headers;
    public String rwie;
    public ArrayList<Value> valueList;

    public int a;
    public int b;


    private Interpreter interpreter;

    public StatisticsAndAnnotationsFixture(String interpreterName) throws Throwable
    {
        interpreter = GreenPepper.getInterpreter(interpreterName, new Object[] {new PlainOldFixture(this)});
    }

    public void setValues(String[] values)
    {
        valueList = new ArrayList<Value>();

        for (String value : values)
        {
            valueList.add(new Value(value));
        }
    }


    public Collection<Value> query()
    {
        return valueList;
    }


    public int division()
    {
        return a / b;
    }

    public void prepareRWIE(Statistics stats)
    {
        rwie = stats.rightCount() + " " + stats.wrongCount() + " " + stats.ignoredCount() + " " + stats.exceptionCount();
    }

    public AnnotationTable annotations() throws Exception
    {
        FakeExample example = new Tables();

        example.addChild(new Rows());
        example.addChild((Rows)table.at(0, 0));

        FakeSpecification specification = new FakeSpecification( example );
        interpreter.interpret(specification);

        prepareRWIE(specification.stats());

        return new AnnotationTable(table);
    }

    public static class Value
    {
        public String value;
        private String other;

        public Value(String value)
        {
            String[] subvalues = value.split(":");

            this.value = subvalues[0];
            this.other = (subvalues.length > 1) ? subvalues[1] : "";
        }

        public int getOther()
        {
            return Integer.parseInt(other);
        }
    }

}
