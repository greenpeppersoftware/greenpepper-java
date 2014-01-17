package com.greenpepper.interpreter;

import com.greenpepper.interpreter.column.Column;
import com.greenpepper.interpreter.column.ExpectedColumn;
import com.greenpepper.reflect.NoSuchMessageException;
import com.greenpepper.util.AnnotationUtil;
import com.greenpepper.util.Cells;

public class CellAnnotationFixture
{
	public String comparisonValue;
	public String returnedValue;
	
	public Object annotation() throws NoSuchMessageException, Exception
	{
		Cells cell = new Cells(comparisonValue);
		
		MockFixture mockFixture = new MockFixture();
		mockFixture.willRespondTo("foo", returnedValue);
		
		Column col = new ExpectedColumn(mockFixture.send("foo"));
		col.doCell(cell);
		
		return AnnotationUtil.getAnnotationOnCell(cell);
	}
	
	public void setComparisonValue(String text)
	{
		comparisonValue = text == null ? "" : text;
	}
	
	public static class MyFixture
	{
		private CellAnnotationFixture cellAnnotationFixture;
		
		public MyFixture(CellAnnotationFixture var)
		{
			cellAnnotationFixture = var;
		}
		
		public String foo(String valueToCompare) throws Exception
		{
			if ("error".equals(cellAnnotationFixture.returnedValue))
				throw new Exception();
			return cellAnnotationFixture.returnedValue;
		}
	}
}
