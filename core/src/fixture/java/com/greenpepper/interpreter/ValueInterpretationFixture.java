package com.greenpepper.interpreter;

import com.greenpepper.interpreter.column.Column;
import com.greenpepper.interpreter.column.ExpectedColumn;
import com.greenpepper.interpreter.column.GivenColumn;
import com.greenpepper.interpreter.column.NullColumn;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.NoSuchMessageException;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.AnnotationUtil;
import com.greenpepper.util.Cells;

public class ValueInterpretationFixture
{
	private String cellText;
	public String recognizedAs;
	public String resultingValue;
	public boolean ruleRespected;
	public String inputCellRecognizedAs;

	public String comparisonWillBe() throws Exception
	{
		Cells cell = new Cells(cellText);
		
		MockFixture mockFixture = new MockFixture();
		mockFixture.willRespondTo("foo", "anything");
		
		Column col = new ExpectedColumn(mockFixture.send("foo"));
		col.doCell(cell);
		
		return AnnotationUtil.getAnnotationOnCell(cell);
	}
	
	public String inputWillBe() throws Exception
	{
		Fixture fixture = new PlainOldFixture(new MyFixture(this));
		Column column = new GivenColumn(fixture.send("foo"));
		column.doCell(new Cells(cellText));

		return inputCellRecognizedAs;
	}

	 public static class MyFixture
	 {
		ValueInterpretationFixture valueInterpretationFixture;

    	public MyFixture(ValueInterpretationFixture var) {
			valueInterpretationFixture = var;
		}

		public void foo(String val)
    	{
    		valueInterpretationFixture.inputCellRecognizedAs = val;
    	}
	 }

	public void setCellText(String text)
    {
    	if (text == null)
    	{
    		cellText = "";
    	}
    	else
    	{
    		cellText = text;
    	}
    }

	public String getCellText()
	{
		return cellText;
	}

	public void setRecognizedAs(String recognizedAs) throws NoSuchMessageException, Exception
    {
		Fixture fixture;
		Column column = new NullColumn();
		ruleRespected=false;
		resultingValue="";

		fixture = fixtureFor(recognizedAs);
		column = new GivenColumn(fixture.send("foo"));
		column.doCell(new Cells(cellText));
	}

	private Fixture fixtureFor(String recognizedAs)
    {
		if ("a number".equals(recognizedAs))
		{
			return new PlainOldFixture(new MyFixtureFloat(this));
		}

		if ("a string".equals(recognizedAs))
		{
			return new PlainOldFixture(new MyFixtureString(this));
		}

		if ("a boolean".equals(recognizedAs))
		{
			return new PlainOldFixture(new MyFixtureBoolean(this));
		}
		return null;
	}

	public static class MyFixtureFloat
	{
		ValueInterpretationFixture interpretationFixture;

		public MyFixtureFloat(ValueInterpretationFixture val)
		{
			interpretationFixture=val;
		}

		public void foo(Float var)
		{
			interpretationFixture.ruleRespected=true;
			interpretationFixture.resultingValue=var.toString();
		}
	}

	public static class MyFixtureString
	{
		ValueInterpretationFixture interpretationFixture;

		public MyFixtureString(ValueInterpretationFixture val)
		{
			interpretationFixture=val;
		}

		public void foo(String var)
		{
			interpretationFixture.ruleRespected=true;
			interpretationFixture.resultingValue=var;
		}
	}

	public static class MyFixtureBoolean
	{
		ValueInterpretationFixture interpretationFixture;

		public MyFixtureBoolean(ValueInterpretationFixture val)
		{
			interpretationFixture=val;
		}

		public void foo(Boolean var)
		{
			interpretationFixture.ruleRespected=true;
			interpretationFixture.resultingValue=var.toString();
		}
	}




}
