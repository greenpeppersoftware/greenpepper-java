package com.greenpepper.reflect;

import static org.hamcrest.Matchers.is;

import com.greenpepper.Interpreter;
import com.greenpepper.Specification;
import com.greenpepper.util.TestCase;

public class TypeTest extends TestCase
{
    private Type<Interpreter> type;

    protected void setUp() {
        type = new Type<Interpreter>( TypeTest.MyInterpreter.class );
    }

    public void testInstanciatesObjectsFromTheirClassNames() throws Throwable {
        Interpreter interpreter = type.newInstanceUsingCoercion();
        assertThat(interpreter, is(TypeTest.MyInterpreter.class));
    }

    public void testWillLookForConstructorThatBestMatchesTypeParameters() throws Throwable {
        TypeTest.MyInterpreter interpreter = (TypeTest.MyInterpreter) type.newInstance( new TypeTest.MyFixture() );
        assertThat(interpreter.fixture, is( TypeTest.MyFixture.class ) );
    }
    
// TO DO WORKS ON JAVA 1.6 but doesnt seem to WORK ON 1.5
//    public void testConstructorCanHavePrimitiveTypes() throws Throwable {
//        TypeTest.MyInterpreter interpreter = (TypeTest.MyInterpreter) type.newInstance( (int)5 );
//        assertEquals(5, interpreter.i );
//    }

    public void testParametersWillBeConvertedToConstructorTypesWhenUsingStringForm() throws Throwable {
        TypeTest.MyInterpreter interpreter = (TypeTest.MyInterpreter) type.newInstanceUsingCoercion( "a string", "5" );
        assertEquals("a string", interpreter.s );
        assertEquals(5, interpreter.i );
    }

    public static class MyFixture {}

    public static class MyInterpreter implements Interpreter {

        public Object fixture;
        public String s;
        public int i;

        public MyInterpreter() {
        }

        public MyInterpreter(int val) {
            i = val;
        }

        public MyInterpreter(String s, Integer i) {
            this.s = s;
            this.i = i;
        }

        public MyInterpreter(Object fixture) {
            this.fixture = fixture;
        }

        public void interpret(Specification specification) {
        }

    }
}
