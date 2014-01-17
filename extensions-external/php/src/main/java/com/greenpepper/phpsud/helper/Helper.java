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

/**
 * @author Bertrand Paquet
 */
public class Helper {

	public static String formatParamList(String ... params) {
		StringBuilder s = new StringBuilder();
		for(String p : params) {
			if (s.length() != 0) {
				s.append(", ");
			}
			s.append(p);
		}
		return s.toString();
	}
	
	public static String formatProcedureName(String s) {
		StringBuilder res = new StringBuilder();
		boolean one = true;
		for(String s1 : s.split(" ")) {
			if (one) {
				one = false;
				res.append(s1);
			}
			else {
				res.append(firstUpperCase(s1));
			}
		}
		return res.toString();
	}
	
	public static String firstUpperCase(String s) {
		String first = s.length() > 0 ? s.substring(0, 1).toUpperCase() : "";
		String second = s.length() > 1 ? s.substring(1).toLowerCase() : "";
		return first + second;
	}
	
	public static String purge(String s) {
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < s.length(); i ++) {
			char z = s.charAt(i);
			if (z < 256) {
				out.append(z);
			}			
		}
		return out.toString();
	}
	
	public static String [] parseArray(String s) {
		// a:2:{i:0;s:12:"PHPSUD_OBJ_2";i:1;s:12:"PHPSUD_OBJ_3";}
		String [] t = s.split(":");
		int z = Integer.parseInt(t[1]);
		String [] res = new String[z];
		for(int i = 0; i < z; i ++) {
			 String a = t[5 + 3*i].split(";")[0];
			 res[i] = a.substring(1).substring(0, a.length() - 2);
		}
		return res;
	}

}
