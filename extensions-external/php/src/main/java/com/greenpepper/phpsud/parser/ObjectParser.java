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

import com.greenpepper.phpsud.container.IObjectParser;
import com.greenpepper.phpsud.container.PHPClassDescriptor;
import com.greenpepper.phpsud.container.PHPContainer;
import com.greenpepper.phpsud.exceptions.PHPException;
import com.greenpepper.phpsud.helper.Helper;
import com.greenpepper.phpsud.phpDriver.PHPInterpeter;

/**
 * @author Bertrand Paquet
 */
public class ObjectParser implements IObjectParser {
	
	private PHPContainer container;

	public ObjectParser() {
	}
	
	public Object parse(String expr) throws PHPException {
		return parse(expr, null);
	}

	public Object parse(String expr, String id) throws PHPException {
		String varName = PHPInterpeter.getVar();
		container.run(varName + " = " + expr);
		String type = container.get(PHPInterpeter.getType(varName));
		if ("string".equals(type)) {
			return container.get(varName);
		}
		if ("boolean".equals(type)) {
			return new Boolean("1".equals(container.get(varName)));
		}
		if ("integer".equals(type)) {
			return new Integer(container.get(varName));
		}
		if ("double".equals(type)) {
			return new Double(container.get(varName));
		}
		if ("array".equals(type)) {
			String s = container.get(PHPInterpeter.serialize(PHPInterpeter.getArray(varName)));
			String [] ids = Helper.parseArray(s);
			Object [] array = new Object[ids.length];
			for(int i = 0; i < ids.length; i ++) {
				array[i] = parse(PHPInterpeter.getObject(ids[i]), ids[i]);
			}
			return array;
		}
		if ("object".equals(type)) {
			String className = container.get(PHPInterpeter.getClass(varName));
			if (id == null) {
				id = container.get(PHPInterpeter.saveObject(varName));
			}
			PHPClassDescriptor desc = container.getClassDescriptor(className);
			return desc.createNewObject(id);
		}
		return null;
	}

	public void setPHPContainer(PHPContainer container) {
		this.container = container;
	}
	

}
