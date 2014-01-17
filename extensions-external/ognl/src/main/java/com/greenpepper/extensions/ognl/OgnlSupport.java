package com.greenpepper.extensions.ognl;

import com.greenpepper.document.Document;
import com.greenpepper.expectation.ShouldBe;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;

public class OgnlSupport implements SystemUnderDevelopment
{
    static
    {
        ShouldBe.register( OgnlExpectation.class );
    }

    private final SystemUnderDevelopment systemUnderDevelopment;

    public OgnlSupport()
    {
        this( new DefaultSystemUnderDevelopment() );
    }

    public OgnlSupport( SystemUnderDevelopment systemUnderDevelopment )
    {
        this.systemUnderDevelopment = systemUnderDevelopment;
    }

    public Fixture getFixture( String name, String... params ) throws Throwable
    {
        return new OgnlFixture( systemUnderDevelopment.getFixture( name, params ) );
    }

    public void addImport( String packageName )
    {
        systemUnderDevelopment.addImport(packageName);
    }

    public void onEndDocument(Document document)
    {
         
    }

    public void onStartDocument(Document document)
    {
        
    }
}
