package com.greenpepper.util;

import com.greenpepper.Example;

public class Rows extends FakeExample
{
    private static final String EMPTY = "(empty row)";

    public Rows( Object... cells )
    {
        for (Object cell : cells)
        {
            addChild().content = cell;
        }
    }

    public static Rows parse( String markup )
    {
        Tables table = new Tables();

        String[] rows = markup.split( "\\n" );

        for (String cells : rows)
        {
            if (StringUtil.isBlank( cells )) continue;
            Rows row = (Rows) table.addChild();
            if (!isRowEmpty( cells )) row.addChild( Cells.parse( cells ) );
        }

        return (Rows) table.firstChild();
    }

    public static String toMarkup( Example example, boolean prettyPrint )
    {
    	return toMarkup(example, prettyPrint, new MarkupPrinter.Default());
    }

    public static String toMarkup( Example example, boolean prettyPrint, MarkupPrinter printer )
    {
        StringBuilder sb = new StringBuilder();
        if (example.hasChild())
            sb.append( Cells.toMarkup( example.firstChild(), prettyPrint, printer ) );
        else
            sb.append( EMPTY );

        if (example.hasSibling())
        {
            sb.append( "\n" );
            sb.append( toMarkup( example.nextSibling(), prettyPrint, printer) );
        }
        return sb.toString();
    }

    private static boolean isRowEmpty( String cells )
    {
        return EMPTY.equals( cells );
    }

    public String toMarkup( boolean prettyPrint )
    {
        return toMarkup( this, prettyPrint );
    }

    protected FakeExample newSibling()
    {
        return new Rows();
    }

    protected FakeExample newChild()
    {
        return new Cells( "" );
    }

    public String toString()
    {
        return hasChild() ? parts.toMarkup( false ) : EMPTY;
    }
}
