package com.greenpepper.extensions.fit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.Do;
import com.greenpepper.call.ResultIs;
import com.greenpepper.interpreter.flow.AbstractRow;
import com.greenpepper.interpreter.flow.dowith.Action;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.Group;

public class FitDefaultRow extends AbstractRow 
{
	private Fixture fitFixture;
	private boolean timed;
	
	public FitDefaultRow(Fixture fitFixture, Fixture fixture, Boolean timed) 
	{
		super(fixture);
		this.fitFixture = fitFixture;
		this.timed = timed;
	}

    public void interpret(Specification table) 
    {
        Example row = table.nextExample();
        Action action = Action.parse(actionCells(row));
        Date start = new Date();
        try 
        {
            Call call = action.checkAgainst(fitFixture != null ? fitFixture : fixture);
            call.will(Do.both(Annotate.right(Group.composedOf(keywordCells(row)))).and(countRowOf(table).right())).when(ResultIs.equalTo(true));
            call.will(Do.both(Annotate.wrong(Group.composedOf(keywordCells(row)))).and(countRowOf(table).wrong())).when(ResultIs.equalTo(false));
            call.will(Do.both(Annotate.exception(CollectionUtil.first(keywordCells(row)))).and(countRowOf(table).exception())).when(ResultIs.exception());
            call.execute();
        }
        catch (Exception e)
        {
            CollectionUtil.first(keywordCells(row)).annotate(Annotations.exception(e));
            reportException(table);
        }
        finally
        {
            if(timed)
            {
                long split = new Date().getTime() - start.getTime();
            	row.addChild().setContent(Fit.format.format(start));
            	row.addChild().setContent(split<1000 ? "&nbsp;" : Double.toString((split)/1000.0));
            }
        }
    }

	public List<Example> actionCells(Example row) 
	{
		List<Example> defaultRow = new ArrayList<Example>();
		defaultRow.add(row.firstChild());
		return defaultRow;
    }

    private List<Example> keywordCells(Example row)
    {
        return CollectionUtil.even(row.firstChild());
    }
}