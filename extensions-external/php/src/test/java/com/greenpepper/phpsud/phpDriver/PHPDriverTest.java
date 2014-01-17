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
package com.greenpepper.phpsud.phpDriver;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.greenpepper.phpsud.exceptions.ExecutionErrorException;
import com.greenpepper.phpsud.exceptions.PHPTimeoutException;
import com.greenpepper.phpsud.exceptions.SyntaxErrorException;

/**
 * @author Bertrand Paquet
 */
public class PHPDriverTest {

	private static final String triggerError = "function run() {trigger_error(\"Division by zero\", E_USER_ERROR);}";
	private static final String launchException = "function run() {throw new Exception(\"myCustomException\");}";
	
	private PHPDriver php;
	
	private void check(String s) throws Exception {
		Assert.assertEquals(s, php.execGet("\"" + s + "\""));
	}
	
	@Before
	public void setUp() throws Exception {
		php = new PHPDriver(5);
	}

	@After
	public void finalCheck() throws Exception {
		String stderr = php.getStderr(); 
		Assert.assertNull(stderr);
		String stdout = php.getStdout();
		Assert.assertNull(stdout);
		php.close();
	}

	@Test
	public void testString() throws Exception {
		check("Ceci est un test");
	}
	
	@Test
	public void testTwoString() throws Exception {
		check("Ceci est un test 1");
		check("Ceci est un test 2");
	}
	
	@Test
	public void testEmptyString() throws Exception {
		check("");
	}
	
	@Test
	public void testInteger() throws Exception {
		Assert.assertEquals("12", php.execGet("12"));
	}
	
	@Test
	public void testBoolean() throws Exception {
		Assert.assertEquals("1", php.execGet("true"));
	}
	
	@Test
	public void testFloat() throws Exception {
		Assert.assertEquals("1.234", php.execGet("1.234"));
	}
	
	@Test
	public void testFunction() throws Exception {
		Assert.assertEquals("string", php.execGet(PHPInterpeter.getType("\"s\"")));
	}
	
	@Test
	public void testCR() throws Exception {
		check("Salut\nsalut");
	}
	
	@Test
	public void testStrangeCharacters() throws Exception {
		String s = php.execGet("\"'\\\"\\/\"");
		Assert.assertEquals("'\"\\/", s);
		finalCheck();
	}

	@Test(expected=SyntaxErrorException.class)
	public void testSyntaxErrorGet() throws Exception {
		try {
			php.execGet(launchException);
		}
		catch(SyntaxErrorException e) {
			php.flushConsoles();
			check("Ceci est un test");
			throw e;
		}
		Assert.fail();
	}
	
	@Test
	public void testWarningGet() throws Exception {
		php.execGet("1 / 0");
		Assert.assertNotNull(php.getStdout());
		String warning = php.getStdout();
		Assert.assertNotNull(warning);
		Assert.assertTrue(warning.contains("Division by zero"));
	}
	
	@Test(expected=ExecutionErrorException.class)
	public void testErrorGet() throws Exception {
		php.execRun(triggerError);
		try {
			php.execGet("run()");
		} catch (ExecutionErrorException e) {
			String msg = e.getMessage();
			Assert.assertTrue(msg.contains("Division by zero"));
			check("Ceci est un test");
			throw e;
		}
		Assert.fail();
	}
	
	@Test(expected=ExecutionErrorException.class)
	public void testExceptionGet() throws Exception {
		php.execRun(launchException);
		try {
			php.execGet("run()");
		} catch (ExecutionErrorException e) {
			String msg = e.getMessage();
			Assert.assertTrue(msg.contains("myCustomException"));
			check("Ceci est un test");
			throw e;
		}
		Assert.fail();
	}
	
	@Test
	public void testRun() throws Exception {
		php.execRun("$i = 1");
		Assert.assertEquals("1", php.execGet("$i"));
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testSyntaxErrorRun() throws Exception {
		try {
			php.execRun("function() {ret $a}");
		}
		catch(SyntaxErrorException e) {
			php.flushConsoles();
			check("Ceci est un test");
			throw e;
		}
		Assert.fail();
	}
	
	@Test
	public void testWarningRun() throws Exception {
		php.execRun("1 / 0");
		Assert.assertNotNull(php.getStdout());
		String warning = php.getStdout();
		Assert.assertNotNull(warning);
		Assert.assertTrue(warning.contains("Division by zero"));
	}
	
	@Test(expected=ExecutionErrorException.class)
	public void testErrorRun() throws Exception {
		php.execRun(triggerError);
		try {
			php.execRun("run()");
		} catch (ExecutionErrorException e) {
			String msg = e.getMessage();
			Assert.assertTrue(msg.contains("Division by zero"));
			check("Ceci est un test");
			throw e;
		}
		Assert.fail();
	}
	
	@Test(expected=ExecutionErrorException.class)
	public void testExceptionRun() throws Exception {
		php.execRun(launchException);
		try {
			php.execRun("run()");
		} catch (ExecutionErrorException e) {
			String msg = e.getMessage();
			Assert.assertTrue(msg.contains("myCustomException"));
			check("Ceci est un test");
			throw e;
		}
		Assert.fail();
	}
	
	@Test(expected=IOException.class)
	public void testSocketClosed() throws Exception {
		php.execRun("die();");
		Assert.fail();
	}
	
	@Test
	public void testStdout() throws Exception {
		php.execRun("echo \"1\n\"");
		Assert.assertEquals("1", php.getStdout());
	}

	@Test
	public void testStderr() throws Exception {
		php.execRun("fwrite(STDERR, \"1\n\")");
		Assert.assertEquals("1", php.getStderr());
	}

	@Test(expected=PHPTimeoutException.class)
	public void testSleep() throws Exception {
		php.execRun("sleep(10)");
	}
	
	@Test
	public void testFlushConsole() throws Exception {
		php.execRun("echo \"1\n\"");
		php.execRun("fwrite(STDERR, \"3\n\")");
		php.execRun("echo \"2\n\"");
		List<String> out = php.flushStdout();
		List<String> err = php.flushStderr();
		Assert.assertEquals("3", err.get(0));
		Assert.assertEquals("1", out.get(0));
		Assert.assertEquals("2", out.get(1));
	}
	
}
