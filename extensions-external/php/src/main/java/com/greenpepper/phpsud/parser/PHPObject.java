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

import org.apache.log4j.Logger;

import com.greenpepper.phpsud.container.IObjectParser;
import com.greenpepper.phpsud.container.PHPClassDescriptor;
import com.greenpepper.phpsud.container.PHPContainer;
import com.greenpepper.phpsud.exceptions.PHPException;
import com.greenpepper.phpsud.phpDriver.PHPInterpeter;

/**
 * @author Bertrand Paquet
 */
public class PHPObject {
	
	private static final Logger LOGGER = Logger.getLogger(PHPObject.class);

	private String phpSudId;
	
	protected PHPClassDescriptor desc;
	
	private PHPContainer container;

	public PHPObject(PHPContainer container, String className, String phpSudId) {
		this.phpSudId = phpSudId;
		this.container = container;
		try {
			this.desc = container.getClassDescriptor(className);
		} catch(PHPException e) {
			LOGGER.error("Class not found " + className);
		}
	}
	
	public String getPHPSudId() {
		return phpSudId;
	}
	
	public Object invoke(String methodName) {
		IObjectParser parser = container.getObjectParser();
		LOGGER.debug("Calling " + methodName + " on object " + phpSudId);
		String expr = PHPInterpeter.getObject(phpSudId) + "->" + methodName + "()";
		try {
			return parser.parse(expr);
		} catch (PHPException e) {
			LOGGER.error("Unable to get value : " + e.toString());
		}
		return null;
	}
	
	protected boolean equalsTo(Object o) {
		if (o instanceof PHPObject) {
			PHPObject phpObject = (PHPObject) o;
			try {
				String expr = PHPInterpeter.getObject(this.phpSudId) + "->equals(" + PHPInterpeter.getObject(phpObject.phpSudId) + ")";
				Object z = container.getObjectParser().parse(expr);
				if (z instanceof Boolean) {
					Boolean res = (Boolean) z;
					return res;	
				}
			} catch (PHPException e) {
				LOGGER.error("Error when calling equalsTo " + e.toString());
			}
		}
		else {
			LOGGER.error("Equals call on bad object type : " + o.getClass().getCanonicalName());
		}
		return false;
	}
	
	protected boolean phpEquals(Object o) {
		if (o instanceof PHPObject) {
			PHPObject phpObject = (PHPObject) o;
			try {
				String expr = PHPInterpeter.getObject(this.phpSudId) +  " === " + PHPInterpeter.getObject(phpObject.phpSudId);
				Object z = container.getObjectParser().parse(expr);
				if (z instanceof Boolean) {
					Boolean res = (Boolean) z;
					return res;	
				}
			} catch (PHPException e) {
				LOGGER.error("Error when calling == " + e.toString());
			}
		}
		else {
			LOGGER.error("PhpEquals call on bad object type : " + o.getClass().getCanonicalName());
		}
		return false;
	}
	
	public PHPClassDescriptor getClassDescriptor() {
		return desc;
	}
	
	protected static String parse(PHPContainer container, String className, String s) {
		try {
			String expr = PHPInterpeter.saveObject(className + "::parse('" + s + "')");
			return container.get(expr);
		} catch (PHPException e) {
			LOGGER.error("Error when calling parse " + e.toString());
		}
		return null;
	}
	
}
