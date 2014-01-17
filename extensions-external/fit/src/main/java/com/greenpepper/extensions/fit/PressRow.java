package com.greenpepper.extensions.fit;

import java.util.ArrayList;
import java.util.List;

import com.greenpepper.Example;
import com.greenpepper.reflect.Fixture;

public class PressRow extends FitDefaultRow 
{
	public PressRow(Fixture fixture, Boolean timed) 
	{
		super(null, fixture, timed);
	}

	@Override
	public List<Example> actionCells(Example row) 
	{
		List<Example> pressRow = new ArrayList<Example>();
		pressRow.add(row.at(0, 1));
		return pressRow;
    }
}