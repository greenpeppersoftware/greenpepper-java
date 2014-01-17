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

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import com.greenpepper.phpsud.helper.Helper;

/**
 * @author Bertrand Paquet
 */
public class ObjectParserTest extends AbstractPhpFileTest {
	
	public ObjectParserTest() {
		super("MyClass.php");
	}
	
	@Test
	public void testString() throws Exception {
		container.run("$s = \"myString\"");
		Object o = parser.parse("$s");
		checkObject("myString", o, String.class);
	}

	@Test
	public void testBooleanTrue() throws Exception {
		container.run("$s = true");
		Object o = parser.parse("$s");
		checkObject(true, o, Boolean.class);
	}
	
	@Test
	public void testBooleanFalse() throws Exception {
		container.run("$s = false");
		Object o = parser.parse("$s");
		checkObject(false, o, Boolean.class);
	}
	
	@Test
	public void testInteger() throws Exception {
		container.run("$s = 12");
		Object s = parser.parse("$s");
		Assert.assertEquals("java.lang.Integer", s.getClass().getCanonicalName());
		Assert.assertEquals(12, s);
	}
	
	@Test
	public void testDouble() throws Exception {
		container.run("$s = 1.552");
		Object o = parser.parse("$s");
		checkObject(1.552, o, Double.class);
	}
	
	@Test
	public void testArrayParse() throws Exception {
		String s = "a:2:{i:0;s:12:\"PHPSUD_OBJ_2\";i:1;s:12:\"PHPSUD_OBJ_3\";}";
		String [] a = Helper.parseArray(s);
		Assert.assertNotNull(a);
		Assert.assertEquals(2, a.length);
		Assert.assertEquals("java.lang.String", a[0].getClass().getCanonicalName());
		Assert.assertEquals("java.lang.String", a[1].getClass().getCanonicalName());
		Assert.assertEquals("PHPSUD_OBJ_2", a[0]);
		Assert.assertEquals("PHPSUD_OBJ_3", a[1]);
	}
	
	@Test
	public void testArray() throws Exception {
		container.run("$s = array(3, 4, \"myString\")");
		Object o = parser.parse("$s");
		Object [] a = (Object []) o;
		Assert.assertNotNull(a);
		Assert.assertTrue(a.getClass().isArray());
		Assert.assertEquals(3, a.length);
		checkObject(3, a[0], Integer.class);
		checkObject(4, a[1], Integer.class);
		checkObject("myString", a[2], String.class);
	}
	
	public Object getObject(Object o, String methodName) throws Exception {
		Method m = o.getClass().getMethod(methodName);
		Assert.assertNotNull(m);
		return m.invoke(o);
	}
	
	public void checkMyClass(Object o, String id, Integer x, String y) throws Exception {
		Assert.assertNotNull(o);
		Assert.assertNotNull(o.getClass().getConstructor(String.class));
		checkObject(id, getObject(o, "getPHPSudId"), String.class);
		checkObject(x, getObject(o, "getX"), Integer.class);
		checkObject(y, getObject(o, "getY"), String.class);
	}
	
	@Test
	public void testObject() throws Exception {
		container.run("$s = new MyClass()");
		container.run("$s->x = 12");
		container.run("$s->y = \"myString\"");
		Object o = parser.parse("$s");
		checkMyClass(o, "PHPSUD_OBJ_0", 12, "myString");
	}
	
	@Test
	public void testObjectArray() throws Exception {
		container.run("$a1 = new MyClass()");
		container.run("$a1->x = 12");
		container.run("$a1->y = \"myString\"");
		container.run("$a2 = new MyClass()");
		container.run("$a2->x = 13");
		container.run("$a2->y = \"myString2\"");
		container.run("$a = array($a1, $a2)");
		Object o = parser.parse("$a");
		Assert.assertNotNull(o);
		Assert.assertTrue(o.getClass().isArray());
		Object [] t = (Object []) o;
		Assert.assertEquals(2, t.length);
		checkMyClass(t[0], "PHPSUD_OBJ_0", 12, "myString");
		checkMyClass(t[1], "PHPSUD_OBJ_1", 13, "myString2");
	}

}
