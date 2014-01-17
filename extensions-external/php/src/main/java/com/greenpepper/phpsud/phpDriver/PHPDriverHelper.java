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

import hidden.org.codehaus.plexus.interpolation.os.Os;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.greenpepper.util.StringUtil;

/**
 * @author Bertrand Paquet
 */
public class PHPDriverHelper {

	private static final Logger LOGGER = Logger.getLogger(PHPDriverHelper.class);
	
	private static final String phpExecutableProperties = "php.executable";
	private static final String phpDriverTimeoutProperties = "gp.phpdriver.timeout";
		
	private static PHPDriverHelper _instance = null;
	
	private File interpretor;
	
	private String phpExec;
	
	private int timeout;
	
	private Properties properties;
	
	public static PHPDriverHelper getInstance() throws IOException {
		if (_instance == null) {
			_instance = new PHPDriverHelper();
			_instance.interpretor = createTmpPhpFile("/interpretor.php");
			_instance.phpExec = null;
		}
		return _instance;
	}
	
	private PHPDriverHelper() throws IOException {
		properties = new Properties();
		InputStream is = getClass().getResourceAsStream("/phpdriver.properties");
		if (is != null) {
			properties.load(is);
			is.close();
		}
		readTimeout();
		LOGGER.info("Default timeout = " + timeout + " s");
	}

	private void readTimeout() {
		String prop = System.getProperty(phpDriverTimeoutProperties);
		if (prop == null) {
			prop = properties.getProperty("timeout");
		}
		timeout = Integer.parseInt(prop);
	}

	private static void copyFile(BufferedReader in, BufferedWriter out) throws IOException {
		while (true) {
			String line = in.readLine();
			if (line == null) {
				break;
			}
			out.write(line);
			out.newLine();
			out.flush();
		}
	}

	public static File createTmpPhpFile(String resourceName) throws IOException {
		return createTmpPhpFileFromResource(resourceName, true);
	}

	public static File createTmpPhpFileFromResource(String resourceName, boolean deleteOnExit) throws IOException {
		InputStream is = getInstance().getClass().getResourceAsStream(resourceName);
		if (is == null) {
			throw new IOException("Unable to find php file " + resourceName);
		}
		return copyFile(is, deleteOnExit);
	}
	
	public static File copyFile(InputStream is, boolean deleteOnExit) throws IOException {
		File f = File.createTempFile("php", ".php");
		if (deleteOnExit) {
			f.deleteOnExit();
		}
		FileWriter fw = new FileWriter(f);
		copyFile(new BufferedReader(new InputStreamReader(is)), new BufferedWriter(fw));
		is.close();
		fw.close();
		return f;
	}
	
	public File getInterpretor() {
		return interpretor;
	}
	
	private boolean tryPhpExec(String exec) {
		if (StringUtil.isEmpty(exec)) {
			return false;
		}
		File f = new File(exec);
		if (f.exists()) {
			phpExec = exec;
			return true;
		}
		return false;
	}

	private void initPhpExec(String exec) throws IOException {
		if (tryPhpExec(System.getProperty(phpExecutableProperties))) {
			LOGGER.info("Using " + phpExecutableProperties + " properties for phpExec " + phpExec);
			return;
		}
		if (tryPhpExec(exec)) {
			LOGGER.info("Using for phpExec " + phpExec + " : " + phpExec);
			return;
		}
		for(Object o : Os.getValidFamilies()) {
			if (o instanceof String) {
				String family = (String) o;
				if (Os.isFamily(family)) {
					String e = properties.getProperty(family);
					if (tryPhpExec(e)) {
						LOGGER.info("Using value os (family=" + family + ") for phpExec " + e);
						return;
					}	
				}
			}
		}
		throw new IOException("Unable to find PHP intrepretor. Please set the property with -D" + phpExecutableProperties + "=path/php");
	}
		
	public String getPhpExec(String exec) throws IOException {
		if (phpExec != null) {
			return phpExec;
		}
		initPhpExec(exec);
		return phpExec;
	}
	
	public int getTimeout() {
		return timeout;
	}

}
