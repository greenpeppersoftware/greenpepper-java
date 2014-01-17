/**
 *
 */
package com.greenpepper.util;

import com.greenpepper.Example;

public class AnnotationTable
    {
    	Example tables;

    	public AnnotationTable(Example tables)
    	{
    		this.tables = tables;
    	}

    	public String toString()
    	{
    		return Tables.toMarkup(tables, false, new AnnotationMarkupPrinter());
    	}

        public boolean equals( Object object )
        {
            if (object == null) return false;
            if (!(object instanceof AnnotationTable)) return false;

            AnnotationTable other = (AnnotationTable) object;

            return this.toString().equals(other.toString());
        }


    	public static AnnotationTable parse (String markup)
    	{
    		AnnotationTable table = new AnnotationTable(Tables.parse(markup));
    		return table;
    	}


	public static class AnnotationMarkupPrinter implements MarkupPrinter
	{

		public String print(Example cell)
		{
            String content = cell.getContent();
            String spacer = StringUtil.isBlank(content) ? "" : " ";

			return  content + (AnnotationUtil.hasAnnotation(cell)
										? spacer + AnnotationUtil.getAnnotationOnCell(cell) : "");
		}
    }

}