package com.greenpepper.extensions.selenium;

import com.greenpepper.Call;
import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.call.Annotate;
import com.greenpepper.call.ResultIs;
import com.greenpepper.interpreter.AbstractInterpreter;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;
import com.greenpepper.util.ExampleUtil;

public class SeleniumIDETableInterpreter extends AbstractInterpreter
{

    // DEBT :  An index card was made.
    // this code was not tested. Is was transfert and made to compile.
    // We need a SeleniumIDETableInterpreterTest.


    private final Fixture fixture;
    private Statistics stats;

    public SeleniumIDETableInterpreter(Fixture fixture )
    {
        this.fixture = fixture;
    }

    public void interpret( Specification specification )
    {
        stats = new Statistics();
        Example table = specification.nextExample();

        for (Example row = table.at( 0, 1 ); row != null; row = row.nextSibling())
        {
            doRow( row );
        }

        specification.exampleDone( stats );
    }

    private void doRow(Example row)
    {
        if (!row.hasChild()) return;

        SeleniumAction action = new SeleniumAction(row);
        action.executeOn( fixture );
    }

    public static class SeleniumAction
    {
        private Example row;

        public SeleniumAction( Example row )
        {
            this.row = row;
        }

        public void executeOn( Fixture fixture )
        {
            try
            {
                Call call = doOn( fixture );

//                call.will( tallyStatistics( table ) );
//                call.will( Annotate.right( Group.composedOf( action.keywordCells() ) ) ).when( ResultIs.equalTo( true ) );
//                call.will( Annotate.wrong( Group.composedOf( action.keywordCells() ) ) ).when( ResultIs.equalTo( false ) );
                call.will( Annotate.exception( row.firstChild() ) ).when( ResultIs.exception() );
                call.execute();
            }
            catch (Exception e)
            {
                row.firstChild().annotate( Annotations.exception( e ) );
//                reportException( table );
            }
        }

        public Call doOn( Fixture fixture ) throws Exception
        {
            return call( fixture.send( actionName() ) );
        }

        private String actionName()
        {
            return ExampleUtil.contentOf(row.firstChild());
        }

        private Call call( Message message ) throws NoSuchMessageException
        {
            Call call = new Call( message );
            call.addInput( arguments() );
            return call;
        }

        private String[] arguments()
        {
            return ExampleUtil.content(row.at(0, 1));
        }
    }

}
