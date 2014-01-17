package com.greenpepper.extensions.fit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.ResultIs;
import com.greenpepper.interpreter.flow.dowith.Action;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.util.CollectionUtil;

public class CheckRow extends com.greenpepper.interpreter.flow.dowith.CheckRow
{
	private boolean timed;
	
	public CheckRow(Fixture fixture, Boolean timed)
	{
		super(fixture);
		this.timed = timed;
	}

    public void interpret( Specification table )
    {
        Example row = table.nextExample();

        Example expectedCell = row.at(0, 2);
        Action action = Action.parse( actionCells(row));
        Date start = new Date();

        try
        {
            Call call = action.checkAgainst( fixture );
            call.expect( expectedCell.getContent() );
            call.will( Annotate.right( expectedCell ) ).when( ResultIs.right() );
            call.will( Annotate.wrongWithDetails( expectedCell ) ).when( ResultIs.wrong() );
            call.will( Annotate.exception( CollectionUtil.first( keywordCells(row) ) ) ).when( ResultIs.exception() );
            call.will( tallyStatistics( table ) );            
            call.execute();
        }
        catch (Exception e)
        {
            reportException( table );
            CollectionUtil.first( keywordCells(row) ).annotate( Annotations.exception( e ) );
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
		List<Example> checkRow = new ArrayList<Example>();
		checkRow.add(row.at(0, 1));
		return checkRow;
	}

    private List<Example> keywordCells(Example row) 
    {
        return CollectionUtil.odd( row.firstChild() );
    }
}
