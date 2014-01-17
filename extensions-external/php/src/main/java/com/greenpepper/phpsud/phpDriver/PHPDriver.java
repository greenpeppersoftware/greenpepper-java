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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.Base64;

import com.greenpepper.phpsud.exceptions.ExecutionErrorException;
import com.greenpepper.phpsud.exceptions.PHPException;
import com.greenpepper.phpsud.exceptions.PHPTimeoutException;
import com.greenpepper.phpsud.exceptions.SyntaxErrorException;

/**
 * @author Bertrand Paquet
 */
public class PHPDriver {

	private static final Logger LOGGER = Logger.getLogger(PHPDriver.class);

	private BufferedReader stdout;

	private BufferedReader stderr;

	private BufferedReader in;

	private BufferedWriter out;

	private int port;

	private ServerSocket server;

	private Socket client;

	public PHPDriver(int timeout) throws IOException, PHPException {
		this(PHPDriverHelper.getInstance().getPhpExec(null), ".", timeout);
	}

	private void openServer() {
		port = 8888;
		while (server == null) {
			try {
				server = new ServerSocket(port, 2, InetAddress.getByName("127.0.0.1"));

			} catch (IOException e) {
			}
			if (server == null || !server.isBound()) {
				LOGGER.warn("May be port " + port + " in used, try another");
				port++;
				server = null;
			}
		}
	}

	public PHPDriver(String exec, String workingDirectory) throws IOException, PHPException {
		this(exec, workingDirectory, PHPDriverHelper.getInstance().getTimeout());
	}

	public PHPDriver(String exec, String workingDirectory, int timeout) throws IOException, PHPException {
		server = null;
		client = null;
		String phpExec = PHPDriverHelper.getInstance().getPhpExec(exec);
		String interpretor = PHPDriverHelper.getInstance().getInterpretor().getAbsolutePath();
		Runtime r = Runtime.getRuntime();
		openServer();
		String[] cmd = { phpExec, interpretor, Integer.toString(port) };
		LOGGER.debug("Timeout configured to " + timeout + " s");
		LOGGER.debug("Open socket server : " + server.getInetAddress().getHostAddress() + ":" + server.getLocalPort());
		server.setSoTimeout(timeout * 1000);
		Process process = r.exec(cmd, null, new File(workingDirectory));
		try {
			LOGGER.debug("Start waiting for php interpretor");
			Socket client = server.accept();
			client.setSoTimeout(timeout * 1000);
			LOGGER.debug("Socket connected !");
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			String s = readLine();
			if (s == null || !s.startsWith("Starting interpretor")) {
				throw new IOException("Unable to read interpretor prompt");
			}
			LOGGER.debug(s);
		} catch (SocketTimeoutException e) {
			throw new IOException("No connection on socket");
		}
		stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
	}

	public void execRun(String command) throws IOException, PHPException {
		writeCommand("R" + command);
		readLine();
	}

	public String execGet(String command) throws IOException, PHPException {
		writeCommand("G" + command);
		String res = readLine();
		return res;
	}

	private static String encode(String s) {
		return new String(Base64.encodeBase64(s.getBytes()));
	}

	private static String decode(String s) {
		return new String(Base64.decodeBase64(s.getBytes()));
	}

	private String writeCommand(String command) throws IOException {
		String cmd = command.trim();
		if (!cmd.endsWith(";")) {
			cmd += ";";
		}
		out.write(encode(cmd));
		out.newLine();
		out.flush();
		return cmd;
	}

	public void close() throws IOException {
		out.close();
		in.close();
		if (server != null) {
			server.close();
		}
		if (client != null) {
			client.close();
		}
	}

	private String readLine() throws IOException, PHPException {
		try {
			String s = in.readLine();

			if (s == null) {
				throw new IOException("Socket unexpectedly closed");
			}

			s = decode(s);
			if (s != null) {
				if ("__SYNTAX_ERROR__".equals(s)) {
					throw new SyntaxErrorException();
				}
				if (s.startsWith("__EXEC_ERROR__")) {
					throw new ExecutionErrorException(s.substring("__EXEC_ERROR__".length() + 1));
				}
			}
			return s;
		} catch (SocketTimeoutException e) {
			throw new PHPTimeoutException();
		}
	}

	public String getStdout() throws IOException {
		return stdout.ready() ? stdout.readLine() : null;
	}

	public String getStderr() throws IOException {
		return stderr.ready() ? stderr.readLine() : null;
	}

	public List<String> flushStdout() throws IOException {
		List<String> result = new ArrayList<String>();
		while(stdout.ready()) {
			result.add(stdout.readLine());
		}
		return result;
	}
	
	public List<String> flushStderr() throws IOException {
		List<String> result = new ArrayList<String>();
		while(stderr.ready()) {
			result.add(stderr.readLine());
		}
		return result;
	}
	
	public void flushConsoles() throws IOException {
		flushStderr();
		flushStdout();
	}
}