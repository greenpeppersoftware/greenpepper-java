package com.greenpepper.seeds.comparison;

import com.greenpepper.expectation.ShouldBe;

public class StringComparisonFixture
{
    public String expectedValue;

    public String systemValue;

    public boolean equal()
    {
        return ShouldBe.literal(expectedValue).meets(systemValue);
    }
}
