/**
 * Copyright (c) 2009 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.greenpepper.interpreter.flow.scenario;

import com.greenpepper.Annotatable;
import com.greenpepper.Statistics;
import com.greenpepper.Text;
import com.greenpepper.annotation.Annotation;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.annotation.Colors;
import com.greenpepper.annotation.Styles;
import com.greenpepper.call.Result;
import com.greenpepper.call.Stub;

import java.util.regex.MatchResult;

class AnnotateThen implements Stub
{
    private final Annotatable annotable;
    private final ScenarioMessage message;
    private final Statistics statistics;

    AnnotateThen(Annotatable annotable, ScenarioMessage message, Statistics stats)
    {
        this.annotable = annotable;
        this.message = message;
        this.statistics = stats;
    }

    public void call(Result result)
    {
        if (result.isException())
        {
            statistics.exception();
            annotable.annotate( Annotations.exception( result.getException() ) );
            return;
        }
    	
        annotable.annotate( new Annotation()
        {

            public void writeDown(Text text)
            {
                StringBuilder content = new StringBuilder( text.getContent() );
                Object[] arguments = message.arguments();
                MatchResult matchResult = message.matchResult();

                // Backwards since we are replacing text with real positions
                for (int index = arguments.length - 1; index >= 0; index--)
                {
                    int start = matchResult.start( index + 1 );
                    int end = matchResult.end( index + 1 );

                    if (arguments[index] instanceof Expectation)
                    {
                        Expectation expectation = (Expectation) arguments[index];

                        if (expectation.meets())
                        {
                            String span = String.format( "<span style='%s: %s;'>%s</span>", Styles.BACKGROUND_COLOR, Colors.GREEN, expectation.getExpected() );
                            content.replace( start, end, span );
                            statistics.right();
                        }
                        else
                        {
                            String span = String.format( "<span style='%s: %s;'>%s</span>", Styles.BACKGROUND_COLOR, Colors.RED, expectation.getDescribe() );
                            content.replace( start, end, span );
                            statistics.wrong();
                        }
                    }
                }

                text.setContent( content.toString() );
            }
        } );
    }
}