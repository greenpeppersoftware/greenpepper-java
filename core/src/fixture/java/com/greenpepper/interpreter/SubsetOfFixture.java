package com.greenpepper.interpreter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.AnnotationTable;
import com.greenpepper.util.Rows;
import com.greenpepper.util.Tables;

public class SubsetOfFixture
{
	Tables tables;
	List<String> values;

	public void specValues(Tables tables)
	{
		this.tables = tables;
	}

	public void sudValues(String [] values)
	{
		this.values = Arrays.asList(values);
	}

	public Collection query()
	{
		return values;
	}


	public AnnotationTable annotations()
	{
		SubsetOfInterpreter subsetOf = new SubsetOfInterpreter(new PlainOldFixture(this));

		Rows rows = new Rows("to string");
		rows.addSibling(tables.firstChild());

		subsetOf.execute(rows);

		return new AnnotationTable(tables);
	}

}
