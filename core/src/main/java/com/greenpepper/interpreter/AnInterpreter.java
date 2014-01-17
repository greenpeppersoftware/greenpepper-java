package com.greenpepper.interpreter;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;

public class AnInterpreter extends RuleForInterpreter
{
    public AnInterpreter( SystemUnderDevelopment systemUnderDevelopment )
    {
        this(new PlainOldFixture(new NullFixture()));
    }

    public AnInterpreter(Fixture fixture)
    {
        super(fixture);
    }

    public static class NullFixture
    {
        public String fixtureName()
        {
            return null;
        }

        public String fixtureParameters()
        {
            return null;
        }
    }
}
