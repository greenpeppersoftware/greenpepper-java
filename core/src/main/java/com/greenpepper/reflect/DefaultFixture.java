package com.greenpepper.reflect;

import com.greenpepper.ogn.ObjectGraphNavigationFixture;

public class DefaultFixture implements Fixture
{
    private final Fixture delegate;

    public DefaultFixture( Object target )
    {
        delegate = new ObjectGraphNavigationFixture(target);
    }

    public Fixture fixtureFor( Object target )
    {
        return new DefaultFixture( target );
    }

    public Object getTarget()
    {
        return delegate.getTarget();
    }

    public boolean canSend( String message )
    {
        return delegate.canSend( message );
    }

    public boolean canCheck( String message )
    {
        return delegate.canCheck( message );
    }

    public Message check( String message ) throws NoSuchMessageException
    {
        return delegate.check( message );
    }

    public Message send( String message ) throws NoSuchMessageException
    {
        return delegate.send( message );
    }
}
