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

import com.greenpepper.runner.CommandLineRunner;
import com.greenpepper.runner.CompositeSpecificationRunnerMonitor;
import com.greenpepper.runner.LoggingMonitor;
import com.greenpepper.runner.RecorderMonitor;

/**
 * @author Bertrand Paquet
 */
public class Runner {

	public static String run(String [] args) {
		if (args.length < 4) {
			return "Syntax error : args : " + " phpExec|default workingDirectory initPhpFile";
		}
		String [] l = new String[args.length - 1];
		for(int i = 3; i < args.length; i ++) {
			l[i - 1] = args[i];
		}
		l[0] = "--sud";
		l[1] = PHPSud.class.getCanonicalName() + ";" + args[0] + ";" + args[1] + ";" + args[2];
		StringBuilder cmd = new StringBuilder();
		for(String s : l) {
			cmd.append(s).append(" ");
		}
		System.out.println("Command line : " + cmd.toString());
		try {
			CommandLineRunner runner = new CommandLineRunner();
			RecorderMonitor recorderMonitor = new RecorderMonitor();
			LoggingMonitor loggingMonitor = new LoggingMonitor( System.out, System.err );
			runner.setMonitor(new CompositeSpecificationRunnerMonitor(recorderMonitor, loggingMonitor));
			runner.run(l);

			if (recorderMonitor.getStatistics().hasFailed()) {
				return "Some errors detected";
			}
			return "";
		} catch (Exception e) {
			return "Exception when executing tests " + e.toString();
		}
	}
	
	public static void main(String [] args) {
		String result = run(args);
		if (result.length() != 0) {
			System.err.println(result);
			System.exit(1);
		}
	}
}
