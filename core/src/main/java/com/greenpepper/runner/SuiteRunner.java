/*
 * Copyright (c) 2007 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */

package com.greenpepper.runner;

import com.greenpepper.document.InterpreterSelector;
import com.greenpepper.report.ReportGenerator;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import static com.greenpepper.util.URIUtil.flatten;

import java.util.ArrayList;
import java.util.List;

public class SuiteRunner implements SpecificationRunner
{
    private SpecificationRunnerMonitor monitor;
    private DocumentRepository documentRepository;
    private DocumentRunner runner;

    public SuiteRunner()
    {
        runner = new DocumentRunner();
    }

    public void setSystemUnderDevelopment( SystemUnderDevelopment systemUnderDevelopment )
    {
        runner.setSystemUnderDevelopment( systemUnderDevelopment );
    }

    public void setRepository( DocumentRepository documentRepository )
    {
        this.documentRepository = documentRepository;
        runner.setRepository( documentRepository );
    }

    public void setMonitor( SpecificationRunnerMonitor monitor )
    {
        this.monitor = monitor;
        runner.setMonitor( monitor );
    }

    public void setSections( String... sections )
    {
        runner.setSections( sections );
    }

    public void setReportGenerator( ReportGenerator generator )
    {
        runner.setReportGenerator( generator );
    }

    public void run( String source, String destination )
    {
        List<String> names = listDocumentsAt( source );
        if (names.isEmpty())
        {
            monitor.testRunning(source);
            monitor.testDone( 0, 0, 0, 0 );
            return;
        }

        for (String name : names)
        {
            runner.run( name, flatten( name ) );
        }
    }

    private List<String> listDocumentsAt( String source )
    {
        List<String> names = new ArrayList<String>();
        try
        {
            names = documentRepository.listDocuments( source );
        }
        catch (Exception e)
        {
            monitor.exceptionOccured( e );
        }
        return names;
    }

    public void setLazy(boolean lazy)
    {
        runner.setLazy(lazy);
    }

    public void setInterpreterSelector(Class<? extends InterpreterSelector> interpreterSelectorClass)
    {
        runner.setInterpreterSelector(interpreterSelectorClass);
    }
}

