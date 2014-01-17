package com.greenpepper.document;

import static org.hamcrest.Matchers.is;

import com.greenpepper.Assertions;
import com.greenpepper.GreenPepper;
import com.greenpepper.Interpreter;
import com.greenpepper.Specification;
import com.greenpepper.interpreter.SkipInterpreter;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.Tables;
import static com.greenpepper.util.Tables.parse;
import com.greenpepper.util.TestCase;

public class GreenPepperInterpreterSelectorTest extends TestCase {

    private InterpreterSelector selector;
    
    protected void setUp()
    {
        SystemUnderDevelopment sud = new DefaultSystemUnderDevelopment();
        sud.addImport("com.greenpepper.document");
        GreenPepper.aliasInterpreter("MyInterpreter", MyInterpreter.class);
        selector = new GreenPepperInterpreterSelector(sud);
    }
    
    public void testReadsInterpreterClassNamesFromFirstCellOfRow()
    {
        Tables table = parse("[MyInterpreter]");
        MyInterpreter interpreter = (MyInterpreter)selector.selectInterpreter(table);
        
        assertNull(interpreter.getFixture());
    }

    public void testSecondCellCanIndicateTheFixtureToUse()
    {
        Tables table = parse("[MyInterpreter][GreenPepperInterpreterSelectorTest$MyFixture]");
        MyInterpreter interpreter = (MyInterpreter)selector.selectInterpreter(table);
        MyFixture myFixture = (MyFixture) interpreter.getFixture().getTarget();
        
        assertEquals(0, myFixture.parameterCount());
    }
    
    public void testFixturesCanHaveParametersInEveryOtherCell()
    {
        Tables table = parse("[MyInterpreter][GreenPepperInterpreterSelectorTest$MyFixture][ignored][1st parameter][ignored][2nd parameter]");
        MyInterpreter interpreter = (MyInterpreter)selector.selectInterpreter(table);
        MyFixture myFixture = (MyFixture) interpreter.getFixture().getTarget();
        assertEquals(2, myFixture.parameterCount());
    }

    public void testSkipsTableWhenInterpreterCannotBeFound()
    {
        Tables table = parse("[MissingInterpreter]");
        Interpreter interpreter = selector.selectInterpreter( table );
        assertThat( interpreter, is( SkipInterpreter.class ));
        Assertions.assertAnnotatedException( table.at(0, 0, 0 ) );
    }

    public static class MyFixture {
        public String[] parameters;

        public MyFixture() {
            this(new String[0]);
        }

        public MyFixture(String first, String second)
        {
            this(new String[] { first, second });
        }

        public MyFixture(String... parameters) {
            this.parameters = parameters;
        }

        public int parameterCount() {
            return parameters.length;
        }

    }

    public static class MyInterpreter implements Interpreter
    {
        private Fixture fixture;
        
        public MyInterpreter( SystemUnderDevelopment systemUnderDevelopment )
        {
        }
        
        public MyInterpreter(Fixture fixture)
        {
            this.fixture = fixture;   
        }
        
        public Fixture getFixture()
        {
            return fixture;
        }

        public void interpret(Specification specification)
        {
            
        }
    }

}
