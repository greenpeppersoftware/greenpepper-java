package com.greenpepper.document;

import com.greenpepper.Example;
import com.greenpepper.util.ExampleUtil;
import com.greenpepper.util.Tables;
import com.greenpepper.util.TestCase;

public class EagerFilterTest extends TestCase
{

    public void testShouldContinueUntilNothingIsLeftToFilter()
    {
        Tables tables = Tables.parse(
            "****\n" +
            "[Begin Info]\n" +
            "****\n" +
            "[should not be interpreted]" +
            "****\n" +
            "[End Info]\n" +
            "****\n" +
            "[ a table ]\n" +
            "****\n" +
            "[Begin Info]\n" +
            "****\n" +
            "[should not be interpreted]" +
            "****\n" +
            "[End Info]\n" +
            "****\n" +
            "[Begin Info]\n" +
            "****\n" +
            "[should not be interpreted]" +
            "****\n" +
            "[End Info]\n" +
            "****\n" +
            "[ the last table ]\n"
        );

        ExampleFilter filter=new EagerFilter( new CommentTableFilter() );
        Example next = filter.filter( tables );
        assertEquals( "a table", ExampleUtil.contentOf( next.at( 0, 0, 0 ) ) );
        next = filter.filter( next.nextSibling() );
        assertEquals( "the last table", ExampleUtil.contentOf( next.at( 0, 0, 0 ) ) );
    }
}