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

import com.greenpepper.phpsud.container.IPHPJavaClassCreator;
import com.greenpepper.phpsud.container.IPHPJavaClassCreatorFactory;
import com.greenpepper.phpsud.container.PHPClassDescriptor;
import com.greenpepper.phpsud.container.PHPContainer;

/**
 * @author Bertrand Paquet
 */
public class PHPJavaClassCreatorFactory implements IPHPJavaClassCreatorFactory {

	private int uniqueId;
	
	public PHPJavaClassCreatorFactory(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	public IPHPJavaClassCreator getClassCreator(PHPContainer container, PHPClassDescriptor desc) {
		return new PHPJavaClassCreator(container, desc, uniqueId);
	}
}
