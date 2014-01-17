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

package com.greenpepper.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Provides multiples static methods to deal with IO.
 */
public final class IOUtil
{

    private IOUtil() {}

    public static void closeQuietly( Closeable stream )
    {
        if (stream == null) return;
        try
        {
            stream.close();
        }
        catch (IOException ignored)
        {
        }
    }

    /**
     * Returns the content of a file as a String.
     */
    public static String readContent( File file ) throws IOException
    {
        Reader input = null;
        try
        {
            input = new FileReader( file );
            return readContent( input );
        }
        finally
        {
            closeQuietly( input );
        }
    }

    public static String readContent( Reader in ) throws IOException
    {
        char[] buffer = new char[2048];
        StringBuilder sb = new StringBuilder();
        int read;
        while ((read = in.read( buffer )) != -1)
        {
            sb.append( buffer, 0, read );
        }

        return sb.toString();
    }

    public static File createDirectoryTree( File file ) throws IOException
    {
        if (file.isDirectory() && file.canWrite()) return file;
        if (file.isDirectory())
            throw new IOException( "Directory not writable " + file.getAbsolutePath() );
        if (file.exists())
            throw new IOException( file.getAbsolutePath() + " is not a directory" );
        boolean created = file.mkdirs();
        if (!created) throw new IOException( "Could not create directory " + file.getAbsolutePath() );

        return file;
    }

    public static boolean deleteDirectoryTree( File dir )
    {
        File[] files = dir.listFiles();
        if (files == null) files = new File[0];
        for (File file : files)
        {
            deleteDirectoryTree( file );
        }
        return dir.delete();
    }

	public static void deleteFile( File file )
	{
		if (file != null && !file.delete())
		{
			file.deleteOnExit();
		}
	}
}
