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

import com.greenpepper.phpsud.parser.PHPObject;
import com.greenpepper.reflect.Message;

/**
 * @author Bertrand Paquet
 */
public class PHPObjectMessage extends Message {

	private static final Logger LOGGER = Logger.getLogger(PHPObjectMessage.class);
	
	private PHPObject object;
	
	private String name;
	
	public PHPObjectMessage(PHPObject object, String name) {
		this.object = object;
		this.name = name;
	}
	
	@Override
	public int getArity() {
		throw new UnsupportedOperationException("Not implemented yet !");
	}

	@Override
	public Object send(String... arg0) throws Exception {
		LOGGER.info("Send called with " + arg0.length); 
		return object.invoke("get" + name);
	}

}
