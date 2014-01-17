package com.greenpepper.extensions.selenium;

import junit.framework.TestCase;
import junit.framework.Assert;

public class ExpressionTest extends TestCase {

	public void testThatParsingANullExpressionAsVariableNameReturnsNull(){
		Expression expression = new Expression(null);
		assertNull(expression.parseVariableName());
	}
	
	public void testThatParsingAnExpressionThatDoesNotStartWithADollarSignThrowsAnException(){
		Expression expression = new Expression("some expression");
		try{
			expression.parseVariableName();
			fail("Should have thrown an exception.");
		}
		catch(Exception e) {
			assertTrue(true);
		}
	}
	
	public void testThatParsingAVariableNameExpressionIgnoresWhitespacesOutsideVariableName(){
		Expression expression = new Expression(" ${ myVar }");
		Assert.assertEquals("myVar", expression.parseVariableName());
		
		expression = new Expression("${myVar}");
		Assert.assertEquals("myVar", expression.parseVariableName());
	}
		
}
