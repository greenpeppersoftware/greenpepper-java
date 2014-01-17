package com.greenpepper.interpreter.flow.dowith;

import com.greenpepper.Example;
import static com.greenpepper.GreenPepper.isAnInterpreter;
import com.greenpepper.TypeLoaderChain;
import com.greenpepper.annotation.Annotations;
import com.greenpepper.interpreter.flow.Row;
import com.greenpepper.interpreter.flow.RowSelector;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Type;
import com.greenpepper.reflect.TypeLoader;
import com.greenpepper.util.ExampleUtil;

import java.util.Collection;

public class DoWithRowSelector implements RowSelector
{
    protected final Fixture fixture;

    private TypeLoader<Row> typeLoader;

    public DoWithRowSelector(Fixture fixture)
    {
        this.fixture = fixture;
        this.typeLoader = new TypeLoaderChain<Row>(Row.class);
        searchPackage( getClass().getPackage().getName() );
        addSuffix("Row");
    }

    public void searchPackage(String name)
    {
        typeLoader.searchPackage(name);
    }

    public void addSuffix(String suffix)
    {
        typeLoader.addSuffix(suffix);
    }

    /* (non-Javadoc)
	 * @see com.greenpepper.interpreter.dowith.RowSelector#select(com.greenpepper.Example)
	 */
    public Row select(Example example)
    {
        if (isARow(identifier(example)))
            return instantiateRow(example);

        if (isAnInterpreter(identifier(example)))
            return new InterpretRow(fixture);

        return new DefaultRow(fixture);
    }

    private boolean isARow(String name)
    {
        Type<Row> type = loadRowType(name);
        return type != null && !type.getUnderlyingClass().equals(Row.class);
    }

    private Type<Row> loadRowType(String name)
    {
        return typeLoader.loadType(name);
    }

    private String identifier(Example row)
    {
        return ExampleUtil.contentOf(row.firstChild());
    }

    private Row instantiateRow(Example row)
    {
        Type<Row> rowClass = loadRowType(identifier(row));
        try
        {
            return rowClass.newInstance(fixture);
        }
        catch (Throwable throwable)
        {
            row.firstChild().annotate(Annotations.exception(throwable));
            return new SkipRow();
        }
    }

    public void addSuffixes(Collection<String> suffixes)
    {
        for (String suffix : suffixes)
        {
            addSuffix(suffix);
        }
    }

    public void addPackages(Collection<String> packages)
    {
        for (String aPackage : packages)
        {
            searchPackage(aPackage);
        }
    }
}