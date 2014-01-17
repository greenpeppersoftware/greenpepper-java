/*
 * Copyright (c) 2006 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 /*
 * Copyright (c) 2006 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */

package com.greenpepper.runner;

import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;

import junit.framework.TestCase;

import com.greenpepper.AlternateCalculator;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.report.XmlReport;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.util.ExceptionUtils;
import com.greenpepper.util.IOUtil;

public class CommandLineRunnerTest extends TestCase
{
    private File outputDir;

    protected void setUp() throws Exception
    {
        createOutputDirectory();
    }

    private void createOutputDirectory()
    {
        outputDir = new File(System.getProperty("java.io.tmpdir"), "specs");
    }

    protected void tearDown() throws Exception
    {
        deleteOutputDirectory();
    }

    private void deleteOutputDirectory()
    {
        if (outputDir != null) IOUtil.deleteDirectoryTree( outputDir );
    }

    public void testCanRunASingleSpecificationAndProduceAReportFile() throws Exception
    {
        String  input = getResourcePath("/specs/ABankSample.html");
        File outputFile = outputFile("report.html");

        new CommandLineRunner().run(input, outputFile.getAbsolutePath());

        assertFile(outputFile);
    }

    public void testCanGenerateAUniqueReportFileNameFromSpecificationName() throws Exception
    {
        String input = getResourcePath("/specs/ABankSample.html");
        File expectedOutputFile = outputFile( "ABankSample.html.xml" );

        new CommandLineRunner().run("--xml", "-o", outputDir.getAbsolutePath(), input);
        assertFile(expectedOutputFile);
    }

    public void testCanRunASuiteOfSpecificationsAndGenerateReports() throws Exception
    {
        String input = getResourcePath("/specs");
        new CommandLineRunner().run("-s", input, outputDir.getAbsolutePath());

        assertTrue(outputDir.isDirectory());
        assertEquals(3, outputDir.listFiles().length);
    }

    private void assertFile( File file )
    {
        assertTrue( file.exists() );
        long length = file.length();
        assertTrue( length > 0 );
    }

    private File outputFile(String fileName) {
        return new File( outputDir, fileName);
    }

    private String getResourcePath(String name) throws URISyntaxException
    {
        return CommandLineRunnerTest.class.getResource(name).getPath();
    }

    public void testThatFixtureFactoryCanBeSpecifiedAlongWithArguments() throws Exception
    {
        File reportFile = outputFile( "report.xml" );
        String[] args = new String[]{
            "-f", CustomSystemUnderDevelopment.class.getName() + ";param1;param2",
            "--xml",
            getResourcePath("/WithACustomFixtureFactory.html"),
            reportFile.getAbsolutePath()
        };

        new CommandLineRunner().run( args );
        XmlReport parser = XmlReport.parse( new FileReader( reportFile ) );
        assertEquals( 1, parser.getSuccess( 0 ) );
        assertEquals( 1, parser.getFailure( 0 ) );
    }

    public void testThatDebugModeAllowToSeeWholeStackTrace() throws Exception {

        String input = getResourcePath("/specs/ABankSample.html");
        File outputFile = outputFile("report.html");

        CommandLineRunner commandLineRunner = new CommandLineRunner();
        commandLineRunner.run("--debug", input,outputFile.getAbsolutePath());

        try
        {
            throw new Exception(new Throwable(""));
        }
        catch(Exception e)
        {
            assertTrue(countLines(ExceptionUtils.stackTrace(e.getCause(), "\n", 2)) > 2+1);
        }

    }

    private int countLines(String val)
    {
        int result = 0;

        while (val.indexOf("\n") > -1)
        {
            result ++;
            val = val.substring(val.indexOf("\n") + 1);
        }

        return result;
    }

    public static class CustomSystemUnderDevelopment extends DefaultSystemUnderDevelopment
    {
        public CustomSystemUnderDevelopment( String... params )
        {
        }

        public Fixture getFixture( String name, String... params ) throws Exception
        {
            return new PlainOldFixture( classForName( name ).newInstance() );
        }

        public void addImport( String packageName )
        {
        }

        public Class classForName( String fixtureName ) throws Exception
        {
            if (fixtureName.equals( "MyFixture" )) return AlternateCalculator.class;
            else throw new Exception();
        }
    }
}
