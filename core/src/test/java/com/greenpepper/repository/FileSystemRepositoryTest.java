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
package com.greenpepper.repository;

import static com.greenpepper.util.IOUtil.createDirectoryTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.greenpepper.document.Document;
import com.greenpepper.util.IOUtil;
import com.greenpepper.util.TestCase;

public class FileSystemRepositoryTest extends TestCase
{
    private File root, hierarchy;
    private FileSystemRepository repository, hierarchyRepo;

    protected void setUp() throws Exception
    {
        File tempDir = new File( System.getProperty( "java.io.tmpdir" ) );
        root = createDirectoryTree( new File( tempDir, "testfsr" ) );
        hierarchy = createDirectoryTree( new File( tempDir, "hierarchy" ) );
        repository = new FileSystemRepository( root );
        hierarchyRepo = new FileSystemRepository( hierarchy );
    }

    protected void tearDown() throws Exception
    {
        deleteOutputDirectory();
    }

    private void deleteOutputDirectory()
    {
        if (root != null) IOUtil.deleteDirectoryTree( root );
    }

    public void testComplainsIfDocumentIsNotFound() throws Exception
    {
        try
        {
            repository.loadDocument( "unknown.file" );
            fail();
        }
        catch (DocumentNotFoundException expected)
        {
            pass();
        }
    }

    public void testLocatesAndLoadsHtmlFiles() throws Exception
    {
        Document doc = repository.loadDocument( createDocument( root, "/subdir/specification.html" ) );
        assertSpecification( doc );
    }

    public void testShouldListAllVisibleHtmlFilesInDirectory() throws Exception
    {
        List<String> specFiles = createSpecificationFiles(root);
        createOtherFiles(root);
        Set<String> listing = new HashSet<String>( repository.listDocuments( "/specs" ) );

        assertTrue( listing.containsAll( specFiles ) );
        assertTrue( specFiles.containsAll( listing ) );
    }

    public void testShouldBeAbleToLoadListedFiles() throws Exception
    {
        createDocument( root, "/directory/file.html" );
        createDocument( root, "/directory/sub directory/some file.html" );
        createDocument( root, "/directory/file with uppercase ext.HTML" );
        List<String> docs = repository.listDocuments( "directory" );
        assertEquals(3, docs.size());
        for (String doc : docs)
        {
            assertSpecification( repository.loadDocument( doc ) );
        }
    }
    
    public void testWeCanRetrieveTheDocumentsInAHierarchy() throws Exception
    {
    	List<String> names = Arrays.asList(new String[]{"specs",
    													"specs/dir1",
    													"specs/dir1/spec1.html",
    													"specs/dir1/subdir1",
    													"specs/dir1/subdir1/spec2.html",
    													"specs/dir1/subdir1/spec4.html",
    													"specs/dir2",
    													"spec3.html"});
    	createSpecificationHierarchyFiles(hierarchy, names);
        List<Object> hierarchy = hierarchyRepo.listDocumentsInHierarchy();
        assertNamesInHierarchy((Hashtable)hierarchy.get(3), names);
    }

    public void testAFileShouldNotHoldAnyDocument() throws Exception
    {
    	
        List<String> listing = repository.listDocuments( createDocument( root, "test.html" ) );
        assertTrue( listing.isEmpty() );
    }

    private List<String> createOtherFiles(File root) throws Exception
    {
        return Arrays.asList(
            createDocument( root, "/specs/dir1/not_a_spec.log" ),
            createDocument( root, "/specs/dir1/dir3/a_text_file.txt" ) );
    }

    private List<String> createSpecificationFiles(File root) throws Exception
    {
        return Arrays.asList(
            createDocument( root, "/specs/dir1/spec1.html" ),
            createDocument( root, "/specs/dir1/spec2.html" ),
            createDocument( root, "/specs/dir1/dir3/spec4.html" ),
            createDocument( root, "/specs/dir2/spec3.html" ) );
    }

    private void createSpecificationHierarchyFiles(File root, List<String> names) throws Exception
    {
        for(String name: names)
            createDocument( root, name );
    }
    
    @SuppressWarnings("unchecked")
	private void assertNamesInHierarchy(Hashtable branch, List<String> names)
    {
    	Iterator iter = branch.keySet().iterator();
    	while(iter.hasNext())
    	{
    		String name = (String)iter.next();
        	Vector child = (Vector)branch.get(name);
        	assertTrue(names.contains(child.get(0)));
        	assertNamesInHierarchy((Hashtable)child.get(3), names);
    	}
    }

    private void assertSpecification( Document doc ) throws IOException
    {
        assertNotNull( doc );
        
        StringWriter buffer = new StringWriter();
    	PrintWriter writer = new PrintWriter( buffer );
        doc.print( writer );
        writer.close();
        
        assertEquals( specification(), buffer.toString() );
    }

    private String createDocument( File root, String fileName )
        throws IOException
    {
        File file = new File( root, fileName );
        IOUtil.createDirectoryTree( file.getParentFile() );
        if(fileName.toLowerCase().endsWith(".html"))
        {
	        Writer writer = null;
	        try
	        {
	            writer = new FileWriter( file );
	            writer.write( specification() );
	            writer.flush();
	        }
	        catch (IOException e)
	        {
	            IOUtil.closeQuietly( writer );
	        }
        }
        return fileName;
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
}