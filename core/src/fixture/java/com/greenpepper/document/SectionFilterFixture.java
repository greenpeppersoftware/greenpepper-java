package com.greenpepper.document;

import com.greenpepper.Example;
import com.greenpepper.util.Tables;

public class SectionFilterFixture
{
    public String documentContent;
    public String specifiedSectionsToFilter;
    
    public String interpretedElements()
    {
        SectionsTableFilter ctf = new SectionsTableFilter();

        if (!specifiedSectionsToFilter.equals("none"))
            ctf.includeSections(specifiedSectionsToFilter.split(","));
        
        Example tableToFilter = Tables.parse(documentContent);
        StringBuilder ret = new StringBuilder();
        boolean none = true;

        while (tableToFilter != null)
        {
            if (!ctf.canFilter(tableToFilter))
            {
                ret.append(tableToFilter.firstChild().toString());
                tableToFilter = tableToFilter.nextSibling();
                none = false;
            }
            else
            {
                tableToFilter = ctf.filter(tableToFilter);
            }
        }

        if (none)
        {
            return "none";
        }
        return ret.toString();
    }
}
