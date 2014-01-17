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
package com.greenpepper.phpsud.container;

import org.junit.Assert;
import org.junit.Test;

import com.greenpepper.phpsud.phpDriver.PHPInterpeter;

/**
 * @author Bertrand Paquet
 */
public class PHPReturnObjectTest extends AbstractPHPContainerTest {

	public PHPReturnObjectTest() {
		super("src/test/resources", "MyClass.php");
	}

	@Test
	public void testReturnObject() throws Exception {
		container.run("$o1 = new MyClass()");
		container.run("$o1->x = 2");
		String id1 = container.get(PHPInterpeter.saveObject("$o1"));
		Assert.assertEquals("PHPSUD_OBJ_0", id1);

		container.run("$o2 = new MyClass()");
		container.run("$o2->x = 3");
		String id2 = container.get(PHPInterpeter.saveObject("$o2"));
		Assert.assertEquals("PHPSUD_OBJ_1", id2);
		
		Assert.assertEquals("2", container.get(PHPInterpeter.getObject(id1) + "->x"));
		Assert.assertEquals("3", container.get(PHPInterpeter.getObject(id2) + "->x"));
		Assert.assertEquals("2", container.get(PHPInterpeter.getObject(id1) + "->x"));
		
		container.run("$a = array()");
		container.run("array_push($a, " + PHPInterpeter.getObject(id1) + ")");
		container.run("array_push($a, " + PHPInterpeter.getObject(id2) + ")");
		
		container.run("$b = " + PHPInterpeter.getArray("$a"));
		
		Assert.assertEquals("2", container.get("count($b)"));
		Assert.assertEquals("PHPSUD_OBJ_2", container.get("$b[0]"));
		Assert.assertEquals("PHPSUD_OBJ_3", container.get("$b[1]"));
	}
	
}
