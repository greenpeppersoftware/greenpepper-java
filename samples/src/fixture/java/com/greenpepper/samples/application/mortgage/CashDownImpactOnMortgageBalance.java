package com.greenpepper.samples.application.mortgage;

import com.greenpepper.samples.application.mortgage.MortgageBalanceCalculator;

public class CashDownImpactOnMortgageBalance
{
    private float mortgageAllowance;
    private float cashDown;
    
    public float mortgageBalance()
    {
        return MortgageBalanceCalculator.evaluate(mortgageAllowance, cashDown);
    }
    
    public void setCashDown(float cashDown)
    {
        this.cashDown = cashDown;
    }
    
    public void setMortgageAllowance(float mortgageAllowance)
    {
        this.mortgageAllowance = mortgageAllowance;
    }
}
