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

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.greenpepper.phpsud.container.PHPClassDescriptor;
import com.greenpepper.phpsud.container.PHPMethodDescriptor;
import com.greenpepper.phpsud.exceptions.NoConverterFoundException;
import com.greenpepper.phpsud.exceptions.WrongArgumentCountException;
import com.greenpepper.phpsud.phpDriver.PHPInterpeter;

/**
 * @author Bertrand Paquet
 */
public class ReflectionTest extends AbstractPhpFileTest {
	
	private PHPClassDescriptor desc;
	
	public ReflectionTest() {
		super("MyClass2.php");
	}
	
	@Before
	public void setUp() throws Exception {
		desc = container.getClassDescriptor("MyClass2");
	}

	@Test
	public void testReflection() throws Exception {
		Assert.assertNotNull(desc);
		
		Assert.assertEquals(desc.getClassName(), "MyClass2");
		Set<String> d = desc.getMethodList();
		Assert.assertNotNull(d);
		Assert.assertEquals(4, d.size());
		
		PHPMethodDescriptor myClass2 = desc.getMethod("MyClass2");
		Assert.assertNotNull(myClass2);
		Assert.assertEquals("MyClass2", myClass2.getMethodName());
		Assert.assertEquals(0, myClass2.getParamList().size());
		
		PHPMethodDescriptor getX = desc.getMethod("getX");
		Assert.assertNotNull(getX);
		Assert.assertEquals("getX", getX.getMethodName());
		Assert.assertEquals(2, getX.getParamList().size());
		Assert.assertEquals("Exception", getX.getParamList().get(0));
		Assert.assertNull(getX.getParamList().get(1));
		
		PHPMethodDescriptor getY = desc.getMethod("getY");
		Assert.assertNotNull(getY);
		Assert.assertEquals("getY", getY.getMethodName());
		Assert.assertEquals(1, getY.getParamList().size());
		Assert.assertEquals("SubClass", getY.getParamList().get(0));
		
		PHPMethodDescriptor echo = desc.getMethod("echoo");
		Assert.assertNotNull(echo);
		Assert.assertEquals("echoo", echo.getMethodName());
		Assert.assertEquals(2, echo.getParamList().size());
		Assert.assertNull(echo.getParamList().get(0));
		Assert.assertNull(echo.getParamList().get(1));
	}
	
	private String getId(PHPClassDescriptor desc) throws Exception {
		String id = container.createObject(desc);
		Assert.assertNotNull(id);
		Assert.assertTrue(id.length() != 0);
		return PHPInterpeter.getObject(id);
	}
	
	private PHPMethodDescriptor get(String procName) throws Exception {
		Assert.assertNotNull(desc);
		
		PHPMethodDescriptor proc = desc.getMethod(procName);
		Assert.assertNotNull(proc);
		return proc;
	}

	@Test
	public void testExecEcho() throws Exception {
		PHPMethodDescriptor echo = get("echoo");
		String id = getId(echo.getClassDescriptor());
		Object o = echo.exec(id, "s1", "s2");
		checkObject("s1s2", o, String.class);	
	}

	@Test(expected=NoConverterFoundException.class)
	public void testGetX() throws Exception {
		PHPMethodDescriptor getX = get("getX");
		getX.exec(getId(getX.getClassDescriptor()), "s1", "s2");
		Assert.fail();
	}
	
	@Test(expected=WrongArgumentCountException.class)
	public void testGetXOneArg() throws Exception {
		PHPMethodDescriptor getX = get("getX");
		getX.exec(getId(getX.getClassDescriptor()), "s1");
		Assert.fail();
	}
	
	@Test
	public void testGetY() throws Exception {
		PHPMethodDescriptor getY = get("getY");
		Object o = getY.exec(getId(getY.getClassDescriptor()), "sst");
		checkObject(3, o, Integer.class);
	}
}
