package com.greenpepper.runner.repository;

import junit.framework.TestCase;

public class NotHiddenFileSelectorTest extends TestCase
{
    public void testDummy(){}
    
//    public void testThatHiddenFileAreNotSelected() throws FileSystemException
//    {
//        File file = new File(NotHiddenFileSelectorTest.class.getResource("directory/not-hidden-file.txt").toString());
//        FileObject fileObject = VFS.getManager().resolveFile(file.getParent());
//        FileObject[] filesSelected = fileObject.findFiles(new NotHiddenFileTypeSelector(FileType.FILE));
//        assertEquals(1, filesSelected.length);
//    }

}
