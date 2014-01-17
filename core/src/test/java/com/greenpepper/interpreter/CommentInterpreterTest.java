package com.greenpepper.interpreter;

import com.greenpepper.AlternateCalculator;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.util.Tables;

public class CommentInterpreterTest extends junit.framework.TestCase
{
    private CommentInterpreter interpreter;

    public void setUp()
    {
        interpreter = new CommentInterpreter();
    }

    public void testSkipsTable()
    {
        Tables tables = Tables.parse(
            "[" + CommentInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
            "[a][b][sum?]\n" +
            "[6][2][8]\n" +
            "[5][2][8]\n"
        );
        FakeSpecification spec = new FakeSpecification( tables );
        interpreter.interpret( spec );

        assertEquals( 1, spec.stats.ignoredCount() );
    }
}
