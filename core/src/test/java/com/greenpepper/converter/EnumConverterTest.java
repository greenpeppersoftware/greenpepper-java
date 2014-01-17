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

public class EnumConverterTest
		extends AbstractTypeConverterTest
{
	public EnumConverterTest()
	{
		super(new EnumConverter());
	}

	public void testConversionOfValue()
	{
		assertEquals(WithdrawType.ATM, converter.parse("Atm", WithdrawType.class));
		assertEquals(WithdrawType.PERSONAL_CHECK, converter.parse("Personal Check", WithdrawType.class));
		assertEquals(WithdrawType.INTERACT, converter.parse("Interact", WithdrawType.class));
		assertEquals(WithdrawType.OTHERS, converter.parse("OTHERS", WithdrawType.class));
		assertEquals(WithdrawType.CreditCard, converter.parse("Credit Card", WithdrawType.class));
	}

	public void testEnumHasNoValueAssociated()
	{
		try
		{
			converter.parse("Z", WithdrawType.class);

			fail();
		}
		catch (IllegalArgumentException ex)
		{

		}
	}

	public void testSupportsEnumTypes()
	{
		assertTrue(converter.canConvertTo(WithdrawType.class));
		assertFalse(converter.canConvertTo(Boolean.class));
	}

	public enum WithdrawType
	{
		ATM("ATM"),
		INTERACT("Interact"),
		PERSONAL_CHECK("Personal Check"),
		OTHERS("Others"),
		CreditCard("Credit Card");

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
}
