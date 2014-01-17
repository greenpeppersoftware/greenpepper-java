package com.greenpepper.interpreter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import static com.greenpepper.Assertions.assertAnnotatedException;
import static com.greenpepper.Assertions.assertAnnotatedRight;
import static com.greenpepper.Assertions.assertAnnotatedWrong;
import static com.greenpepper.Assertions.assertNotAnnotated;
import com.greenpepper.Specification;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.Tables;
import com.greenpepper.util.TestCase;

public class ActionInterpreterTest extends TestCase {

	private ActionInterpreter interpreter;
	private Mockery context = new JUnit4Mockery();
	private Target fixture;

	@Override
	protected void setUp() throws Exception {
        fixture = context.mock( Target.class );
        interpreter = new ActionInterpreter( new PlainOldFixture(fixture));
	}

	@Override
	public void tearDown()
	{
		context.assertIsSatisfied();
	}
	
	public void testThatDefinesASequenceOfActions(){
		context.checking(new Expectations()
		{{
			exactly(2).of(fixture).performAction();
		}});
		interpreter.interpret(specificationFor(
        		"[action][mock]\n" +
		        "[performAction]\n" +
		        "[performAction]"));
	}
	
	public void testCanAddOneArgumentToAnAction(){
		context.checking(new Expectations()
		{{
			one(fixture).performActionWithArgument("argument1");
		}});
		interpreter.interpret(specificationFor(
        		"[action][mock]\n" +
		        "[performActionWithArgument][argument1]"));
	}
	
	public void testCanAddMultipleArgumentsToAnAction(){
		context.checking(new Expectations()
		{{
			one(fixture).performActionWithArguments("argument1", "argument2");
		}});
		interpreter.interpret(specificationFor(
        		"[action][mock]\n" +
		        "[performActionWithArguments][argument1][argument2]"));		
	}
	
	public void testThatNoAnnotationIsAppliedIfMethodExecutesNormallyAndDoesNotReturnABoolean(){
		context.checking(new Expectations()
		{{
			one(fixture).performAction();
		}});
		Tables tables = tablesFor(
				"[action][mock]\n" +
		        "[performAction]");
		interpreter.interpret(specificationFor(tables));
        assertNotAnnotated( tables.at( 0, 1, 0 ) );
	}

	public void testThatFirstCellIsAnnotatedRightIfMethodExecutesNormallyAndReturnsTrue(){
		context.checking(new Expectations()
		{{
			one(fixture).performCheck("argument1");
			will(returnValue(true));
		}});
		Tables tables = tablesFor(
				"[action][mock]\n" +
		        "[performCheck][argument1]");
		interpreter.interpret(specificationFor(tables));
		assertAnnotatedRight( tables.at( 0, 1, 0) );
        assertNotAnnotated( tables.at( 0, 1, 1 ) );
	}

	public void testThatFirstCellIsAnnotatedWrongIfMethodExecutesNormallyAndReturnsFalse(){
		context.checking(new Expectations()
		{{
			one(fixture).performCheck("argument1");
			will(returnValue(false));
		}});
		Tables tables = tablesFor(
				"[action][mock]\n" +
		        "[performCheck][argument1]");
		interpreter.interpret(specificationFor(tables));
		assertAnnotatedWrong( tables.at( 0, 1, 0) );
        assertNotAnnotated( tables.at( 0, 1, 1 ) );
	}

	public void testThatFirstCellIsAnnotatedExceptionIfMethodThrowsAnException(){
		context.checking(new Expectations()
		{{
			one(fixture).performCheck("argument1");
			will(throwException(new IllegalArgumentException()));
		}});
		Tables tables = tablesFor(
				"[action][mock]\n" +
		        "[performCheck][argument1]");
		interpreter.interpret(specificationFor(tables));
		assertAnnotatedException( tables.at( 0, 1, 0) );
        assertNotAnnotated( tables.at( 0, 1, 1 ) );
	}

	public void testThatFirstCellIsAnnotatedWithAnExceptionIfMethodSpecifiedIsNotFound(){
		Tables tables = tablesFor(
				"[action][mock]\n" +
		        "[unknownMethod]");
		interpreter.interpret(specificationFor(tables));
		assertAnnotatedException( tables.at( 0, 1, 0) );
	}

	private Specification specificationFor(String examples) {
		return specificationFor( tablesFor(examples) );
	}

	private Specification specificationFor(Tables tables) {
		return new FakeSpecification( tables );
	}

	private Tables tablesFor(String examples) {
		return Tables.parse(examples);
	}
	
    private static interface Target
    {
    	public void performAction();
    	
    	public void performActionWithArgument(String arg);

    	public void performActionWithArguments(String arg1, String arg2);

    	public boolean performCheck(String arg);
    }	
	
}
