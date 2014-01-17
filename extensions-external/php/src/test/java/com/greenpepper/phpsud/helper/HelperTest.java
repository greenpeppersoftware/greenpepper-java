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
package com.greenpepper.phpsud.helper;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Bertrand Paquet
 */
public class HelperTest {
	
	@Test
	public void testformatProcedureName() {
		Assert.assertEquals("x", Helper.formatProcedureName("x"));
		Assert.assertEquals("xx", Helper.formatProcedureName("xx"));
		Assert.assertEquals("xxAa", Helper.formatProcedureName("xx aa"));
		Assert.assertEquals("xA", Helper.formatProcedureName("x a"));
	}
	
	@Test
	public void testformatParams() {
		Assert.assertEquals("", Helper.formatParamList());
		Assert.assertEquals("a", Helper.formatParamList("a"));
		Assert.assertEquals("a, b", Helper.formatParamList("a", "b"));
	}

	@Test
	public void testPurge() {
		char [] t = {' ', 'a', 'b', ' ', 65533};
		String s = new String(t);
		Assert.assertEquals(" ab ", Helper.purge(s));
	}

}
