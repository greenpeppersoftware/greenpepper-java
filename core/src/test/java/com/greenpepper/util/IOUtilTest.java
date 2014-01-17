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

import junit.framework.TestCase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class IOUtilTest extends TestCase
{
    private final List<File> toDelete = new ArrayList<File>();

    protected void tearDown() throws Exception
    {
        for (File file : toDelete)
        {
            file.delete();
        }
    }

    private void deleteAfterTest( File file )
    {
        toDelete.add( file );
    }

    public void testShouldBeAbleToReturnFileContentAsString() throws Exception
    {
        File file = createTempFile( "not empty" );
        assertEquals( "not empty", IOUtil.readContent( file ) );
    }

    private File createTempFile( String content ) throws IOException
    {
        File file = File.createTempFile( "temp", "txt" );
        FileWriter writer = new FileWriter( file );
        writer.write( content );
        writer.close();
        deleteAfterTest( file );
        return file;
    }

    public void testShouldBeAbleToReadInputFully() throws Exception
    {
        String input = "content of the input";
        assertEquals( input, IOUtil.readContent( new StringReader( input ) ) );
    }
}
