package com.greenpepper.samples.application.bank;

import com.google.inject.Inject;
import com.greenpepper.samples.application.bank.AccountType;
import com.greenpepper.samples.application.bank.Bank;
import com.greenpepper.samples.application.bank.Money;
import com.greenpepper.samples.application.bank.Owner;

public class AGroupOfCustomersFixture 
{
	public AccountType type;
	public String number, firstName, lastName;
	public Money balance;
	public Bank bank;
	
	@Inject
	public AGroupOfCustomersFixture(Bank bank)
	{
		this.bank= bank;
	}

	public void enterRow()
	{
		if(AccountType.SAVINGS == type)
			bank.openSavingsAccount(number, new Owner(firstName, lastName)).deposit(balance);
		
		else if(AccountType.CHECKING == type)
			bank.openCheckingAccount(number, new Owner(firstName, lastName)).deposit(balance);
	}
}
