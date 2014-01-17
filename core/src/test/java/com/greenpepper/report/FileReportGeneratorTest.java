package com.greenpepper.report;

import java.io.File;

import junit.framework.TestCase;

public class FileReportGeneratorTest extends TestCase {
	private static String tempDirectory = System.getProperty("java.io.tmpdir");
	
	//Related to bug GP-769
	public void testEscapeDoubleQuoteCharactersWhenWrittingFile()
	{
		try
		{
			PlainReport report = new PlainReport("GreenPepper Confluence_GREENPEPPER_Action Access Resolution \"quote\"_report.xml");
			FileReportGenerator reportGenerator = new FileReportGenerator(new File(tempDirectory));
			reportGenerator.closeReport(report);
		} catch(Exception e)
		{
			fail("Unable to write the report on file system : " + e.getMessage());
		}
	}
}
