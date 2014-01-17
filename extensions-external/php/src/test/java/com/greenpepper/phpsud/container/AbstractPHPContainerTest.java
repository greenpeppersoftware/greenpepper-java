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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.greenpepper.phpsud.PHPContainerFactory;
import com.greenpepper.phpsud.compiler.PHPJavaClassCreatorFactory;
import com.greenpepper.phpsud.parser.ObjectParser;
import com.greenpepper.phpsud.phpDriver.PHPDriverHelper;

/**
 * @author Bertrand Paquet
 */
public abstract class AbstractPHPContainerTest {
	
	protected PHPContainer container;
	
	private String phpExec =  null;
	
	private String workingDirectory = ".";
	
	private String phpInitFile = null;
	
	protected AbstractPHPContainerTest() {
		
	}
	
	protected AbstractPHPContainerTest(String workingDirectory, String phpInitFile) {
		this.workingDirectory = workingDirectory;
		this.phpInitFile = phpInitFile;
	}
	
	protected void checkObject(Object expected, Object actual, Class<?> clazz) {
		Assert.assertNotNull(expected);
		Assert.assertEquals(clazz.getCanonicalName(), actual.getClass().getCanonicalName());
		Assert.assertEquals(expected, actual);
	}

	@Before
	public void setUpPHPContainer() throws Exception {
		if (phpExec == null) {
			phpExec = PHPDriverHelper.getInstance().getPhpExec(null);
		}
		container = PHPContainerFactory.createContainer(phpExec, workingDirectory, phpInitFile);
		container.setObjectParser(new ObjectParser());
		container.setClassCreatorFactory(new PHPJavaClassCreatorFactory(container.hashCode()));
	}
	
	@After
	public void tearDown() throws Exception {
		PHPContainer.dump();
		container.close();
	}

}
