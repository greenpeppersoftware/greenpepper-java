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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.greenpepper.phpsud.container.PHPClassDescriptor;
import com.greenpepper.phpsud.phpDriver.PHPInterpeter;

/**
 * @author Bertrand Paquet
 */
public class StaticObjectTest extends AbstractPhpFileTest {
	
	private Class<?> clazz;
	
	private Method valueOf;
	
	private Method getVal;
	
	private PHPClassDescriptor descStaticClass;
	
	public StaticObjectTest() {
		super("StaticClass.php");
	}

	@Before
	public void setUp() throws Exception {
		descStaticClass = container.getClassDescriptor("StaticClass");
		Assert.assertNotNull(descStaticClass);
		Assert.assertEquals(descStaticClass.getStaticVarList().size(), 2);
		Assert.assertNotNull(descStaticClass.getStaticVarList().get("a"));
		Assert.assertNotNull(descStaticClass.getStaticVarList().get("b"));
		
		clazz = descStaticClass.getGeneratedClass();
		Assert.assertNotNull(clazz);
		
		Field a = clazz.getDeclaredField("a");
		Assert.assertNotNull(a);
		Field b = clazz.getDeclaredField("b");
		Assert.assertNotNull(b);
		
		valueOf = clazz.getMethod("valueOf", new Class<?>[]{String.class});
		Assert.assertNotNull(valueOf);
		
		getVal = clazz.getMethod("getVal");
		Assert.assertNotNull(getVal);
	}
		
	@Test
	public void testSimple() throws Exception {
		String id = "PHPOBJ_STATIC_StaticClass_A";
		Object o = descStaticClass.createNewObject("SALUT");
		Assert.assertNotNull(o);
	
		Object a = valueOf.invoke(null, "a");
		Assert.assertNotNull(a);
		Assert.assertEquals(true, a instanceof PHPObject);
		PHPObject staticA = (PHPObject) a;
		
		Assert.assertEquals(id, staticA.getPHPSudId());
		
		String s = container.get(PHPInterpeter.getType(PHPInterpeter.getObject(id)));
		Assert.assertEquals("object", s);
		
		Object res = getVal.invoke(a);
		Assert.assertNotNull(res);
		Assert.assertEquals(String.class, res.getClass());
		Assert.assertEquals("a", res);
	}
	
	@Test
	public void testEquals() throws Exception {
		Object a1 = valueOf.invoke(null, "a");
		Object a2 = valueOf.invoke(null, "a");
		Assert.assertTrue(a1.hashCode() == a2.hashCode());
		Assert.assertTrue(a1.equals(a2));
		Assert.assertTrue(a2.equals(a1));
	}	
	
}
