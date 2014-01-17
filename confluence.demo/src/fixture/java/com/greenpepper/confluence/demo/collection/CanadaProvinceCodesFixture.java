/**
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
package com.greenpepper.confluence.demo.collection;

import java.util.Set;

import com.greenpepper.reflect.CollectionProvider;
import com.greenpepper.reflect.EnterRow;

public class CanadaProvinceCodesFixture
{
	private static final Country country= new Country("CANADA");
	
	private String name;
	private String code;

	public CanadaProvinceCodesFixture()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}
	
	public void insertProvinceWithCode(String name, String code)
	{
		country.addProvince(name, code);
	}

	@EnterRow
	public void insertProvince()
	{
		country.addProvince(name, code);
	}

	@CollectionProvider
	public Set<Province> getListOfProvinces()
	{
		return country.provinces();
	}
}