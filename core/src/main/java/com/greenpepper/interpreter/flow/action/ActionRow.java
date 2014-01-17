package com.greenpepper.interpreter.flow.action;

import java.util.List;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.Do;
import com.greenpepper.call.ResultIs;
import com.greenpepper.interpreter.flow.AbstractRow;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.util.ExampleUtil;

public class ActionRow extends AbstractRow
{
	protected ActionRow(Fixture fixture)
    {
		super(fixture);
	}

	@Override
	public List<Example> actionCells(Example row)
    {
		return ExampleUtil.asList(row.firstChild());
	}

	public void interpret(Specification spec)
    {
        Example row = spec.nextExample();
        Action action = Action.parse(actionCells(row));
        try
        {
            Call call = action.checkAgainst(fixture);
            call.will(Do.both(Annotate.right(keywordCells(row))).and(countRowOf(spec).right())).
                    when(ResultIs.equalTo(true));
            call.will(Do.both(Annotate.wrong(keywordCells(row))).and(countRowOf(spec).wrong())).
                    when(ResultIs.equalTo(false));
            call.will(Do.both(Annotate.exception(keywordCells(row))).and(countRowOf(spec).exception())).
                    when(ResultIs.exception());
            call.execute();
        }
        catch (Exception e)
        {
            keywordCells(row).annotate(Annotations.exception(e));
            reportException(spec);
        }
	}

    private Example keywordCells(Example row)
    {
        return row.firstChild();
    }
}
