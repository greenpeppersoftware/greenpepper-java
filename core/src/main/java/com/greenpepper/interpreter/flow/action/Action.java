package com.greenpepper.interpreter.flow.action;

import java.util.ArrayList;
import java.util.List;

import com.greenpepper.Example;
import com.greenpepper.interpreter.flow.AbstractAction;
import com.greenpepper.util.ExampleUtil;

public class Action extends AbstractAction
{
    public Action(Iterable<String> cells)
    {
		super(cells);
	}

	public static Action parse(List<Example> cells)
    {
        return new Action( ExampleUtil.contentAsList(cells) );
	}

	protected List<String> keywords()
    {
        return cellsAsList().subList(0, 1);
    }

    protected List<String> parameters()
    {   
        List<String> cellsAsList = cellsAsList();
		return cellsAsList.subList(1, cellsAsList.size());
    }

	private List<String> cellsAsList()
    {
		List<String> parameters = new ArrayList<String>();
    	for (String cell : getCells()) {
			parameters.add(cell);
		}
		return parameters;
	}
}
