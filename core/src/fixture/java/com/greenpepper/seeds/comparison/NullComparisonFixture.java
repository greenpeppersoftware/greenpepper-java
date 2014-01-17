package com.greenpepper.seeds.comparison;

import com.greenpepper.expectation.ShouldBe;

public class NullComparisonFixture
{
    public String expectedValue;

    public String actualValue;

    public boolean equal()
    {
        return ShouldBe.literal(expectedValue).meets(actualValue);
    }

    public void setSystemValue(String value)
    {
        actualValue = value.equals("null") ? null : value;
    }
}
