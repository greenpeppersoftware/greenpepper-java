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
 */
package com.greenpepper.converter;

import java.util.EnumSet;
import java.util.Set;

/**
 * {@link Enum} converter. The <code>toString</code> result of an enum type will be matched against the name value.
 * </p>
 * Ex. :
 * <code><pre>
  	public enum WithdrawType
	{
		ATM("ATM"),
		INTERACT("Interact"),
		PERSONAL_CHECK("Personal Check");

		private final String id;

		private WithdrawType(String id)
		{
			this.id = id;
		}

 		@Override
		public String toString()
		{
			return id;
		}
	}
 *	</pre></code> 
 */
@SuppressWarnings("unchecked")
public class EnumConverter extends AbstractTypeConverter
{
	protected Object doConvert(String value)
	{
        throw new UnsupportedOperationException( "Please call the doConvert(String, Class) method.");
	}

	@Override
	protected Object doConvert(String value, Class type)
	{
		Set allOf = EnumSet.allOf(type);

		for (Object enumValue : allOf)
		{
			if (value.equalsIgnoreCase(String.valueOf(enumValue)))
			{
				return enumValue;
			}
		}

		throw new IllegalArgumentException("Conversion failed : '" + value + "' for type '" + type + "'");
	}

	public boolean canConvertTo(Class type)
	{
		return Enum.class.isAssignableFrom(type);
	}
}