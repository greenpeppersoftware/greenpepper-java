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
public class EqualsTest extends AbstractPhpFileTest {
	
	public EqualsTest() {
		super("Equals.php");
	}

	private PHPClassDescriptor descExplicit;
	
	private PHPClassDescriptor descImplicit;
	
	@Before
	public void setUp() throws Exception {
		descExplicit = container.getClassDescriptor("ExplicitEquals");
		descImplicit = container.getClassDescriptor("ImplicitEquals");
	}
	
	@Test
	public void testExplicit() throws Exception {
		String id1 = container.createObject(descExplicit, "a");
		String id2 = container.createObject(descExplicit, "a");
		String id3 = container.createObject(descExplicit, "b");
		Object o1 = parser.parse(PHPInterpeter.getObject(id1));
		Object o1Bis = parser.parse(PHPInterpeter.getObject(id1));
		Object o2 = parser.parse(PHPInterpeter.getObject(id2));
		Object o3 = parser.parse(PHPInterpeter.getObject(id3));
		Assert.assertTrue(o1.equals(o2));
		Assert.assertTrue(o2.equals(o1));
		Assert.assertFalse(o1.hashCode() == o2.hashCode());
		Assert.assertFalse(o1.hashCode() == o1Bis.hashCode());
		Assert.assertTrue(o1.equals(o1Bis));
		Assert.assertTrue(o1Bis.equals(o1));
		Assert.assertFalse(o1.equals(o3));
		Assert.assertFalse(o3.equals(o1));
	}
	
	@Test
	public void testImplicit() throws Exception {
		String id1 = container.createObject(descImplicit, "a");
		String id2 = container.createObject(descImplicit, "a");
		String id3 = container.createObject(descImplicit, "b");
		Object o1 = parser.parse(PHPInterpeter.getObject(id1));
		Object o1Bis = parser.parse(PHPInterpeter.getObject(id1));
		Object o2 = parser.parse(PHPInterpeter.getObject(id2));
		Object o3 = parser.parse(PHPInterpeter.getObject(id3));
		Assert.assertFalse(o1.equals(o2));
		Assert.assertFalse(o2.equals(o1));
		Assert.assertFalse(o1.hashCode() == o2.hashCode());
		Assert.assertFalse(o1.hashCode() == o1Bis.hashCode());
		Assert.assertTrue(o1.equals(o1Bis));
		Assert.assertTrue(o1Bis.equals(o1));
		Assert.assertFalse(o1.equals(o3));
		Assert.assertFalse(o3.equals(o1));
	}
	
}
