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

import com.greenpepper.GreenPepper;
import static com.greenpepper.GreenPepper.canContinue;
import static com.greenpepper.GreenPepper.setStopOnFirstFailure;
import static com.greenpepper.GreenPepper.shouldStop;
import com.greenpepper.Statistics;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.util.Tables;
import static com.greenpepper.util.Tables.parse;
import com.greenpepper.util.TestCase;

public class OptionsInterpreterTest
		extends TestCase
{

	private Tables tables;
	private OptionsInterpreter interpreter;

	protected void setUp()
			throws Exception
	{
		interpreter = new OptionsInterpreter(null);
		setStopOnFirstFailure(false); // reset
	}

	protected void tearDown() throws Exception
	{
		setStopOnFirstFailure(false); // reset
	}

	public void testSettingStopOnFirstFailureOption()
			throws Exception
	{
		tables = parse(
				"Options\n" +
				"[stop on first failure][true]"
		);

		interpreter.interpret(document());

		assertTrue(GreenPepper.isStopOnFirstFailure());
	}

	public void testShouldStopWhenExceptionFoundInStats()
	{
		assertFalse(shouldStop( new Statistics(0, 0, 0, 0)));
		assertFalse(shouldStop( new Statistics(0, 0, 1, 0)));

		assertTrue(canContinue( new Statistics(0, 0, 0, 0)));
		assertTrue(canContinue( new Statistics(0, 0, 1, 0)));

		setStopOnFirstFailure(true);

		assertFalse(shouldStop( new Statistics(0, 0, 0, 0)));
		assertTrue(shouldStop( new Statistics(0, 0, 1, 0)));

		assertTrue(canContinue( new Statistics(0, 0, 0, 0)));
		assertFalse(canContinue( new Statistics(0, 0, 1, 0)));
	}

	private FakeSpecification document()
	{
		return new FakeSpecification( tables );
	}
}