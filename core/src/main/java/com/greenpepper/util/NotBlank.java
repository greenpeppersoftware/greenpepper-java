package com.greenpepper.util;

public class NotBlank implements CollectionUtil.Predicate<String>
{
    public boolean isVerifiedBy(String element)
    {
        return !StringUtil.isBlank( element );
    }
}
