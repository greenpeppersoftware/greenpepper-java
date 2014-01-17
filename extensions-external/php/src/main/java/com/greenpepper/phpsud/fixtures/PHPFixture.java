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
package com.greenpepper.phpsud.fixtures;

import org.apache.log4j.Logger;

import com.greenpepper.phpsud.container.PHPClassDescriptor;
import com.greenpepper.phpsud.container.PHPContainer;
import com.greenpepper.phpsud.container.PHPMethodDescriptor;
import com.greenpepper.phpsud.exceptions.PHPException;
import com.greenpepper.phpsud.exceptions.UnknownClassException;
import com.greenpepper.phpsud.helper.Helper;
import com.greenpepper.phpsud.parser.PHPObject;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;

/**
 * @author Bertrand Paquet
 */
public class PHPFixture implements Fixture {

	private static final Logger LOGGER = Logger.getLogger(PHPFixture.class);
	
	private String id;
	private PHPObject object;		
	private PHPClassDescriptor desc;
	
	public static PHPClassDescriptor findClass(PHPContainer php, String className) throws PHPException {
		PHPClassDescriptor desc;
		desc = php.getClassDescriptor(className + "Fixture");
		if (desc != null) {
			return desc;
		}
		desc = php.getClassDescriptor(className); 
		if (desc != null) {
			return desc;
		}
		String formattedClassName = Helper.formatProcedureName(className);
		desc = php.getClassDescriptor(formattedClassName + "Fixture");
		if (desc != null) {
			return desc;
		}
		desc = php.getClassDescriptor(formattedClassName); 
		if (desc != null) {
			return desc;
		}
		return null;
	}
	
	public static PHPMethodDescriptor findMethod(PHPClassDescriptor desc, String methodName) {
		PHPMethodDescriptor meth;
		meth = desc.getMethod(Helper.formatProcedureName(methodName));
		if (meth != null) {
			return meth;
		}
		meth = desc.getMethod("get" + Helper.formatProcedureName(methodName));
		if (meth != null) {
			return meth;
		}
		return null;
	}
	
	public PHPFixture(PHPContainer php, String className, String... params) throws PHPException {
		this.desc = findClass(php, className);
		if (this.desc == null) {
			LOGGER.error("Class not found " + className);
			throw new UnknownClassException(className);
		}
		id = php.createObject(this.desc, params);
		object = new PHPObject(php, this.desc.getClassName(), id);
	}
	
	public boolean canCheck(String arg0) {
		throw new UnsupportedOperationException("Not implemented yet ! " + arg0);
	}

	public boolean canSend(String arg0) {
		throw new UnsupportedOperationException("Not implemented yet ! " + arg0);
	}

	public Message check(String arg0) throws NoSuchMessageException {
		String methodName = Helper.purge(arg0);
		LOGGER.debug("Check for class " + desc + ", method " + methodName);
		PHPMethodDescriptor meth = findMethod(desc, methodName);
		if (meth == null) {
			LOGGER.error("Method not found on class " + desc + ", method " + methodName);
			throw new NoSuchMessageException(methodName + " for class " + desc);
		}
		return new PHPMessage(meth, id, true);
	}

	public Fixture fixtureFor(Object arg0) {
		if (arg0 instanceof PHPObject) {
			PHPObject object = (PHPObject) arg0;
			LOGGER.debug("Creating PhpObject fixture for " + arg0);
			return new PHPObjectFixture(object);
		}
		LOGGER.debug("Creating empty fixture for " + arg0);
		return new EmptyFixture(this, arg0);
	}

	public Object getTarget() {
		LOGGER.debug("Get Target for class " + desc);
		PHPMethodDescriptor meth = findMethod(desc, "query");
		if (meth == null) {

			if (object != null) {
				return object;
			}

			LOGGER.error("Method not found on class " + desc + ", method query");
		}
		else {
			PHPMessage m = new PHPMessage(meth, id, true);
			try {
				return m.send();
			} catch (Exception e) {
				LOGGER.error("Get target error " + e.toString());
			}
		}
		return null;
	}

	public Message send(String arg0) throws NoSuchMessageException {
		String methodName = Helper.purge(arg0);
		LOGGER.info("Send for class " + desc + ", method " + methodName);
		PHPMethodDescriptor meth = findMethod(desc, "set " + methodName);
		if (meth == null) {
			LOGGER.error("Method not found on class " + desc + ", method " + methodName);
			throw new NoSuchMessageException(methodName + " for class " + desc);
		}
		return new PHPMessage(meth, id, false);
	}

}
