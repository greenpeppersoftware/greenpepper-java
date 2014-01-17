package com.greenpepper.document;

import java.util.ArrayList;
import java.util.List;

import com.greenpepper.Example;
import com.greenpepper.Interpreter;
import com.greenpepper.document.Document.FilteredSpecification;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.util.ExampleUtil;
import com.greenpepper.util.StringUtil;
import com.greenpepper.util.Tables;

public class GreenpepperFilterFixture
{
    private boolean lazyMode;
    private Tables documentContent;
    private String section = "";

    public void setDocumentContent(Tables tables)
    {
            documentContent = tables;
    }
    
    public void setExecutionMode(String mode)
    {
            lazyMode = "lazy".equals(mode); 
    }

    public void setSection(String section)
    {
            this.section = section;
    }
    
    public String InterpretedElements()
    {
        List<String> interpreted = new ArrayList<String>();

        Example example = documentContent;
        Document document = Document.text(example);
        
        document.addFilter(new GreenPepperTableFilter(lazyMode));
        document.addFilter(new CommentTableFilter());
        document.addFilter(new SectionsTableFilter(section));

        FilteredSpecification spec = document.new FilteredSpecification(example);
        
        while (spec.hasMoreExamples())
        {
            example = spec.peek();
            interpreted.add(ExampleUtil.contentOf(example.at(0,0,0)));
            Interpreter interpreter = new GreenPepperInterpreterSelector(new DefaultSystemUnderDevelopment()).selectInterpreter(spec.peek());
            interpreter.interpret(spec);
        }
        
        StringBuilder sb = new StringBuilder();

        for (String s : interpreted)
        {
            sb.append("[").append(s).append("]");
        }

        return StringUtil.isEmpty(sb.toString()) ? "none" : sb.toString();
    }
}
