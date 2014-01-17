/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
 */
package com.greenpepper.extensions.guice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneBookEntry
{
	private final static Pattern entryPattern =
		Pattern.compile("([\\w- ]+)\\,\\s?([\\w- ]+)\\,\\s?(\\(\\d{3}\\)\\s?\\d{3}-\\d{4})");

	private String firstName;
	private String lastName;
	private String number;

	public PhoneBookEntry(String firstName, String lastName, String number)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.number = number;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}
	
	public static PhoneBookEntry parse(String value)
	{
		Matcher matcher = entryPattern.matcher(value);

		if (matcher.find())
		{
			String firstName = matcher.group(1);
			String lastName = matcher.group(2);
			String number = matcher.group(3);

			return new PhoneBookEntry(firstName, lastName, number);
		}

		// Fallback : try 'naive' solution
		String[] split = value.split(",");
		return new PhoneBookEntry(split[0].trim(), split[1].trim(), split[2].trim());
	}	
}
