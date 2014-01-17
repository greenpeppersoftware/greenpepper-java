package com.greenpepper.interpreter;

import com.greenpepper.Assertions;
import com.greenpepper.GreenPepper;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.reflect.DefaultFixture;
import com.greenpepper.reflect.EnterRow;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.Tables;
import static com.greenpepper.util.Tables.parse;
import junit.framework.TestCase;

public class SetupInterpreterTest extends TestCase {
	private Tables tables;
	private SetupInterpreter interpreter;
	private TargetFixture targetFixture;
	
	protected void setUp() throws Exception
	{
		targetFixture=new TargetFixture();
		interpreter=new SetupInterpreter(new PlainOldFixture(targetFixture));
		GreenPepper.setStopOnFirstFailure(false); // reset
	}

	protected void tearDown() throws Exception
	{
		GreenPepper.setStopOnFirstFailure(false); // reset
	}

	public void testColumnHeaderSpecifiesAnInputValue() throws Exception
	{
		tables = parse(
				"[setup][dummy Fixture]\n" +
				"[a][b]\n" +
				"[5][3]"
		);
		
		interpreter.interpret( document() );
		assertEquals( 5, targetFixture.a );
		assertEquals( 3, targetFixture.b );
	}
	
	public void testExecuteMethodIsCallAfterInput()
	{
		tables = parse(
				"[setup][dummy Fixture]\n" +
				"[a][b]\n" +
				"[5][3]\n" +
				"[2][3]"
		);
		
		interpreter.interpret( document() );
		assertEquals( 21, targetFixture.product );
	}
	
	public void testThatSpecificationIsBypassWhenNoEnterRowMethod()
	{
		tables = parse(
				"[setup][dummy Fixture]\n" +
				"[a][b]\n"
		);
		
		FakeSpecification document=document();
		SetupInterpreter interpreter2=new SetupInterpreter(new PlainOldFixture(new TargetFixtureNoEnterRow()));
		interpreter2.interpret( document );
		assertEquals(1,document.stats().exceptionCount());
		
	}
	
	public void testThatStatisticsEnterRowMethod()
	{
		tables = parse(
				"[setup][dummy Fixture]\n" +
				"[a][b]\n" +
				"[2][-1]\n" +
				"[4][-1]\n" +
				"[4][2]"
		);
		
		FakeSpecification document=document();
		interpreter.interpret( document );
		assertEquals(2,document.stats().exceptionCount());
		
	}
	
	public void testThatCellWithResultIsAdded()
	{
		tables = parse(
				"[setup][dummy Fixture]\n" +
				"[a][b]\n" +
				"[5][3]\n" +
				"[2][3]"
		);
		
		interpreter.interpret( document() );
		Assertions.assertAnnotatedEntered( tables.at(0,2,2) );
	}

	public void testThatCellWithResultIsAddedWithEnterRowAnnotation()
	{
		tables = parse(
				"[setup][dummy Fixture]\n" +
				"[a][b]\n" +
				"[5][3]\n" +
				"[2][3]"
		);

		FakeSpecification document=document();
		SetupInterpreter interpreter2 = new SetupInterpreter(new PlainOldFixture(new AnnotatedTargetFixture()));
		interpreter2.interpret( document );

		Assertions.assertAnnotatedEntered( tables.at(0,2,2) );
	}

	public void testThatHeaderContainingUnresolvableMethodIsAnnotatedStoppedWhenOptionsIsStopOnFirstFailure()
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = parse(
				"[setup][dummy Fixture]\n" +
				"[a][c]\n" +
				"[5][3]\n" +
				"[2][3]"
		);

		FakeSpecification document = document();
		interpreter.interpret( document );

		assertTrue(document.stats().hasFailed());
		Assertions.assertAnnotatedStopped( tables.at(0,1,2) );
	}

	public void testThatCellContainingBadValueIsAnnotatedStoppedWhenOptionsIsStopOnFirstFailure()
	{
		GreenPepper.setStopOnFirstFailure(true);

		tables = parse(
				"[setup][dummy Fixture]\n" +
				"[a][b]\n" +
				"[fail][3]\n" +
				"[2][3]"
		);

		FakeSpecification document = document();
		interpreter.interpret( document );

		assertTrue(document.stats().hasFailed());
		Assertions.assertAnnotatedStopped( tables.at(0,2,2) );
	}
	
     public void testResultIsMarkedWithExceptionStackWhenEnterRowThrowAnException()
     {
         GreenPepper.setStopOnFirstFailure(true);

         tables = parse(
                 "[setup][dummy Fixture]\n" +
                 "[a][b]\n" +
                 "[2][3]"
         );

         FakeSpecification document = document();

         SetupInterpreter interpreter2 = new SetupInterpreter(new DefaultFixture(new TargetFixtureWithExceptionOnEnterRow()));
         interpreter2.interpret(document);

         assertTrue(document.stats().indicatesFailure());
         Assertions.assertAnnotatedException(tables.at(0, 2, 2));
     }

	private FakeSpecification document()
	{
		return new FakeSpecification( tables );
	}
	
	public class TargetFixtureWithExceptionOnEnterRow
    {
        public int a;
        public int b;

        public void enterRow() throws Exception
        {
            throw new Exception();
        }
    }
	public static class TargetFixtureNoEnterRow
	{
		public int a;
		public int b;
	}
	
	public static class TargetFixture
	{
		public int a;
		public int b;
		public int product = 0;
		
		public void enterRow() throws Exception
		{
			if( b < 0) throw new Exception();
			product += a*b;
		}
	}

	public static class AnnotatedTargetFixture
	{
		public int a;
		public int b;
		public int product = 0;

		@EnterRow
		public void doTheDew() throws Exception
		{
			if( b < 0) throw new Exception();
			product += a*b;
		}
	}
}
