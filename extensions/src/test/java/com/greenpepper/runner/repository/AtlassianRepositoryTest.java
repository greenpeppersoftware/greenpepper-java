package com.greenpepper.runner.repository;

import com.greenpepper.document.Document;
import com.greenpepper.repository.DocumentRepository;
import static com.greenpepper.util.CollectionUtil.toVector;
import com.greenpepper.util.TestCase;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.xmlrpc.WebServer;
import static org.hamcrest.Matchers.is;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class AtlassianRepositoryTest extends TestCase
{
    private static WebServer ws;
	private Mockery context = new JUnit4Mockery();
    private Handler handler;
    private DocumentRepository repo;

    public static Test suite() {
        return new TestSetup(new TestSuite(AtlassianRepositoryTest.class)) {
            @Override
            protected void setUp() throws Exception
            {
                ws = new WebServer( 19005 );
                ws.start();
            }

            @Override
            protected void tearDown() throws Exception
            {
                ws.shutdown();
            }
        };
    }

    protected void setUp()
    {
        handler = context.mock( Handler.class );
        ws.removeHandler( "greenpepper1" );
        ws.addHandler( "greenpepper1", handler );
    }

    public void testCannotProvideListOfSpecifications() throws Exception
    {
        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1&includeStyle=true#SPACE KEY");
        assertTrue( repo.listDocuments( "PAGE" ).isEmpty() );
    }

    @SuppressWarnings("unchecked")
    public void testProvideAHierarchyListOfSpecifications() throws Exception
    {
    	final Vector<?> expected1 = toVector( "SPACE KEY" );
    	final Vector<?> expected2 = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			atLeast(1).of(handler).getSpecificationHierarchy("", "", expected1);
			will(returnValue(hierarchy()));
			atLeast(1).of(handler).getRenderedSpecification("", "", expected2);
			will(returnValue(specification()));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1&includeStyle=true#SPACE KEY");
        List docs = repo.listDocumentsInHierarchy();
        Hashtable pageBranch = (Hashtable)docs.get(2);
        repo.loadDocument((String)pageBranch.keySet().iterator().next());
    }

	public void testCanDowloadPageContentFromAConfluenceServer() throws Exception
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			one(handler).getRenderedSpecification("", "", expected);
			will(returnValue(specification()));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1&includeStyle=true#SPACE KEY");
        Document spec = repo.loadDocument( "PAGE" );

        assertSpecification( spec );
    }

	public void testCanDowloadPageContentFromAJiraServer() throws Exception
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "PROJECT ID", "ISSUE KEY", Boolean.FALSE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			one(handler).getRenderedSpecification("", "", expected);
			will(returnValue(specification()));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1&includeStyle=false#PROJECT ID");
        Document spec = repo.loadDocument( "ISSUE KEY" );

        assertSpecification( spec );
    }

    public void testImplementedIsPassedInTheArgumenents() throws Exception
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.FALSE  );

		context.checking(new Expectations()
		{{
			one(handler).getRenderedSpecification("", "", expected);
			will(returnValue(specification()));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1#SPACE KEY");
        repo.loadDocument( "PAGE?implemented=false" );
    }

    public void testStyleDefaultsToTrueAndImplementedDefaultsToTrue() throws Exception
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			one(handler).getRenderedSpecification("", "", expected);
			will(returnValue(specification()));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1#SPACE KEY");
        repo.loadDocument( "PAGE" );
    }

    private void assertSpecification( Document doc )
    {
        assertNotNull( doc );
        StringWriter buffer = new StringWriter();
        doc.print( new PrintWriter( buffer ) );
        assertEquals( specification(), buffer.toString() );
    }

    public void testComplainsIfArgumentsAreMissing() throws Exception
    {
        try
        {
            repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?includeStyle=true");
            repo.loadDocument( "ISSUE KEY?" );
            fail();
        }
        catch (Exception e)
        {
            assertThat( e, is( IllegalArgumentException.class ) );
        }
    }
    
    public void testWeCanSetASpecificationAsImplemented() throws Exception
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			one(handler).setSpecificationAsImplemented("", "", expected);
			will(returnValue("<success>"));
		}});

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1#SPACE KEY");
        repo.setDocumentAsImplemeted( "PAGE" );
    }
    
    public void testExceptionIsThrownIfReturnedValueFromSettingAsImplementedDiffersFromSuccess()
    {
	    @SuppressWarnings("unchecked")
    	final Vector<?> expected = toVector( "SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE  );

		context.checking(new Expectations()
		{{
			one(handler).setSpecificationAsImplemented("", "", expected);
			will(returnValue("exception"));
		}});

        try
        {
            repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=greenpepper1#SPACE KEY");
			repo.setDocumentAsImplemeted( "PAGE" );
			fail();
		} 
        catch (Exception e)
        {
			assertEquals("exception", e.getMessage());
		}
    }
    
    @SuppressWarnings("unchecked")
	private List<Object> hierarchy()
    {
    	List<Object> hierachy = new Vector<Object>();
    	hierachy.add("1");
    	hierachy.add("HOME");
    	
    	Hashtable pageBranch = new Hashtable();
    	Vector page = new Vector();
    	page.add("PAGE");
    	page.add(new Hashtable());
    	pageBranch.put("PAGE", page);
    	
    	hierachy.add(pageBranch);
    	
    	return hierachy;
    }

    private String specification()
    {
        return "<html><table border='1' cellspacing='0'>" +
               "<tr><td colspan='3'>My Fixture</td></tr>" +
               "<tr><td>a</td><td>b</td><td>sum()</td></tr>" +
               "<tr><td>1</td><td>2</td><td>3</td></tr>" +
               "<tr><td>2</td><td>3</td><td>15</td></tr>" +
               "<tr><td>2</td><td>3</td><td>a</td></tr>" +
               "</table></html>";
    }

    public static interface Handler
    {
        String getRenderedSpecification(String username, String password, Vector<?> args);
        Vector getSpecificationHierarchy(String username, String password, Vector<?> args);
        String setSpecificationAsImplemented(String username, String password, Vector<?> args);
    }
}