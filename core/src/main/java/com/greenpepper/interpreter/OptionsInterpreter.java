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
package com.greenpepper.interpreter;

import com.greenpepper.Example;
import com.greenpepper.GreenPepper;
import com.greenpepper.Specification;
import com.greenpepper.Statistics;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;

public class OptionsInterpreter
		extends AbstractInterpreter
{
	public OptionsInterpreter(SystemUnderDevelopment sud)
	{

	}

	public void interpret(Specification specification)
	{
		Example table = specification.nextExample();

		for (Example row = table.at(0, 1); row != null; row = row.nextSibling())
		{
			if (!row.hasChild())
			{
				continue;
			}

			Example example = row.firstChild();
			String property = example.getContent();

			if ("stop on first failure".equals(property))
			{
				String value = example.nextSibling().getContent();
				GreenPepper.setStopOnFirstFailure( Boolean.parseBoolean(value) );
				example.lastSibling().addSibling().annotate(Annotations.entered());
			}
		}

		specification.exampleDone(new Statistics());
	}
}
