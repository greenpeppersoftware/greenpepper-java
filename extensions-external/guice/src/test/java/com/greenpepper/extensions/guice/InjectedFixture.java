package com.greenpepper.extensions.guice;

import com.google.inject.Inject;

public class InjectedFixture 
{
    public String msg;
	private Foo foo;
	
	public InjectedFixture() 
	{
		this.foo = new Foo();
	}

	@Inject
	public InjectedFixture(Foo foo) 
	{
		this.foo = foo;
	}
	
    public String echo()
    {
        return msg;
    }

    public Foo getFoo() {
		return foo;
	}

	public static class Foo 
	{
		public String getMsg() 
		{
			return "We love Guice.";
		} 	
	}
}


