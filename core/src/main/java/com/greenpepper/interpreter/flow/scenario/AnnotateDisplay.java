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

import com.greenpepper.Example;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.call.Result;
import com.greenpepper.call.Stub;

class AnnotateDisplay implements Stub
{
    private final Example example;

    AnnotateDisplay(Example example)
    {
        this.example = example;
    }

    public void call(Result result)
    {
        example.lastSibling().addSibling().annotate( Annotations.ignored( result.getActual() ) );
    }
}