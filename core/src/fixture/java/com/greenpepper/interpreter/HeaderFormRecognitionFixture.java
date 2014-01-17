package com.greenpepper.interpreter;

import com.greenpepper.interpreter.column.Column;
import com.greenpepper.interpreter.column.ExpectedColumn;
import com.greenpepper.interpreter.column.GivenColumn;
import com.greenpepper.interpreter.column.NullColumn;
import com.greenpepper.interpreter.column.RecalledColumn;
import com.greenpepper.interpreter.column.SavedColumn;

public class HeaderFormRecognitionFixture
{
    private String headerText;

    public String cellsWillBe() throws Exception
    {
        MockFixture mockFixture = new MockFixture();
        mockFixture.willRespondTo(headerText, null);

        Column col = HeaderForm.parse(headerText).selectColumn(mockFixture);
        if (col instanceof GivenColumn) return "given";
        else if (col instanceof ExpectedColumn) return "expected";
        else if (col instanceof NullColumn) return "ignore";
        else if (col instanceof SavedColumn) return "saved";
        else if (col instanceof RecalledColumn) return "recalled";
        else throw new Exception();
    }

    public void setHeaderText(String text)
    {
        if (text != null)
        {
            headerText = text;
        }
        else
        {
            headerText = "";
        }
    }
 }
