package com.greenpepper.runner.repository;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.vfs.AllFileSelector;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

import com.greenpepper.document.Document;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.util.CollectionUtil;

public class VFSRepositoryTest extends TestCase
{
//    private static final String FIXTURE_NAME = "com.greenpepper.testing.AlternateCalculator";

    private FileObject tempFile = null;

    private FileObject rootDir = null;

    private FileObject subdir1;
    private FileObject subdir2;
    private FileObject subsubdir;

    protected void setUp() throws Exception
    {
        String tempDir = System.getProperty("java.io.tmpdir");
        FileSystemManager fsm = VFS.getManager();

        rootDir = fsm.resolveFile(tempDir + "/testfsr");
        rootDir.createFolder();
        tempFile = createFile(rootDir, "fsp.html");

        subdir1 = fsm.resolveFile(rootDir, "SubDir1");
        subdir1.createFolder();

        createFile(subdir1, "toto.html");
        createFile(subdir1, "titi.HTML");
        subdir2 = fsm.resolveFile(rootDir, "SubDir2");
        subdir2.createFolder();

        createFile(subdir2, "toto.html");
        subsubdir = fsm.resolveFile(subdir2, "subsubdir");
        subsubdir.createFolder();

        createFile(subsubdir, "momo.html");
        createFile(subsubdir, "mimi.html");
    }

    protected void tearDown() throws Exception
    {
        if (rootDir != null)
        {
            rootDir.delete(new AllFileSelector());
        }
    }

    private FileObject createFile(FileObject directory, String fileName) throws Exception
    {
        FileObject f = VFS.getManager().resolveFile(directory, fileName);
        OutputStream os = f.getContent().getOutputStream();
        os.write(TestStringSpecifications.SimpleAlternateCalculatorTest
                .getBytes());
        os.close();
        return f;
    }

    // What are we testing here?

//    public void testSpecFileDoesNotExists() throws Exception
//    {
//        Repository repository = new VFSRepository();
//        Collection<Specification> specifications = repository.getSpecifications(tempFile.getURL().toURI());
//
//        assertNotNull(specifications);
//    }
//
//    public void testAccessFileNoRoot() throws Exception
//    {
//        Repository repository = new VFSRepository();
//        Collection<Specification> specifications = repository.getSpecifications(tempFile.getURL().toURI());
//
//        assertNotNull(specifications);
//    }


    public void testThatAGetAllWithSingleFileReturnsOneSpec() throws Exception
    {
        DocumentRepository repository = new VFSRepository();
        List<String> listing = repository.listDocuments(tempFile.getName().getURI());

        assertEquals(1, listing.size());
        assertSpecificationLoadedOk(repository.loadDocument(CollectionUtil.first(listing)));
    }

    public void testThatAGetAllWithDirectoryReturnsMultipleSpecNoRoot() throws Exception
    {
        DocumentRepository repository = new VFSRepository();
        Collection<String> listing = repository.listDocuments(rootDir.getName().getURI());

        assertEquals(6, listing.size());
    }

    public void testThatAGetAllWithDirectoryReturnsMultipleSpecRoot() throws Exception
    {
        DocumentRepository repository = new VFSRepository();
        Collection<String> listing = repository.listDocuments(subdir2.getName().getURI());

        assertEquals(3, listing.size());
    }

    private void assertSpecificationLoadedOk(Document doc)
    {
        assertEquals("html", doc.getType());
    }

//    public void testAccesToZipFile() throws Exception
//    {
//        DocumentRepository repository = new VFSRepository();
//        String zipPath = this.getClass().getResource("/test.zip").getPath();
//        List<String> listing = repository.listDocuments("zip://" + zipPath);
//
//        assertEquals(1, listing.size());
//        assertSpecificationLoadedOk(repository.loadDocument(CollectionUtil.first(listing)));
//    }

//    public void xtestFileUrlToUncPath() throws Exception
//    {
//        Repository repository = new VFSRepository();
//
//        TestUrl testUrl = new TestUrl(
//                "file://draco/public/projects/GreenPepper/demo/tests");
//
//        documents = repository.getDocuments(testUrl);
//
//        assertEquals(3, documents.size());
//    }
}