package com.greenpepper.extensions.spring.beans;

public class CalculatorWiredBean
{
    CalculatorBean calculator;
    
    public CalculatorWiredBean(CalculatorBean calculator)
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