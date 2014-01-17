package com.greenpepper.runner;

import java.io.File;

import com.greenpepper.document.Document;
import com.greenpepper.document.GreenPepperInterpreterSelector;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.report.FileReportGenerator;
import com.greenpepper.repository.FileSystemRepository;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.util.TestCase;
import com.greenpepper.util.URIUtil;

public class DocumentRunnerTest extends TestCase
{
	private File specificationDirectory = null;

	@Override
	protected void setUp()
	{
		File baseDirectory = new File( URIUtil.decoded( getRessource( "/" ) ) );

		specificationDirectory = findSpecsDirectory( baseDirectory.getParentFile() );
		
		assertNotNull( specificationDirectory );
	}

	public void testCallsBackOnSystemUnderDevelopmentOnStartAndEndOfDocumentExecution()
    {
		DocumentRunner runner = new DocumentRunner();

        FakeSystemUnderDevelopment sud = new FakeSystemUnderDevelopment();

        runner.setInterpreterSelector( GreenPepperInterpreterSelector.class );
        runner.setSystemUnderDevelopment( sud );
        runner.setSections();
        runner.setReportGenerator( new FileReportGenerator( specificationDirectory ) );
        runner.setRepository( new FileSystemRepository( specificationDirectory ) );

		runner.run( "specs/ABankSample.html", "specs/ABankSample.html.out" );

        assertTrue( sud.onStartDocumentHasBeenCalled );
        assertTrue( sud.onEndDocumentHasBeenCalled );
    }

    private String getRessource(String name)
    {
        return DocumentRunnerTest.class.getResource(name).getPath();
    }

	private File findSpecsDirectory(File root)
	{
		String[] names = new String[] {"classes", "test-classes"};

		for (String name : names)
		{
			File dir = new File(root, name);

			if (new File(dir, "specs").exists())
			{
				return dir;
			}
		}

		return null;
	}

	private static class FakeSystemUnderDevelopment extends DefaultSystemUnderDevelopment
    {
        public boolean onStartDocumentHasBeenCalled = false;
        public boolean onEndDocumentHasBeenCalled = false;

        public void addImport(String packageName)
        {
        }

        public Fixture getFixture(String name, String... params) throws Throwable
        {
            return null;
        }

        public void onEndDocument(Document document)
        {
            onEndDocumentHasBeenCalled = true;
        }

        public void onStartDocument(Document document)
        {
            onStartDocumentHasBeenCalled = true;
        }
    }
}
