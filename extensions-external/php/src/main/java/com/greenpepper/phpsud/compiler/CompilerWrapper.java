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

import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import com.greenpepper.phpsud.parser.PHPObject;

/**
 * @author Bertrand Paquet
 */
public class CompilerWrapper {
	
	private ClassPool classPool;
	
	private CtClass clazz;
	
	private Class<?> javaClazz;
	
	private CtClass resolve(Class<?> clazzToResolve) throws NotFoundException {
		if (clazzToResolve == null) {
			return clazz;
		}
		if (clazzToResolve == Boolean.class) {
			return CtClass.booleanType;
		}
		return classPool.get(clazzToResolve.getCanonicalName());
	}
	
	private CtClass [] getList(Class<?> [] list) throws NotFoundException {
		CtClass  [] l = new CtClass[list.length];
		for(int i = 0; i < l.length; i ++) {
			l[i] = resolve(list[i]);
		}
		return l;
	}
	
	private String formatCode(String [] lines) {
		StringBuilder src = new StringBuilder();
		src.append("{\n");
		for(String line : lines) {
			src.append(line + ";\n");
		}
		src.append("}\n");
		return src.toString();
	}
	
	public CompilerWrapper(String javaClassName, Class<?> superClass) throws CannotCompileException, NotFoundException {
		classPool = ClassPool.getDefault();
		classPool.insertClassPath(new ClassClassPath(PHPObject.class));
		javaClazz = null;
		clazz = classPool.makeClass(javaClassName);
		clazz.setSuperclass(resolve(superClass));
	}
	
	public void addMethod(String methodName, Class<?> returnType, Class<?> [] params, boolean isStatic, String ... lines) throws CannotCompileException, NotFoundException {
		CtMethod m = new CtMethod(resolve(returnType), methodName, getList(params), clazz);
		int modifiers = Modifier.PUBLIC;
		if (isStatic) {
			modifiers += Modifier.STATIC;
		}
		m.setModifiers(modifiers);
		m.setBody(formatCode(lines));
		clazz.addMethod(m);
	}
	
	public void addField(String fieldName, Class<?> fildType, boolean isPublic, boolean isStatic, boolean isFinal) throws CannotCompileException, NotFoundException {
		addField(fieldName, fildType, isPublic, isStatic, isFinal, null);
	}
	
	public void addField(String fieldName, Class<?> fieldType, boolean isPublic, boolean isStatic, boolean isFinal, String initCode) throws CannotCompileException, NotFoundException {
		CtField f = new CtField(resolve(fieldType), fieldName, clazz);
		int modifiers = 0;
		modifiers += isPublic ? Modifier.PUBLIC : Modifier.PRIVATE;
		if (isStatic) {
			modifiers += Modifier.STATIC;
		}
		if (isFinal) {
			modifiers += Modifier.FINAL;
		}
		f.setModifiers(modifiers);
		if (initCode != null) {
			clazz.addField(f, initCode);
		}
		else {
			clazz.addField(f);
		}
	}
	
	public void addConstructor(Class<?> [] params, String ... lines) throws CannotCompileException, NotFoundException {
		CtConstructor c = new CtConstructor(getList(params), clazz);
		int modifiers = Modifier.PUBLIC;
		c.setModifiers(modifiers);
		c.setBody(formatCode(lines));
		clazz.addConstructor(c);
	}
	
	public Class<?> getGeneratedClass() throws CannotCompileException {
		if (javaClazz == null) {
			javaClazz = clazz.toClass();
		}
		return javaClazz;
	}

}
