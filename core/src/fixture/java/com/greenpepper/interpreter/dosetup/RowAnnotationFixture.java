package com.greenpepper.interpreter.dosetup;

import com.greenpepper.Statistics;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.interpreter.DoSetupInterpreter;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.AnnotationUtil;
import com.greenpepper.util.FakeExample;
import com.greenpepper.util.Rows;
import com.greenpepper.util.Tables;

public class RowAnnotationFixture
{
	public Rows row;
	public String enterRow;
	public String rwie;

	public void prepareRWIE(Statistics stats)
	{
		rwie = stats.rightCount() + " " + stats.wrongCount() + " " + stats.ignoredCount() + " " + stats.exceptionCount();
	}
	
	public void enterRow(String val) throws Exception
	{
		enterRow = val;
		DoSetupInterpreter setup = new DoSetupInterpreter(new PlainOldFixture(new MyFixture(enterRow)));
		FakeExample table = new Tables();

		table.addChild(new Rows());
		table.addChild(row);

		FakeSpecification fakeSpecification = new FakeSpecification(table);
		setup.interpret(fakeSpecification);

		prepareRWIE(fakeSpecification.stats());
	}

	public Integer[] cellsMarked()
	{
		return AnnotationUtil.cellsMarked(row);
	}

	public String rowMarkedAs()
	{
		int cellToWatch = row.firstChild().remainings()-1;
		return AnnotationUtil.getAnnotationOnCell(row.at(0,cellToWatch));
	}

	public static class MyFixture
	{
		private String enterRow;

		public MyFixture(String enterRow)
		{
			this.enterRow = enterRow;
		}

		public boolean rowOfTable(int index1, int index2) throws Exception
		{
			if ("exception".equals(enterRow)) throw new Exception();
			return !"failed".equals(enterRow);
		}

		public boolean rowOfTableWith(int index1, int index2, int index3) throws Exception
		{
			if ("exception".equals(enterRow)) throw new Exception();
			return !"failed".equals(enterRow);
		}

		public void existingMethodWithMissingParameter(int p1) throws Exception
		{
			
		}
	}
}