package com.greenpepper.runner.repository;

import org.apache.commons.vfs.FileSelectInfo;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.FileTypeSelector;

/**
 * VFS File selector that exclude hidden files, and hidden directory and it's children.
 *  
 * @author frochambeau
  */
public class NotHiddenFileTypeSelector extends FileTypeSelector
{

    public NotHiddenFileTypeSelector(FileType fileType)
    {
        super(fileType);
    }

    @Override
    public boolean includeFile(FileSelectInfo fileSelectInfo) throws FileSystemException
    {
        if (fileSelectInfo.getFile().isHidden())
        {
            return false;
        }
        return super.includeFile(fileSelectInfo);
    }

    @Override
    public boolean traverseDescendents(FileSelectInfo fileSelectInfo)
    {
        try
        {
            if (fileSelectInfo.getFile().isHidden())
            {
                return false;
            }
        }
        catch (FileSystemException e) {}
        
        return super.traverseDescendents(fileSelectInfo);
    }
    

}
