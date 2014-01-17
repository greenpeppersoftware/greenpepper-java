/*
 * Copyright (c) 2008 Pyxis Technologies inc.
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
 *
 * IMPORTANT NOTE :
 * Kindly contributed by Bertrand Paquet from Octo Technology (http://www.octo.com)
 */
package com.greenpepper.phpsud.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.greenpepper.phpsud.container.PHPClassDescriptor;
import com.greenpepper.phpsud.phpDriver.PHPInterpeter;

/**
 * @author Bertrand Paquet
 */
public class ToStringTest extends AbstractPhpFileTest {
	
	public ToStringTest() {
		super("ToString.php");
	}

	private PHPClassDescriptor descNoToString;
	
	private PHPClassDescriptor descToString;
	
	@Before
	public void setUp() throws Exception {
		descNoToString = container.getClassDescriptor("NoToString");
		descToString = container.getClassDescriptor("WithToString");
	}
	
	@Test
	public void testWithToString() throws Exception {
		String id1 = container.createObject(descToString, "a");
		Object o1 = container.getObjectParser().parse(PHPInterpeter.getObject(id1));
		String res = o1.toString();
		Assert.assertEquals("ToString Function a", res);
	}
	
	@Test
	public void testWithNoToString() throws Exception {
		String id1 = container.createObject(descNoToString, "a");
		Object o1 = parser.parse(PHPInterpeter.getObject(id1));
		String res = o1.toString();
		Assert.assertTrue(res.startsWith(o1.getClass().getCanonicalName()));
	}
	
}
