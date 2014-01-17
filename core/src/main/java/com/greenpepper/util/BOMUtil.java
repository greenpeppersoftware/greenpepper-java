/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public final class BOMUtil
{
    /**
     * see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6206835
     */
    private final static int NONE = -1;
    private final static int UTF32BE = 0;
    private final static int UTF32LE = 1;
    private final static int UTF16BE = 2;
    private final static int UTF16LE = 3;
    private final static int UTF8 = 4;

    private final static byte[] UTF32BEBOMBYTES = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF,};
    private final static byte[] UTF32LEBOMBYTES = new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00,};
    private final static byte[] UTF16BEBOMBYTES = new byte[]{(byte) 0xFE, (byte) 0xFF,};
    private final static byte[] UTF16LEBOMBYTES = new byte[]{(byte) 0xFF, (byte) 0xFE,};
    private final static byte[] UTF8BOMBYTES = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF,};

    private final static byte[][] BOMBYTES = new byte[][]{
            UTF32BEBOMBYTES,
            UTF32LEBOMBYTES,
            UTF16BEBOMBYTES,
            UTF16LEBOMBYTES,
            UTF8BOMBYTES,
    };

    private final static int MAXBOMBYTES = 4;//no bom sequence is longer than 4 byte

    private BOMUtil() {}

    public static int getBOMType(byte[] bytes)
    {
        return getBOMType( bytes, bytes.length );
    }

    public static int getBOMType(byte[] bytes, int length)
    {
        for (int i = 0; i < BOMBYTES.length; i++)
        {
            for (int j = 0; j < length && j < BOMBYTES[i].length; j++)
            {
                if (bytes[j] != BOMBYTES[i][j])
                {
                    break;
                }
                if (bytes[j] == BOMBYTES[i][j] && j == BOMBYTES[i].length - 1)
                {
                    return i;
                }
            }
        }
        return NONE;
    }

    public static int getBOMType(File file)
            throws IOException
    {
        FileInputStream fIn = null;

        try
        {
            fIn = new FileInputStream( file );
            byte[] buff = new byte[MAXBOMBYTES];
            int read = fIn.read( buff );
            return getBOMType( buff, read );
        }
        finally
        {
            IOUtil.closeQuietly(  fIn );
        }
    }

    public static int getSkipBytes(int bomType)
    {
        if (bomType < 0 || bomType >= BOMBYTES.length)
        {
            return 0;
        }
        return BOMBYTES[bomType].length;
    }

    public static Reader newReader(File file)
            throws IOException
    {
        final String fileEncoding = System.getProperty( "file.encoding", "UTF-8" );
        final String greenPepperEncoding = System.getProperty( "greenpepper.file.encoding", fileEncoding );
        return newReader( file, greenPepperEncoding );
    }

    public static Reader newReader(File file, String encoding)
            throws IOException
    {
        int bomType = getBOMType( file );
        int skipBytes = getSkipBytes( bomType );
        FileInputStream fIn = new FileInputStream( file );
        long skippedBytes = fIn.skip( skipBytes );
        return new InputStreamReader( fIn, encoding );
    }
}