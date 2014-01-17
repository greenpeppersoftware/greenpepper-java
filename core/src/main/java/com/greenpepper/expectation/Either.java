package com.greenpepper.expectation;

public class Either implements Collator
{
    private Expectation expectation;

    public Either(Expectation expectation)
    {
        this.expectation = expectation;
    }

    public Either or(Expectation other)
    {
        this.expectation = new OrExpectation( this.expectation, other );
        return this;
    }

	public Either negate()
	{
		this.expectation = new NotExpectation( this.expectation );
		return this;
	}

    public Expectation toExpectation()
    {
        return expectation;
    }
}
