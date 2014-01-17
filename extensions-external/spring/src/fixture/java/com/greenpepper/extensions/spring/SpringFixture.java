package com.greenpepper.extensions.spring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.greenpepper.Assertions;
import com.greenpepper.Example;
import com.greenpepper.annotation.Annotation;
import com.greenpepper.document.Document;
import com.greenpepper.document.GreenPepperInterpreterSelector;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.Tables;

public class SpringFixture
{
    private static final String POJOS_PACKAGE_NAME = "com.greenpepper.extensions.spring.pojos";
    
	private static final String GREATER_THAN_HTML_ESCAPE_CODE = "&#62;";
	private static final String LESS_THAN_HTML_ESCAPE_CODE = "&#60;";
	
	private static final String APPLICATION_CONTEXT_FILE_PREFIX = "applicationContext";
    private static final String APPLICATION_CONTEXT_FILE_SUFFIX = ".xml";
    
    private static final String XML_HEADER = "<!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN 2.0//EN\"\n" +
                                             "        \"http://www.springframework.org/dtd/spring-beans-2.0.dtd\">\n";

    private String applicationContextContent;
    public Tables tableInGreenpepperFormat;
    
    public String getApplicationContextContent()
    {
    	return applicationContextContent;
    }
    
    public void setApplicationContextContent(String content)
    {
		applicationContextContent = content.replaceAll(LESS_THAN_HTML_ESCAPE_CODE, "<")
										.replaceAll(GREATER_THAN_HTML_ESCAPE_CODE, ">");
    }

    public Tables afterExecution() throws Throwable
    {
        SystemUnderDevelopment systemUnderDevelopment = createSpringSystemUnderDevelopment();
        
        Document document = fakeDocument();

        document.execute(new GreenPepperInterpreterSelector(systemUnderDevelopment));

        appendAnnotationText(tableInGreenpepperFormat);
        
        return tableInGreenpepperFormat;
    }
    
    private void appendAnnotationText(Example example)
    {
    	annotate(example);
    	
    	if (example.hasChild())
    	{
	        for (Example rows : example.firstChild())
	        {
	        	if (rows.hasChild())
	        	{
	        		for (Example cells : rows.firstChild())
	        		{
	        			appendAnnotationText(cells);
	        		}
	        	}
	        }
    	}
    }

    private void annotate(Example example)
    {
        Annotation ann = Assertions.readAnnotation(example);
        
        if (ann != null)
        {
        	example.setContent(example.getContent() + " " + ann.getClass().getSimpleName());
        }
    }
    
    private Document fakeDocument()
    {
    	return Document.html(tableInGreenpepperFormat);
    }

    private SystemUnderDevelopment createSpringSystemUnderDevelopment()
    {
        String applicationContextFilePath = createApplicationContextFile();

        SystemUnderDevelopment sud = new SpringSystemUnderDevelopment(applicationContextFilePath);
        
        sud.addImport(POJOS_PACKAGE_NAME);
        
        return sud;
    }

    private String createApplicationContextFile()
    {
        try
        {
            File applicationContextFile = File.createTempFile(APPLICATION_CONTEXT_FILE_PREFIX, APPLICATION_CONTEXT_FILE_SUFFIX);
            applicationContextFile.deleteOnExit();

            OutputStream outputStream = new FileOutputStream(applicationContextFile);
            
            outputStream.write(XML_HEADER.getBytes());
            outputStream.write(applicationContextContent.getBytes());
            outputStream.close();
            
            return applicationContextFile.toURI().toURL().toExternalForm();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to create Spring application context file", e);
        }
    }    
}
