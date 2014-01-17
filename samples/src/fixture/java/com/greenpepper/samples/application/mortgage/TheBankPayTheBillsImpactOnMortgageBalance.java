package com.greenpepper.samples.application.mortgage;

import com.greenpepper.samples.application.mortgage.MortgageBalanceCalculator;

public class TheBankPayTheBillsImpactOnMortgageBalance
{
    private float initialMortgageBalance;
    private float notaryFees;
    private float bienvenueTax;
    
    public float getMortgageBalance()
    {
        return MortgageBalanceCalculator.reevaluateBankPayYourBillsOption(initialMortgageBalance, notaryFees, bienvenueTax);
    }

    public void setBienvenueTax(float bienvenueTax)
    {
        this.bienvenueTax = bienvenueTax;
    }

    public void setInitialMortgageBalance(float initialMortgageBalance)
    {
        this.initialMortgageBalance = initialMortgageBalance;
    }

    public void setNotaryFees(float notaryFees)
    {
        this.notaryFees = notaryFees;
    }
}
