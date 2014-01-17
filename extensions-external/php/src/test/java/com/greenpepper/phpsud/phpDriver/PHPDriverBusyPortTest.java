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

import java.net.InetAddress;
import java.net.ServerSocket;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Bertrand Paquet
 */
public class PHPDriverBusyPortTest {

	@Test
	public void checkBusyPort() throws Exception {
		ServerSocket serverSocket = new ServerSocket(8888, 2, InetAddress.getByName("127.0.0.1"));
		Assert.assertTrue(serverSocket.isBound());
		PHPDriver php = new PHPDriver(5);
		Assert.assertEquals("Ceci est un test", php.execGet("\"Ceci est un test\""));
		serverSocket.close();
	}
	
}
