package com.greenpepper.extensions.selenium;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class SeleniumRemoteControlFixture
{
	private static final String DEFAULT_WAIT_TIMEOUT = "30000";
	
    private Selenium selenium;
    
    private Map<String, String> variables = new HashMap<String, String>();
    
    public SeleniumRemoteControlFixture()
    {
    }

    public SeleniumRemoteControlFixture(Selenium selenium)
    {
        this.selenium = selenium;
    }

    @com.greenpepper.reflect.SystemUnderTest
    public Selenium getSystemUnderTest()
    {
        return selenium;
    }
    
    public boolean startBrowserWithSeleniumConsoleOnAtPortAndScriptsAt(String browserName, String serverHost, int serverPort, String browserUrl)
	{
        selenium = new DefaultSelenium(serverHost, serverPort, "*" + browserName, browserUrl);
        selenium.start();
        return true;
	}
    
    public boolean userOpensUrl(String url)
    {
    	selenium.open(url);
    	return true;
    }
    
    public boolean shutdownBrowser()
    {
    	selenium.stop();
    	return true;
    }
    
    public String text(String locator)
    {
    	return selenium.getText(locator);
    }
    
    public String value(String locator)
    {
    	return selenium.getValue(locator);
    }

    
    // =====================================
    // Selenium commands
    // =====================================
    
    
    public boolean assertAlert(String alert)
    {
    	return selenium.getAlert().equals(alert);
    }
    
    public boolean assertAlertPresent()
    {
    	return selenium.isAlertPresent();
    }
    
    public boolean assertAlertNotPresent()
    {
    	return !assertAlertPresent();
    }

    public boolean assertElementPresent (String text)
    {
        return selenium.isElementPresent(text);
    }

    public boolean assertElementNotPresent (String text)
    {
        return !assertElementPresent(text);
    }

    public boolean assertExpression(String variableNameExpression, String expectedValue)
    {
    	Expression expression = new Expression(variableNameExpression);
    	return expectedValue.equals(this.variables.get(expression.parseVariableName()));
    }
    
    public void assertFailureOnNext()
    {
    	
    }
    
	public boolean assertTextPresent (String text)
    {
        return selenium.isTextPresent(text);
    }

    public boolean assertTextNotPresent (String text)
    {
        return !assertTextPresent(text);
    }

    public boolean assertTitle (String title)
    {
        return title.equals(selenium.getTitle());
    }

    public boolean assertNotTitle(String title)
    {
    	return !assertTitle(title);
    }
    
    public void clickAndWait (String element)
	{
	    selenium.click(element);
	    selenium.waitForPageToLoad(DEFAULT_WAIT_TIMEOUT);
	}
    
    public void storeAlert(String variableName)
    {
    	this.variables.put(variableName, selenium.getAlert());
    }
    
    public boolean verifyAlert(String alert)
    {
    	return assertAlert(alert);
    }
    
    public boolean verifyAlertPresent()
    {
    	return assertAlertPresent();
    }
    
    public boolean verifyAlertNotPresent()
    {
    	return !verifyAlertPresent();
    }

    public boolean verifyElementPresent (String text)
    {
        return assertElementPresent(text);
    }

    public boolean verifyElementNotPresent (String text)
    {
        return !verifyElementPresent(text);
    }

    public boolean verifyExpression(String expression, String expectedValue)
    {
    	return assertExpression(expression, expectedValue);
    }

    public boolean verifyTextPresent (String text)
    {
        return assertTextPresent(text);
    }

    public boolean verifyTextNotPresent (String text)
    {
        return !verifyTextPresent(text);
    }
    
    public boolean verifyTitle(String title)
    {
    	return assertTitle(title);
    }
    
    public boolean verifyNotTitle(String title)
    {
    	return !verifyTitle(title);
    }
    
    public void waitForAlertPresent()
    {
    	selenium.waitForCondition("selenium.browserbot.hasAlerts()", DEFAULT_WAIT_TIMEOUT);
    }

}
