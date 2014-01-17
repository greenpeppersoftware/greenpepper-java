package com.greenpepper.seeds.comparison;

import com.greenpepper.expectation.ShouldBe;
import com.greenpepper.util.ClassUtils;

public class TypeComparisonFixture
{
    public String expectedValue;

    public String systemValueType;

    public boolean equal() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException
    {
        return ShouldBe.instanceOf(ClassUtils.loadClass(expectedValue)).meets(
                ClassUtils.loadClass(systemValueType).newInstance());
    }
}
