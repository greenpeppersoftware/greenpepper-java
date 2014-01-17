package com.greenpepper.samples.application.bank;

import com.greenpepper.samples.application.bank.AccountType;
import com.greenpepper.samples.application.bank.Money;
import com.greenpepper.samples.application.bank.WithdrawType;

public class WithdrawalFeesFixture 
{
	private AccountType accountType; 	 
	private WithdrawType withdrawType;
	
	public AccountType getAccountType() 
	{
		return accountType;
	}

	public void setAccountType(AccountType accountType) 
	{
		this.accountType = accountType;
	}

	public WithdrawType getWithdrawType() 
	{
		return withdrawType;
	}

	public void setWithdrawType(WithdrawType withdrawType) 
	{
		this.withdrawType = withdrawType;
	}

	public Money getFees() 
	{
		return accountType.feesFor(withdrawType);
	}
}
