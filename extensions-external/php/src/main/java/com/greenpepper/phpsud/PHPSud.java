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
package com.greenpepper.phpsud;

import org.apache.log4j.Logger;

import com.greenpepper.document.Document;
import com.greenpepper.phpsud.container.PHPContainer;
import com.greenpepper.phpsud.exceptions.PHPException;
import com.greenpepper.phpsud.fixtures.PHPFixture;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.util.ExceptionImposter;

/**
 * @author Bertrand Paquet
 */
public class PHPSud extends DefaultSystemUnderDevelopment {
	
	private static final Logger LOGGER = Logger.getLogger(PHPSud.class);
	
	private PHPContainer container;
	
	private String phpExec;
	private String workingDirectory;
	private String phpInitFile;
	
	public PHPSud(String ... params) {
		if (params.length != 3) {
			LOGGER.error("Syntax : phpExec;workingDirectory;phpInitFile");
			return;
		}
		this.phpExec = params[0];
		this.workingDirectory = params[1];
		this.phpInitFile = params[2];
	}
	
	@Override
	public void addImport(String packageName) {
		LOGGER.info("Import package " + packageName + " ignored.");
	}

	@Override
	public void onEndDocument(Document document) {
		LOGGER.info("End document");
		PHPContainer.dump();
		
		if (container != null) {
			container.close();
		}

		super.onEndDocument(document);
	}

	@Override
	public void onStartDocument(Document document) {
		LOGGER.info("Start document");
		super.onStartDocument(document);
		LOGGER.debug("Create new PHP SUD");
		try {
			container = PHPContainerFactory.createContainer(phpExec, workingDirectory, phpInitFile);
		} catch (PHPException e) {
			LOGGER.error("Unable to create PHPContainer " + e.toString());
			throw ExceptionImposter.imposterize(e);
		}
	}

	@Override
	public Fixture getFixture(String name, String... params) throws Throwable {
		LOGGER.debug("GetFixture " + name + " with " + params.length + " params");
		return new PHPFixture(container, name, params);
	}
}