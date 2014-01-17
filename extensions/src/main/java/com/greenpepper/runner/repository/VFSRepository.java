package com.greenpepper.runner.repository;

import com.greenpepper.document.Document;
import com.greenpepper.html.HtmlDocumentBuilder;
import com.greenpepper.repository.DocumentNotFoundException;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.repository.UnsupportedDocumentException;
import com.greenpepper.util.ExceptionImposter;
import com.greenpepper.util.IOUtil;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.impl.DefaultFileSystemManager;
import org.apache.commons.vfs.provider.FileProvider;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The goal of the file system repository is to give access to specification
 * documents kept in a file system directory hierarchical structure.
 *
 * This particular VFSRepository is implemented using commons.VFS
 * (Virtual File Systems). It allows us to implements our file system with
 * different kinds of protocol (file system, FTP, HTTP ...) It is also possible
 * to create our own resolver and implements, for example, a file system inside
 * a database table(s) or maybe, most interestigly for GreenPepper, inside a wiki.
 *
 * See documentation of VFS for more detail.
 *
 * @author clapointe
 */
public class VFSRepository implements DocumentRepository
{
    private final FileSystemManager fileSystemManager;
    protected static final NotHiddenFileTypeSelector NOT_HIDDEN_FILE_TYPE_SELECTOR = new NotHiddenFileTypeSelector(FileType.FILE);

    public VFSRepository()
    {
        try
        {
            fileSystemManager = VFS.getManager();
        }
        catch (FileSystemException ex)
        {
            throw ExceptionImposter.imposterize( ex );
        }
    }

    public void setDocumentAsImplemeted(String location) throws Exception {}
	
    public List<String> listDocuments(String location) throws Exception
    {
        FileObject root = getFileObject( location );
        if (!root.exists()) return Collections.emptyList();

        List<String> names = new ArrayList<String>();
        if (isDirectory(root))
        {
            for (FileObject child : root.findFiles(NOT_HIDDEN_FILE_TYPE_SELECTOR))
            {
                names.addAll( listDocuments(child.getName().getURI()));
            }
        }
        else
        {
            names.add(root.getName().getURI());
        }
        return names;
    }

	public List<Object> listDocumentsInHierarchy() throws Exception 
	{
		throw new UnsupportedDocumentException("Hierechy not supported");
	}

    public Document loadDocument(String uri) throws Exception
    {
        FileObject file = getFileObject( uri );
        if (!file.exists()) throw new DocumentNotFoundException( file.getURL() );

        return createDocument(file);
    }

    /**
     * For testing purpose of new VFS providers (eg. Confluence, ...)
     *
     * @param urlScheme
     * @param provider
     * @throws FileSystemException
     */
    public void addProvider(String urlScheme, FileProvider provider) throws FileSystemException
    {
        ((DefaultFileSystemManager)fileSystemManager).addProvider(urlScheme, provider);
    }

    private boolean isDirectory(FileObject fileObject) throws FileSystemException
    {
        return fileObject.getType().hasChildren();
    }

    private Document createDocument(FileObject file) throws Exception
    {
        String extension = file.getName().getExtension();

        if ("html".equalsIgnoreCase(extension))
        {
            return createHtmlDocument(file);
        }

        throw new UnsupportedDocumentException(file.getURL());
    }

    private Document createHtmlDocument(FileObject file) throws IOException
    {
        Reader reader = new InputStreamReader(file.getContent().getInputStream());

        try
        {
            return HtmlDocumentBuilder.tablesAndLists().build(reader);
        }
        finally
        {
            IOUtil.closeQuietly(reader);
        }
    }

    private FileObject getFileObject(String uri) throws IOException
    {
        return fileSystemManager.resolveFile(uri);
    }
}