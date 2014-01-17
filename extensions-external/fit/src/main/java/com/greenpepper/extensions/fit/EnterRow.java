package com.greenpepper.extensions.fit;

import java.util.ArrayList;
import java.util.List;

import com.greenpepper.Example;
import com.greenpepper.reflect.Fixture;

public class EnterRow extends FitDefaultRow 
{

	public EnterRow(Fixture fixture, Boolean timed) 
	{
		super(null, fixture, timed);
	}
	
	

	@Override
	public List<Example> actionCells(Example row) 
	{
		List<Example> enterRow = new ArrayList<Example>();
		enterRow.add(row.at(0, 1));
		enterRow.add(row.at(0, 2));
		return enterRow;
    }
}
