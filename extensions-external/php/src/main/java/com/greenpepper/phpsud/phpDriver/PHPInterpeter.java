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

/**
 * @author Bertrand Paquet
 */
public class PHPInterpeter {

	public static String getObject(String id) {
		return "phpsud_getObject(\"" + id + "\")";
	}
	
	public static String saveObject(String obj) {
		return "phpsud_saveObject(" + obj + ")";
	}
	
	public static String getStaticVar(String className) {
		return serialize("phpsud_getStaticVar(\"" + className + "\")");
	}
	
	public static String getMethodsInClass(String className) {
		return serialize("phpsud_getMethodsInClass(\"" + className + "\")");
	}
	
	public static String getParamsInMethods(String className, String methodName) {
		return serialize("phpsud_getParamsInMethod(\"" + className + "\", \"" + methodName + "\")");
	}
	
	public static String getArray(String array) {
		return "phpsud_getArray(" + array + ")";
	}
	
	public static String getClass(String obj) {
		return "get_class(" + obj + ")";
	}
	
	public static String getType(String obj) {
		return "gettype(" + obj + ")";
	}
	
	public static String serialize(String obj) {
		return "serialize(" + obj + ")";
	}
	
	private static int index = 0;
	
	public static String getVar() {
		String varName = "$phpsud_" + index;
		index ++;
		return varName;
	}
}
