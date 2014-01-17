package com.greenpepper.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class ExceptionImposterTest extends TestCase
{
    private Exception original;

    public void testShouldNotImposterizeUncheckedExceptions()
    {
        original = new RuntimeException();
        assertSame( original, ExceptionImposter.imposterize( original ));
    }

    public void testShouldImposterizeCheckedExceptionsAndKeepAReference()
    {
        original = new Exception();
        RuntimeException imposter = ExceptionImposter.imposterize( original );
        assertTrue( imposter instanceof ExceptionImposter );
        assertSame( original, ((ExceptionImposter) imposter).getRealException() );
    }

    public void testShouldMimicImposterizedExceptionToStringOutput()
    {
        original = new Exception( "Detail message" );
        RuntimeException imposter = ExceptionImposter.imposterize( original );
        assertEquals( original.toString(), imposter.toString() );
    }

    public void testShouldCopyImposterizedExceptionStackTrace()
    {
        original = new Exception( "Detail message" );
        original.fillInStackTrace();
        RuntimeException imposter = ExceptionImposter.imposterize( original );
        assertTrue(
                Arrays.toString( imposter.getStackTrace() ),
                Arrays.equals( original.getStackTrace(), imposter.getStackTrace() ) );
    }

    public void testShouldMimicImposterizedExceptionStackTraceOutput()
    {
        original = new Exception( "Detail message" );
        original.fillInStackTrace();
        RuntimeException imposter = ExceptionImposter.imposterize( original );
        assertEquals( captureStackTrace( original ), captureStackTrace( imposter ) );
    }

    private String captureStackTrace(Exception exception)
    {
        StringWriter capture = new StringWriter();
        exception.printStackTrace( new PrintWriter( capture ) );
        capture.flush();
        return capture.toString();
    }
}
