package com.greenpepper.interpreter.flow.action;

import static org.hamcrest.Matchers.is;

import com.greenpepper.interpreter.flow.Row;
import com.greenpepper.interpreter.flow.RowSelector;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.Rows;
import com.greenpepper.util.TestCase;

public class ActionRowSelectorTest extends TestCase {

	RowSelector rowSelector;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		rowSelector = new ActionRowSelector(new PlainOldFixture(new Object()));
	}
	
	public void testThatItAlwaysSelectsAnActionRow(){
        Rows rows = Rows.parse(
                "[ anything ]"
        );

		Row row = rowSelector.select(rows);
		assertThat(row, is(ActionRow.class));
	}
	
}
