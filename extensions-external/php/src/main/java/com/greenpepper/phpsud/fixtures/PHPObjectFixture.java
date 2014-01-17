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
import com.greenpepper.phpsud.container.PHPMethodDescriptor;
import com.greenpepper.phpsud.parser.PHPObject;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;

/**
 * @author Bertrand Paquet
 */
public class PHPObjectFixture implements Fixture {

	private static final Logger LOGGER = Logger.getLogger(PHPObjectFixture.class);
	
	private PHPObject object;
	
	private PHPClassDescriptor desc;
	
	public PHPObjectFixture(PHPObject object) {
		this.object = object;
		this.desc = object.getClassDescriptor();
	}
	
	public boolean canCheck(String arg0) {
		throw new UnsupportedOperationException("Not implemented yet ! " + arg0);
	}

	public boolean canSend(String arg0) {
		throw new UnsupportedOperationException("Not implemented yet ! " + arg0);
	}

	public Message check(String arg0) throws NoSuchMessageException {
		LOGGER.debug("Check for class " + desc + ", method " + arg0);
		PHPMethodDescriptor meth = PHPFixture.findMethod(desc, arg0);
		if (meth == null) {
			LOGGER.error("Method not found on class " + desc + ", method " + arg0);
			throw new NoSuchMessageException(arg0 + " for class " + desc);
		}
		return new PHPMessage(meth, object.getPHPSudId(), true);
	}

	public Fixture fixtureFor(Object arg0) {
		throw new UnsupportedOperationException("Not implemented yet ! " + arg0);
	}

	public Object getTarget() {
		throw new UnsupportedOperationException("Not implemented yet !");
	}

	public Message send(String arg0) throws NoSuchMessageException {
		throw new UnsupportedOperationException("Not implemented yet ! " + arg0);
	}

}
