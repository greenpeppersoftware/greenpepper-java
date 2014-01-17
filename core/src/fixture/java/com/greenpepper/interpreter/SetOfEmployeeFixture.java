package com.greenpepper.interpreter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.AnnotationTable;
import com.greenpepper.util.Rows;
import com.greenpepper.util.Tables;

public class SetOfEmployeeFixture
{
	Tables tables;
	List<Employee> employees;


	public void specValues(Tables tables)
	{
		this.tables = tables;
	}

	public void sudValues(String values)
	{
		this.employees = toEmployees(values);
	}

    private List<Employee> toEmployees(String values)
    {
        ArrayList<Employee> list = new ArrayList<Employee>();
        String[] rows = values.split("\n");

        for (String row : rows)
        {
            String[] cells = row.split(",");
            list.add(new Employee(cells[0].trim(), cells[1].trim()));
        }
        return list;
    }

    public class Employee
    {
        public String first;
        public String last;
        
        public Employee(String first, String last)
        {
            this.first = first;
            this.last = last;
        }
    }

	public Collection query()
	{
		return employees;
	}

	public AnnotationTable annotations()
	{
		SetOfInterpreter setOf = new SetOfInterpreter(new PlainOldFixture(this));

		Rows rows = new Rows("first", "last");
		rows.addSibling(tables.firstChild());

		setOf.execute(rows);

		return new AnnotationTable(tables);
	}

}
