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

package com.greenpepper.html;

import com.greenpepper.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BulletListFilter implements HtmlContentFilter
{
    private static final String ELEMENT_BOUNDARY = "(?s)<\\s*(.*?)\\s*.*?>(.*?)<\\s*/\\s*\\1\\s*>";

    public boolean handles(String tag)
    {
        return tag.equalsIgnoreCase( "li" );
    }

    public String process(String content)
    {
        Matcher matcher = Pattern.compile( ELEMENT_BOUNDARY ).matcher( content );

        StringBuilder sb = new StringBuilder();
        int startIndex, matchIndex;
        for (startIndex = 0; matcher.find(); startIndex = matcher.end())
        {
            matchIndex = matcher.start();
            String preMatch = content.substring( startIndex, matchIndex );
            if (!StringUtil.isBlank( preMatch )) sb.append( span( preMatch ) );
            if (!StringUtil.isBlank( matcher.group( 2 ) )) sb.append( matcher.group() );
        }
        String postMatch = content.substring( startIndex, content.length() );
        if (!StringUtil.isBlank( postMatch )) sb.append( span( postMatch ) );

        return sb.toString();
    }

    private String span(String content)
    {
        return String.format( "<span>%s</span>", content );
    }
}
