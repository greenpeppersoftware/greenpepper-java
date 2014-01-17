/*
 * Copyright (c) 2006 Pyxis Technologies inc.
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
package com.greenpepper.interpreter;

import com.greenpepper.Example;
import com.greenpepper.GreenPepper;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;

/**
 * <code>Interpreter</code> implementation that pushes imports into the
 * context import stack.
 */
public class ImportInterpreter extends AbstractInterpreter
{
    private final SystemUnderDevelopment systemUnderDevelopment;

    public ImportInterpreter( SystemUnderDevelopment systemUnderDevelopment )
    {
        this.systemUnderDevelopment = systemUnderDevelopment;
    }

    public void interpret( Specification specification )
    {
        Example table = specification.nextExample();
        for (Example row = table.at( 0, 1 ); row != null; row = row.nextSibling())
        {
            if (!row.hasChild()) continue;
            String packageName = row.firstChild().getContent();
            systemUnderDevelopment.addImport(packageName);
            GreenPepper.addImport( packageName );
        }
        specification.exampleDone( new Statistics() );
    }
}