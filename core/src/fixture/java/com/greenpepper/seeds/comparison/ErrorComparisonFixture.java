package com.greenpepper.seeds.comparison;

import com.greenpepper.expectation.ShouldBe;
import com.greenpepper.util.ClassUtils;

public class ErrorComparisonFixture
{
    public String expectedValue;

    public Object systemValue;

    public boolean equal() throws Exception
    {
        return ShouldBe.literal(expectedValue).meets(systemValue);
    }

    public void setSystemValue(String value)
    {
        try
        {
            systemValue = ClassUtils.loadClass(value).newInstance();
        }
        catch (Exception e)
        {
            systemValue = value;
        }
    }
}
