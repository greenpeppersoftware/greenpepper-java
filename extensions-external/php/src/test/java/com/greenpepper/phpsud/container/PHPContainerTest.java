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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Bertrand Paquet
 */
public class PHPContainerTest extends AbstractPHPContainerTest {
	
	@Test
	public void testConstructor() throws Exception {
		
	}

	@Test
	public void testCreateObjectOK() throws Exception {
		PHPClassDescriptor desc = container.getClassDescriptor("Exception");
		Assert.assertNotNull(desc);
		String id = container.createObject(desc);
		Assert.assertNotNull(id);
		Assert.assertTrue(id.length() > 0);
	}
	
	@Test
	public void testCreateObjectKO() throws Exception {
		Assert.assertNull(container.getClassDescriptor("MyPrivateException"));
	}
	
	private List<String> stdout = new ArrayList<String>();
	private List<String> stderr = new ArrayList<String>();
	
	class CustomLogger implements IConsoleLogger {
		
		public CustomLogger() {
			stderr.clear();
			stdout.clear();
		}

		public void stderr(String line) {
			stderr.add(line);
		}

		public void stdout(String line) {
			stdout.add(line);
		}
		
	}
	
	@Test
	public void testConsoleLogger() throws Exception {
		container.addConsoleLogger(new CustomLogger());
		container.run("echo \"2\n\"");
		Assert.assertEquals(1, stdout.size());
	}

}
