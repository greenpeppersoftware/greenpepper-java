package com.greenpepper.confluence.demo.bank;

import com.greenpepper.converter.AbstractTypeConverter;

public class OwnerTypeConverter
		extends AbstractTypeConverter {

	@SuppressWarnings("unchecked")
	public boolean canConvertTo(Class type)
    {
		return Owner.class.isAssignableFrom(type);
	}

	protected Object doConvert(String value)
    {
		String[] names = value.split("\\s");
		return new Owner(names[0], names.length > 1 ? names[1] : null);
	}
}