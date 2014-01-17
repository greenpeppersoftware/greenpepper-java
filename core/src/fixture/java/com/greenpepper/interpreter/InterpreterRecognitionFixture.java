package com.greenpepper.interpreter;

import com.greenpepper.Interpreter;
import com.greenpepper.document.GreenPepperInterpreterSelector;
import com.greenpepper.document.InterpreterSelector;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.FakeExample;
import com.greenpepper.util.Rows;
import com.greenpepper.util.Tables;

public class InterpreterRecognitionFixture
{
    public String interpreterCalled;

    public Rows interpreterRow;

    private Interpreter interpreter;

    private InterpreterSelector interpreterSelector;

    public InterpreterRecognitionFixture()
    {
        SystemUnderDevelopment sud = new DefaultSystemUnderDevelopment();
        sud.addImport("com.greenpepper.interpreter");
        interpreterSelector = new GreenPepperInterpreterSelector( sud );
    }

    public String instanceOf()
    {
        interpreter = interpreterSelector
                .selectInterpreter(createFakeExampleForInterpreterRow(interpreterRow));
        return interpreter.getClass().getSimpleName();
    }

    public boolean isAnInterpreter()
    {
        return !(interpreter instanceof SkipInterpreter);
    }

    private FakeExample createFakeExampleForInterpreterRow(
            Rows theInterpreterRow)
    {
        FakeExample table = new Tables();

        table.addChild(theInterpreterRow);
        table.addChild(new Rows());
        table.addChild(new Rows());

        return table;
    }
}
