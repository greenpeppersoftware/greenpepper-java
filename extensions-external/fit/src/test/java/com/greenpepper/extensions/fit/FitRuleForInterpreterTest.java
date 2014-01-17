package com.greenpepper.extensions.fit;

import com.greenpepper.Example;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.Tables;
import fit.ColumnFixture;
import junit.framework.TestCase;

public class FitRuleForInterpreterTest extends TestCase 
{	
	public void testCellIsAnnotatedIfAnExcpetionOccuresInTheExcuteOrResetMethod()
	{
		FakeSpecification spec = new FakeSpecification( executeTable());
		FitRuleForInterpreter interpreter = new FitRuleForInterpreter(new PlainOldFixture(new MyExecuteFixture()));
		interpreter.interpret(spec);

		assertEquals(0, spec.stats.wrongCount());
		assertEquals(0, spec.stats.rightCount());
		assertEquals(0, spec.stats.ignoredCount());
		assertEquals(1, spec.stats.exceptionCount());

		spec = new FakeSpecification( resetTable());
		interpreter = new FitRuleForInterpreter(new PlainOldFixture(new MyResetFixture()));
		interpreter.interpret(spec);
		
		assertEquals(0, spec.stats.wrongCount());
		assertEquals(0, spec.stats.rightCount());
		assertEquals(0, spec.stats.ignoredCount());
		assertEquals(1, spec.stats.exceptionCount());
	}
	
	private Example resetTable()
	{
		return Tables.parse(
				"["+ MyResetFixture.class.getName() +"]\n" +
				"[a]	[x()]\n" +
				"[1]	[1]\n" +
				"****\n"
				);
	}
	
	private Example executeTable()
	{
		return Tables.parse(
				"["+ MyExecuteFixture.class.getName() +"]\n" +
				"[a]	[x()]\n" +
				"[1]	[1]\n" +
				"****\n"
				);
	}
	
	public static class MyResetFixture extends ColumnFixture
	{
		public String a;
		
		public String x(){return "";}
		
		public void reset() throws Exception
		{
			throw new Exception();
		}
	}
	
	public static class MyExecuteFixture extends ColumnFixture
	{
		public String a;
		
		public String x(){return "";}
		
		public void execute() throws Exception
		{
			throw new Exception();
		}
	}
}
