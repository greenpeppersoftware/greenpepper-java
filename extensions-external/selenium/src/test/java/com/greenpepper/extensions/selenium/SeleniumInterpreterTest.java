package com.greenpepper.extensions.selenium;

import com.greenpepper.Specification;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.util.Tables;
import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.HttpCommandProcessor;
import junit.framework.TestCase;

public class SeleniumInterpreterTest extends TestCase {

	private SeleniumInterpreter interpreter;
	private SeleniumFixture fixture;
	private CommandProcessor processor;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

//		processor = new HttpCommandProcessor("localhost", 4343, "*firefox", "http://www.google.com");
//		fixture = new SeleniumFixture(processor);
//		interpreter = new SeleniumInterpreter(fixture);
        interpreter  = new SeleniumInterpreter(null);
	}

       public void testEmptySoJUnitWontComplain() throws Throwable {

        }


	public void xtestCanInterpretASeleniumSpecification(){
		interpreter.interpret(specificationFor(
				"[selenium][selenium]\n" +
		        "[assertTitle][Google]"));
	}

	private Specification specificationFor(String examples) {
		return specificationFor( tablesFor(examples) );
	}

	private Specification specificationFor(Tables tables) {
		return new FakeSpecification( tables );
	}

	private Tables tablesFor(String examples) {
		return Tables.parse(examples);
	}

}
