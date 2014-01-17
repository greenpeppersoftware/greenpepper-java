package com.greenpepper.document;

import com.greenpepper.Example;
import com.greenpepper.util.ExampleUtil;

public class GreenPepperTableFilter implements ExampleFilter
{
    public static final String BEGIN_GP_TEST = "begin example";
    public static final String END_GP_TEST = "end example";
    private boolean lazyMode;

    public GreenPepperTableFilter(boolean lazyMode)
    {
        this.lazyMode = lazyMode;
    }

    public boolean canFilter(Example example)
    {
        Example result = example;

        if (result == null) return false;

        if (isBeginTag(result) || isEndTag(result)) return true;
        else if (!lazyMode) return false;

        if (isEndTag(getNextTag(result)))
            return false;
        
        return !isWithinBeginAndEndTags(result);
    }

    public Example filter(Example example)
    {
        return canFilter(example) ? doFilter(example) : example;
    }

    protected Example doFilter(Example example)
    {
        Example result = example;

        while ( !isBeginTag(result) && !isEndTag(result) )
        {
            if (result.nextSibling() == null)
            {
                return getNextEndTag(example) == null ? null : example;
            }
            result = result.nextSibling();
        }

        return filter(result.nextSibling());
    }

    private static boolean isBeginTag(Example table)
    {
        return table == null ? false : BEGIN_GP_TEST.equalsIgnoreCase(ExampleUtil.contentOf(table.at(0, 0, 0)));
    }

    private static boolean isEndTag(Example table)
    {
        return table == null ? false : END_GP_TEST.equalsIgnoreCase(ExampleUtil.contentOf(table.at(0, 0, 0)));
    }

    private static Example getNextEndTag(Example table)
    {
        Example result = table;

        do
        {
            result = getNextTag(result);
        }
        while (!isEndTag(result) && result != null);

        return result;
    }

    private static Example getNextTag(Example table)
    {
        Example result = table == null ? null : table.nextSibling();

        while (!isBeginTag(result) && !isEndTag(result) && result != null)
        {
            result = result.nextSibling();
        }

        return result;
    }

    private static Example bindToEnd(Example example)
    {
        Example nextTag = getNextTag(example);

        while (!isEndTag(nextTag) && nextTag != null)
        {
            nextTag = bindToEnd(nextTag); 
        }

        return getNextTag(nextTag);
    }

    private static boolean isWithinBeginAndEndTags(Example example)
    {
        return isEndTag(bindToEnd(getNextTag(example)));
    }
}
