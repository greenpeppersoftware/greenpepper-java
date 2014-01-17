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
package com.greenpepper.phpsud.compiler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.greenpepper.phpsud.container.IPHPJavaClassCreator;
import com.greenpepper.phpsud.container.PHPClassDescriptor;
import com.greenpepper.phpsud.container.PHPContainer;
import com.greenpepper.phpsud.container.PHPMethodDescriptor;
import com.greenpepper.phpsud.parser.PHPObject;

/**
 * @author Bertrand Paquet
 */
public class PHPJavaClassCreator implements IPHPJavaClassCreator {
	
	private static final Logger LOGGER = Logger.getLogger(PHPJavaClassCreator.class);
	
	private static final String packageName = "com.greenpepper.phpsud.compiler.temp.";
	
	private static final String fieldPHPContainer = "localPHPContainer";
	
	private String javaClassName;
	
	private PHPClassDescriptor descriptor;
	
	private Class<?> clazz;
	
	private Constructor<?> constructor;
	
	public PHPJavaClassCreator(PHPContainer container, PHPClassDescriptor descriptor, int uniqueId) {
		this.javaClassName = packageName + "container" + uniqueId + "." + descriptor.getClassName();
		this.descriptor = descriptor;
		createClass(container);
	}
	
	private void createClass(PHPContainer container) {
		try {
			boolean equalsImplemented = false;
			CompilerWrapper wrapper = new CompilerWrapper(javaClassName, PHPObject.class);
			
			// Add logger field
		
			wrapper.addField(
					"LOGGER",
					Logger.class,
					false,
					true,
					true,
					Logger.class.getCanonicalName() + ".getLogger(" + javaClassName + ".class)"
					);
			
			// Add container filed
			
			wrapper.addField(
					fieldPHPContainer,
					PHPContainer.class,
					false,
					true,
					true,
					PHPContainer.class.getCanonicalName() + ".getPHPContainer(" + container.getId() + ")"
					);
	
			// Add constructor
			
			wrapper.addConstructor(
					new Class<?> []{String.class},
					"super(" + fieldPHPContainer + ", \"" + descriptor.getClassName() + "\", $1)",
					"LOGGER.debug(\"Create object " + javaClassName + " for id : \" + $1)"
					);
			
			// Add methods
			
			for(String m : descriptor.getMethodList()) {
				PHPMethodDescriptor method = descriptor.getMethod(m);
				String name = method.getMethodName();
				
				// Add getters
				
				if (name.startsWith("get")) {
					wrapper.addMethod(
							name,
							Object.class,
							new Class<?>[]{},
							false,
							"return invoke(\"" + name + "\")"
							);
				}
				
				// Add toString
				
				if (name.equals("toString") && method.getParamList().size() == 0) {
					wrapper.addMethod(
							name, 
							String.class, 
							new Class<?>[]{},
							false,
							"return (String) invoke(\"" + name + "\")"
							);
				}
				
				// Add equals
				
				if (name.equals("equals") && method.getParamList().size() == 1) {
					wrapper.addMethod(
							name, 
							Boolean.class, 
							new Class<?>[]{Object.class},
							false,
							"LOGGER.debug(\"Equals called : \" + $1)",
							"return super.equalsTo($1)"
							);
					equalsImplemented = true;
				}
				
				// Add parse
				
				if (name.equals("parse") && method.getParamList().size() == 1) {
					wrapper.addMethod(
							name, 
							null, 
							new Class<?>[]{String.class},
							true,
							"LOGGER.debug(\"Parse called : \" + $1)",
							"String id = " + PHPObject.class.getCanonicalName() + ".parse(" + fieldPHPContainer + ", \"" + descriptor.getClassName() + "\", $1)",
							"return id == null ? null : new " + javaClassName + "(id);"
							);
				}
			}
			
			// Add default equals if not implemented in PHP
			
			if (!equalsImplemented) {
				wrapper.addMethod(
						"equals", 
						Boolean.class, 
						new Class<?>[]{Object.class},
						false,
						"LOGGER.debug(\"Transfer equals to PHP : \" + this + \" and \" + $1)",
						"return super.phpEquals($1)"
						);
				LOGGER.info("Warning, equals method not implemented in PHP Class " + descriptor.getClassName());
			}
			
			// Add Static var and value of for enum
			
			if (descriptor.getStaticVarList().size() != 0) {
				List<String> l = new ArrayList<String>();
		 		l.add("LOGGER.debug(\"valueof called for \" + $1)");
		 		for(String s : descriptor.getStaticVarList().keySet()) {
		 			l.add("if (\"" + s +"\".equals($1)) {");
		 			l.add("return " + s);
		 			l.add("}");
		 			wrapper.addField(
		 					s,
		 					null,
		 					true,
		 					true,
		 					false,
		 					"new " + javaClassName + "(\"PHPOBJ_STATIC_" + descriptor.getClassName() + "_" + descriptor.getStaticVarList().get(s).substring(1) + "\")"
		 					);
		 		}
		 		l.add("return null");
		 		wrapper.addMethod(
		 				"valueOf",
		 				null,
		 				new Class<?>[]{String.class},
		 				true,
		 				l.toArray(new String[0]));
		 	}
			clazz = wrapper.getGeneratedClass();
			constructor = clazz.getConstructor(new Class<?>[] {String.class});
		}
		catch(Exception e) {
			LOGGER.error("Unable to compile code " + e.toString());
			e.printStackTrace();
		}	
	}
	
	public Class<?> getGeneratedClass() {
		return clazz;
	}
	
	public Object createNewObject(String id) {
		if (this.clazz == null || this.constructor == null) {
			LOGGER.error("Unable to get class or constructor");
			return null;
		}
		try {
			Object o = constructor.newInstance(id);
			return o;
		} catch (Exception e) {
			LOGGER.error("Unable to create object " + e.toString());
		}
		return null;
	}
	
}
