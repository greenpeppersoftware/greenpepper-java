package com.greenpepper.seeds.comparison;

import com.greenpepper.TypeConversion;
import com.greenpepper.expectation.ShouldBe;

public class BooleanComparisonFixture
{
    public String expectedValue;

    public String systemValue;

    public boolean equal()
    {
        return ShouldBe.literal(expectedValue).meets(
                TypeConversion.parse(systemValue, Boolean.class));
    }
}
