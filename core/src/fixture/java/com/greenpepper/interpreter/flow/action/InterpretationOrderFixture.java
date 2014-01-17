/*
 * Copyright (c) 2007 Pyxis Technologies inc.
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

package com.greenpepper.interpreter.flow.action;

import java.util.ArrayList;
import java.util.List;

import com.greenpepper.Interpreter;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.interpreter.ActionInterpreter;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.Tables;

public class InterpretationOrderFixture
{
    public Tables tables;

    public String[] orderOfInterpretation()
    {
        FlowTracer tracer = new FlowTracer();
        Interpreter actionInterpreter = new ActionInterpreter(new PlainOldFixture(tracer));

        actionInterpreter.interpret(new FakeSpecification( tables ));

        return tracer.trace.toArray(new String[tracer.trace.size()]);
    }

    public static class FlowTracer
    {
        public final List<String> trace = new ArrayList<String>();

        public void perform(String action)
        {
            trace.add(action);
        }
    }
}
