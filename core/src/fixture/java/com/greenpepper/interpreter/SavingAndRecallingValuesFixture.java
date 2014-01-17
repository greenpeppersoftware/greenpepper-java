package com.greenpepper.interpreter;

import com.greenpepper.TypeConversion;
import com.greenpepper.Variables;
import com.greenpepper.interpreter.column.Column;
import com.greenpepper.interpreter.column.SavedColumn;
import com.greenpepper.util.AnnotationUtil;
import com.greenpepper.util.Cells;

public class SavingAndRecallingValuesFixture
{
    public static Variables variables = new Variables();

    public String cellText;
    public String outputFromSystemUnderDevelopment;

    public Object annotation() throws Exception
	{
		Cells cell = new Cells(cellText);

		MockFixture mockFixture = new MockFixture();
		mockFixture.willRespondTo("outputFromSystemUnderDevelopment", outputFromSystemUnderDevelopment);

		Column col = new SavedColumn(mockFixture.check("outputFromSystemUnderDevelopment"));
        col.bindTo( variables );
        col.doCell(cell);

		return AnnotationUtil.getAnnotationOnCell(cell);
	}

    public String inputToSystemUnderDevelopment()
    {
        return TypeConversion.toString( variables.getVariable( cellText ) );       
    }

    public void setCellDisplaysAs( String symbol )
    {
    }

    public String getCellDisplaysAs()
    {
        return outputFromSystemUnderDevelopment;
    }
}
