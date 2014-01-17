package com.greenpepper.report;

import java.io.StringWriter;

import junit.framework.TestCase;

import com.greenpepper.TextExample;
import com.greenpepper.document.Document;

public class PlainReportTest extends TestCase
{

    public void testCanPrintGeneratedReport() throws Exception
    {

        StringWriter out = new StringWriter();
        PlainReport report = new PlainReport("test");

        String lorem = "Lorem ipsum";

        report.generate(Document.text(new TextExample(lorem)));
        report.printTo(out);

        assertEquals(lorem, out.toString());
    }
}
