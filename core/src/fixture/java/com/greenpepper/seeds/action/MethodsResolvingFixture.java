package com.greenpepper.seeds.action;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;

public class MethodsResolvingFixture
{
    
    public int calledWithArity;
    public String method;

    public boolean hasMethodBeenCalled() throws Throwable
    {
        try
        {
            String[] params = new String[calledWithArity];
            for (int i = 0; i < calledWithArity; i ++) params[i] = "" + i;
            
            SystemUnderDevelopment sut = new DefaultSystemUnderDevelopment();
            Fixture fixture = sut.getFixture(Target.class.getName());            
            fixture.send(method).send(params);
            
            return calledWithArity == Target.lastArityOfCalledMethod;    
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    public static class Target
    {
        public static int lastArityOfCalledMethod = -1;

        public void MyMethodThatIsOverrideWithNoParametersAndTwoParameters()
        {
            lastArityOfCalledMethod = 0;
        }

        public void MyMethodThatIsOverrideWithNoParametersAndTwoParameters(int arg1, int arg2)
        {
            lastArityOfCalledMethod = 2;
        }
    }

}
