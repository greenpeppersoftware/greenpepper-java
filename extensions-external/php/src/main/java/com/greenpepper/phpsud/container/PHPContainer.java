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
package com.greenpepper.phpsud.container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import com.greenpepper.phpsud.exceptions.IOErrorException;
import com.greenpepper.phpsud.exceptions.PHPException;
import com.greenpepper.phpsud.helper.Helper;
import com.greenpepper.phpsud.phpDriver.PHPDriver;
import com.greenpepper.phpsud.phpDriver.PHPInterpeter;

/**
 * @author Bertrand Paquet
 */
public class PHPContainer {
	
	private static final Logger LOGGER = Logger.getLogger(PHPContainer.class);
	
	private PHPDriver php = null;
	
	private IObjectParser parser;
	
	private IPHPJavaClassCreatorFactory classCreatorFactory;

	private PHPClassPath classPath;
	
	private List<IConsoleLogger> loggerList = new ArrayList<IConsoleLogger>();
	
	private static Hashtable<Integer, PHPContainer> phpContainerList = new Hashtable<Integer, PHPContainer>();

	public PHPContainer(String phpExec, String workingDirectory, String phpInitFile) throws PHPException {
		LOGGER.info("Creating PHPContainer " + getId());
		phpContainerList.put(getId(), this);
		this.classPath = new PHPClassPath();
		try {
			LOGGER.info("Trying to intialize with php : " + phpExec);
			php = new PHPDriver(phpExec, workingDirectory);
			LOGGER.info("PHPDriver initialized");
			if (phpInitFile != null && phpInitFile.length() > 0) {
				run("include_once \"" + phpInitFile + "\"");
			}
		} catch (IOException e) {
			LOGGER.error("IO Error with PhpDriver : " + e.toString());
			throw new IOErrorException(flushContentAndDropIOException());
		}

		addConsoleLogger(new IConsoleLogger() {

			public void stdout(String line) {
				LOGGER.info("STDOUT : " + line);
			}

			public void stderr(String line) {
				LOGGER.info("STDERR : " + line);
			}
			
		});
	}
	
	public int getId() {
		return this.hashCode();
	}

	public void addConsoleLogger(IConsoleLogger logger) {
		loggerList.add(logger);
	}
	
	public static void dump() {
		for(PHPContainer container : phpContainerList.values()) {
			try {
				container.flushContent();
			} catch (IOException e) {
				LOGGER.error("Exception when dumping " + e.toString());
			}
		}
	}

	private String flushContent() throws IOException {
		StringBuffer msg = new StringBuffer();
		for(String l : php.flushStdout()) {
			for(IConsoleLogger log : loggerList) {
				log.stdout(l);
			}
			msg.append("stdout : " + l + "\n");
		}
		for(String l : php.flushStderr()) {
			for(IConsoleLogger log : loggerList) {
				log.stderr(l);
			}
			msg.append("stderr : " + l + "\n");
		}
		return msg.toString();
	}
	
	private String flushContentAndDropIOException() {
		try {
			return flushContent();
		}
		catch(IOException e) {
			LOGGER.error("Exception when dumping " + e.toString());
			return "Exception when dumping " + e.toString();
		}
	}

	public void close() {
		phpContainerList.remove(getId());
		LOGGER.info("Removing PHPContainer " + getId());
		try {
			php.close();
			LOGGER.info("PHPDriver Closed");
		} catch (IOException e) {
			LOGGER.error("IO Error with PhpDriver : " + e.toString());
		}
	}
	
	public void run(String command) throws PHPException {
		try {
			php.execRun(command);
			flushContent();
		} catch (IOException e) {
			LOGGER.error("IO Error with PhpDriver : " + e.toString());
			throw new IOErrorException(flushContentAndDropIOException());
		}
	}
	
	public String get(String command) throws PHPException {
		try {
			String s = php.execGet(command);
			flushContent();
			return s;
		} catch (IOException e) {
			LOGGER.error("IO Error with PhpDriver : " + e.toString());
			throw new IOErrorException(flushContentAndDropIOException());
		}
	}
	
	public String createObject(PHPClassDescriptor desc, String ... params) throws PHPException {
		String cmd = PHPInterpeter.saveObject("new " + desc.getClassName() + "(" + Helper.formatParamList(params) + ")");
		return get(cmd);
	}
	
	public void setObjectParser(IObjectParser parser) {
		this.parser = parser;
		this.parser.setPHPContainer(this);
	}
	
	public PHPClassDescriptor getClassDescriptor(String className) throws PHPException {
		return classPath.getClassDescriptor(className, this);
	}
	
	public IObjectParser getObjectParser() {
		return parser;
	}

	public IPHPJavaClassCreatorFactory getClassCreatorFactory() {
		return classCreatorFactory;
	}

	public void setClassCreatorFactory(
			IPHPJavaClassCreatorFactory classCreatorFactory) {
		this.classCreatorFactory = classCreatorFactory;
	}
	
	public static PHPContainer getPHPContainer(int id) {
		return phpContainerList.get(id);
	}
	
}
