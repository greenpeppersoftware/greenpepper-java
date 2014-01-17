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
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.greenpepper.phpsud.container.PHPClassDescriptor;

/**
 * @author Bertrand Paquet
 */
public class ParseTest extends AbstractPhpFileTest {
	
	public ParseTest() {
		super("Parse.php");
	}

	private PHPClassDescriptor descParser;

	@Before
	public void setUp() throws Exception {
		descParser = container.getClassDescriptor("ParseClass");
	}

	@Test
	public void testParser() throws Exception {
		Class<?> c = descParser.getGeneratedClass();
		Method m = c.getMethod("parse", String.class);
		Assert.assertNotNull(m);
		Assert.assertEquals(Modifier.STATIC + Modifier.PUBLIC, m.getModifiers());
		Object o = m.invoke(null, "MyString");
		Assert.assertNotNull(o);
		String className = o.getClass().getCanonicalName();
		Assert.assertTrue(className.startsWith("com.greenpepper.phpsud.compiler.temp.container"));
		Assert.assertTrue(className.endsWith(".ParseClass"));
		Method getA = o.getClass().getMethod("getA");
		Assert.assertNotNull(getA);
		Object res = getA.invoke(o);
		checkObject("ParseClass MyString", res, String.class);
	}
}
