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

import com.greenpepper.phpsud.container.PHPContainer;
import com.greenpepper.phpsud.container.PHPMethodDescriptor;
import com.greenpepper.phpsud.exceptions.ExecutionErrorException;
import com.greenpepper.phpsud.phpDriver.PHPInterpeter;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.SystemUnderDevelopmentException;


/**
 * @author Bertrand Paquet
 */
public class PHPMessage extends Message {

	private static final Logger LOGGER = Logger.getLogger(PHPMessage.class);
	
	private String id;
	
	private String realId;
	
	private boolean readResponse;
	
	private PHPMethodDescriptor desc;
	
	public PHPMessage(PHPMethodDescriptor desc, String id, boolean readResponse) {
		this.desc = desc;
		this.id = id;
		this.realId = PHPInterpeter.getObject(id);
		this.readResponse = readResponse;
	}

	@Override
	public int getArity() {
		throw new UnsupportedOperationException("Not implemented yet !");
	}

	@Override
	public Object send(String... params) throws Exception {
		LOGGER.debug("Send on class " + desc.getClassDescriptor() + ", method " + desc + ", id " + id + " with " + params.length + " params");
		try {
			if (!readResponse) {
				desc.execNoResponse(realId, params);
				return null;
			}
			Object res = desc.exec(realId, params);
			return res;
		} catch (ExecutionErrorException e) {
			PHPContainer.dump();
			throw new SystemUnderDevelopmentException(e);
		}
	}

}
