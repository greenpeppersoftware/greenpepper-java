package com.greenpepper.util;

import junit.framework.TestSuite;

public class TablesTest extends junit.framework.TestCase
{
    private static Class[] contexts = new Class[]{
        TablesTest.AnEmptyTable.class,
        TablesTest.AnEmptyRowInATable.class,
        TablesTest.SeveralTables.class,
        TablesTest.BlankLinesInMarkup.class,
    };

    public static TestSuite suite()
    {
        TestSuite suite = new TestSuite( NameUtils.humanize( TablesTest.class.getSimpleName() ) );
        for (Class context : contexts)
        {
            suite.addTest( new TestSuite( context, NameUtils.humanize( context.getSimpleName() ) ) );
        }
        return suite;
    }

    public static class BlankLinesInMarkup extends TestCase
    {
        private Tables table;
        private String markup;

        protected void setUp()
        {
            table = new Tables();
            table.row( 1, 2, 3 ).row( 4, 5, 6 ).row( 7, 8, 9 );
            table.table().row( 1, 2, 3 ).row( 4, 5, 6 ).row( 7, 8, 9 );
            markup = "\n\n[ 1 ][ 2 ][ 3 ]\n\n[ 4 ][ 5 ][ 6 ]\n[ 7 ][ 8 ][ 9 ]\n\n****\n\n[ 1 ][ 2 ][ 3 ]\n[ 4 ][ 5 ][ 6 ]\n[ 7 ][ 8 ][ 9 ]\n\n";
        }

        public void testShouldBeIgnoredDuringParsing()
        {
            assertEquals( table, Tables.parse( markup ) );
        }
    }

    public static class AnEmptyTable extends TestCase
    {
        private Tables tables;
        private String markup;

        protected void setUp()
        {
            tables = new Tables();
            markup = "(empty table)";
        }

        public void testShouldBeVisibleAsEmpty()
        {
            assertEquals( markup, tables.toMarkup() );
        }

        public void testCanBeIndicatedInMarkup()
        {
            assertEquals( tables, Tables.parse( markup ) );
        }
    }

    public static class AnEmptyRowInATable extends TestCase
    {
        private Tables table;
        private String markup;

        protected void setUp()
        {
            table = new Tables();
            table.row( 1, 2, 3 ).row().row( 7, 8, 9 );
            markup = "[ 1 ][ 2 ][ 3 ]\n(empty row)\n[ 7 ][ 8 ][ 9 ]";
        }

        public void testShouldBeVisibleAsEmpty()
        {
            assertEquals( markup, table.toMarkup() );
        }

        public void testCanBeIndicatedInTheMarkup()
        {
            assertEquals( table, Tables.parse( markup ) );
        }
    }

    public static class SeveralTables extends TestCase
    {
        private Tables tables;
        private String markup;

        protected void setUp()
        {
            tables = new Tables();
            tables.row( 1, 2 ).row( 3, 4 );
            tables.table().row( 5, 6 ).row( 7, 8 );
            markup = "[ 1 ][ 2 ]\n[ 3 ][ 4 ]\n****\n[ 5 ][ 6 ]\n[ 7 ][ 8 ]";
        }

        public void testShouldBeSeparatedWithARowOfStarsWhenDisplayed()
        {
            assertEquals( markup, tables.toMarkup() );
        }

        public void testShouldBeParsedFromMarkupCorrectly()
        {
            assertEquals( tables, Tables.parse( markup ) );
        }
    }
}
