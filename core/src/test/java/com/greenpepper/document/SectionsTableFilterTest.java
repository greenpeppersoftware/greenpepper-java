package com.greenpepper.document;

import com.greenpepper.Example;
import static com.greenpepper.util.ExampleUtil.contentOf;
import com.greenpepper.util.Tables;
import com.greenpepper.util.TestCase;

public class SectionsTableFilterTest extends TestCase
{
    private SectionsTableFilter filter;

    protected void setUp() throws Exception
    {
        super.setUp();
        filter = new SectionsTableFilter();
    }

    public void testShouldNotFilterSpecificationByDefault() throws Exception
    {
        Example example = Tables.parse(
            "[table]\n" +
            "[with rows]\n"
        );
        Example next = filter.filter( example );
        assertNotNull( next );
        assertEquals( "table", contentOf( next.at( 0, 0, 0 ) ) );
    }

    public void testShouldNotFilterIfNoSectionsAreDefined() throws Exception
    {
        Example example = Tables.parse(
            "[section]\n" +
            "[unix]\n" +
            "****\n" +
            "[table]\n" +
            "[with rows]\n" +
            "****\n"
        );
        Example next = filter.filter( example );
        assertNotNull( next );
        assertEquals( "table", contentOf( next.at( 0, 0, 0 ) ) );
    }

    public void testShouldNotFilterTableIfSectionIsIncluded() throws Exception
    {
        filter.includeSections( "unix" );
        Example example = Tables.parse(
            "[section]\n" +
            "[unix]\n" +
            "****\n" +
            "[table]\n" +
            "[with rows]\n" +
            "****\n"
        );
        Example next = filter.filter( example );
        assertNotNull( next );
        assertEquals( "table", contentOf( next.at( 0, 0, 0 ) ) );
    }

    public void testShouldNotFilterTableIfInAGeneralSection() throws Exception
    {
        filter.includeSections( "unix" );
        Example example = Tables.parse(
            "[section]\n" +
            "****\n" +
            "[table]\n" +
            "[with rows]\n" +
            "****\n"
        );
        Example next = filter.filter( example );
        assertNotNull( next );
        assertEquals( "table", contentOf( next.at( 0, 0, 0 ) ) );
    }

    public void testShouldSkipTablesUpToNextSectionIfSectionIsNotIncluded() throws Exception
    {
        filter.includeSections( "mac" );
        Example example = Tables.parse(
            "[section]\n" +
            "[unix]\n" +
            "****\n" +
            "[unix only]\n" +
            "****\n" +
            "[unix only]\n" +
            "****\n" +
            "[section]"
        );
        Example next = filter.filter( example );
        assertNotNull( next );
        assertEquals( "section", contentOf( next.at( 0, 0, 0 ) ) );
    }
}
