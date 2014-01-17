package com.greenpepper.samples.application.mortgage;

import junit.framework.TestCase;
import com.greenpepper.samples.application.mortgage.MortgageBalanceCalculator;

public class MortgageBalanceCalculatorTest extends TestCase
{
    private float commercialValue;
    private float purchasedPrice;
    private float mortgageAllowance;
    private float minCashDown;
    
    public void setUp()
    {
        purchasedPrice = 100000;
        commercialValue = 100000;
        mortgageAllowance = 100000;
        minCashDown = mortgageAllowance * MortgageBalanceCalculator.MIN_CASH_DOWN_RATIO;
    }
    
    public void testThatTheMortgageAllowanceIsToppedByTheCommercialValue()
    {
        float expetedMortgageAllowance = Math.min(commercialValue * MortgageBalanceCalculator.MAX_MORTGAGE_RATIO, purchasedPrice);
        assertEquals(expetedMortgageAllowance, MortgageBalanceCalculator.getMortgageAllowance(commercialValue, purchasedPrice));
    }
    
    public void testThatTheCashDownIsInsufficient()
    {        
        float insufficientCashDown = minCashDown - 1;        
        assertTrue(MortgageBalanceCalculator.isInsufficient(mortgageAllowance, insufficientCashDown));
        
        float sufficientCashDown = minCashDown + 1;      
        assertFalse(MortgageBalanceCalculator.isInsufficient(mortgageAllowance, sufficientCashDown));
    }

    public void testThatTheMortgageBalanceIsIncreasedIfTheCashDownIsInsufficient()
    {
        float insufficientCashDown = minCashDown - 1;  
        float balanceBeforeFee = mortgageAllowance - insufficientCashDown;
        float expetedBalance = balanceBeforeFee * (1 + MortgageBalanceCalculator.MIN_CASH_DOWN_FEE_RATIO);
        
        assertEquals(expetedBalance, MortgageBalanceCalculator.evaluate(mortgageAllowance, insufficientCashDown));
    }

    public void testThatTheMortgageBalanceIsNotImpactedIfTheCashDownIsSufficient()
    {
        float insufficientCashDown = minCashDown + 1;  
        float expetedBalance = mortgageAllowance - insufficientCashDown;
        
        assertEquals(expetedBalance, MortgageBalanceCalculator.evaluate(mortgageAllowance, insufficientCashDown));
    }
    
    public void testThatTheFeesAreAddedToTheMortgageBalanceInTheBankPayYourBillsOption()
    {
        float bienvenueTax = 1500;
        float bigNotaryFees = MortgageBalanceCalculator.MAX_NOTARY_FEE + 1;
        float expetedBalance = mortgageAllowance + MortgageBalanceCalculator.MAX_NOTARY_FEE + bienvenueTax;
        assertEquals(MortgageBalanceCalculator.MAX_NOTARY_FEE, MortgageBalanceCalculator.getPayedNotaryFee(bigNotaryFees));
        assertEquals(expetedBalance, MortgageBalanceCalculator.reevaluateBankPayYourBillsOption(mortgageAllowance, bigNotaryFees, bienvenueTax));

        float smallNotaryFees = MortgageBalanceCalculator.MAX_NOTARY_FEE - 1;
        expetedBalance = mortgageAllowance + smallNotaryFees + bienvenueTax;
        assertEquals(smallNotaryFees, MortgageBalanceCalculator.getPayedNotaryFee(smallNotaryFees));
        assertEquals(expetedBalance, MortgageBalanceCalculator.reevaluateBankPayYourBillsOption(mortgageAllowance, smallNotaryFees, bienvenueTax));
    }
}
