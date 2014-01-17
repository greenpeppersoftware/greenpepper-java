package com.greenpepper.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.greenpepper.util.Introspector;

import static com.greenpepper.util.NameUtils.toJavaIdentifierForm;

public abstract class AbstractFixture implements Fixture
{
    protected final Object target;

    public AbstractFixture(Object target)
    {
        this.target = target;
    }

    public Object getTarget()
    {
        return target;
    }

    public boolean canSend(String message)
    {
        return getSendMessage(message) != null;
    }

    public boolean canCheck(String message)
    {
        return getCheckMessage(message) != null;
    }

    public Message check(String message) throws NoSuchMessageException
    {
        Message check = getCheckMessage(message);
        if (check == null) throw new NoSuchMessageException(message);

        return check;
    }

    public Message send(String message) throws NoSuchMessageException
    {
        Message send = getSendMessage(message);
        if (send == null) throw new NoSuchMessageException(message);

        return send;
    }

    protected abstract Message getCheckMessage(String name);

    protected abstract Message getSendMessage(String name);

    protected Method getSetter(Class type, String name)
    {
        return introspector(type).getSetter( toJavaIdentifierForm(name));
    }

    protected Field getField(Class type, String name)
    {
        return introspector(type).getField( toJavaIdentifierForm(name));
    }

    protected List<Method> getMethods(Class type, String name)
    {
        return introspector(type).getMethods( toJavaIdentifierForm(name));
    }

    protected Method getGetter(Class type, String name)
    {
        return introspector(type).getGetter( toJavaIdentifierForm(name));
    }

    protected Introspector introspector(Class type)
    {
        return Introspector.ignoringCase(type);
    }

    protected Object getSystemUnderTest()
    {
        for (Method method : target.getClass().getMethods())
        {
            if (method.isAnnotationPresent(SystemUnderTest.class))
            {
                return invoke(method);
            }
        }

        Method m = getGetter( target.getClass(), "systemUnderTest" );
        return m != null ? invoke( m ) : null;
    }

    protected Object invoke(Method method)
    {
        try
        {
            return method.invoke(target);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
