package com.greenpepper.extensions.spring.pojos;

import com.greenpepper.extensions.spring.beans.CalculatorBean;

public class ExternalCalculatorBeanDependent
{
    CalculatorBean calculator;
    
    public ExternalCalculatorBeanDependent(CalculatorBean calculator)
    {
        this.calculator = calculator;
    }
    
    public void setOp1(int op1)
    {
        calculator.op1 = op1;
    }
    
    public void setOp2(int op2)
    {
        calculator.op2 = op2;
    }
    
    public int sum()
    {
        return calculator.sum();
    }
}    