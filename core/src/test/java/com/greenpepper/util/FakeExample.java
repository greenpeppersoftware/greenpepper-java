package com.greenpepper.util;

import com.greenpepper.AbstractExample;
import com.greenpepper.annotation.Annotation;

import java.io.PrintWriter;

public abstract class FakeExample extends AbstractExample
{
    public Annotation annotation;
    public Object content;
    public FakeExample parts;
    public FakeExample more;

    public FakeExample()
    {
        this( null );
    }

    public FakeExample( Object content )
    {
        this.content = content;
    }

    public void annotate( Annotation annotation )
    {
        this.annotation = annotation;
    }

    public Annotation getAnnotation()
    {
        return annotation;
    }

    public FakeExample firstChild()
    {
        return parts;
    }

    public FakeExample nextSibling()
    {
        return more;
    }

    public String getContent()
    {
        return String.valueOf( content );
    }

    public void setContent( String content )
    {
        this.content = content;
    }

    public FakeExample addChild()
    {
        return addChild( newChild() );
    }

    public FakeExample addSibling()
    {
        return addSibling( newSibling() );
    }

    protected abstract FakeExample newSibling();

    protected abstract FakeExample newChild();

    public FakeExample addChild( FakeExample child )
    {
        if (hasChild()) return parts.addSibling( child );
        parts = child;
        return parts;
    }

    public FakeExample addSibling( FakeExample sibling )
    {
        if (hasSibling()) return more.addSibling( sibling );
        more = sibling;
        return more;
    }

    public String toMarkup()
    {
        return toMarkup( true );
    }

    public abstract String toMarkup( boolean prettyPrint );

    public void print( PrintWriter out )
    {
        out.write( toMarkup( true ) );
    }

    public boolean equals( Object o )
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FakeExample that = (FakeExample) o;

        if (more != null ? !more.equals( that.more ) : that.more != null) return false;
        if (parts != null ? !parts.equals( that.parts ) : that.parts != null) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = (more != null ? more.hashCode() : 0);
        result = 31 * result + (parts != null ? parts.hashCode() : 0);
        return result;
    }
}
