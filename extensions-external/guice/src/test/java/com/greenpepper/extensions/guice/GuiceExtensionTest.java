package com.greenpepper.extensions.guice;

import java.io.File;
import java.net.URISyntaxException;

import com.greenpepper.runner.CommandLineRunner;
import com.greenpepper.util.IOUtil;
import com.greenpepper.util.TestCase;

public class GuiceExtensionTest extends TestCase {

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

    public void testShouldAddSupportForInjectingDependenciesInFixturesUsingGuice() throws Exception
    {
    	String input = getResourcePath( "guice.html" );
        File outputFile = new File( outputDir, "report.html" );

        new CommandLineRunner().run("-f", "com.greenpepper.extensions.guice.GuiceSystemUnderDevelopment", input, outputFile.getAbsolutePath());

        assertFile(outputFile);
    }

    private String getResourcePath(String name) throws URISyntaxException
    {
        return GuiceExtensionTest.class.getResource(name).getPath();
    }

    private void assertFile( File file )
    {
        assertTrue( file.exists() );
        long length = file.length();
        assertTrue( length > 0 );
    }
}
