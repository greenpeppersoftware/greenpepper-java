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

import java.lang.reflect.Constructor;

import com.greenpepper.Statistics;
import com.greenpepper.document.CommentTableFilter;
import com.greenpepper.document.Document;
import com.greenpepper.document.GreenPepperTableFilter;
import com.greenpepper.document.InterpreterSelector;
import com.greenpepper.document.SectionsTableFilter;
import com.greenpepper.report.Report;
import com.greenpepper.report.ReportGenerator;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.Timer;

public class DocumentRunner implements SpecificationRunner
{
    private ReportGenerator reportGenerator;
    private SystemUnderDevelopment systemUnderDevelopment;
    private DocumentRepository documentRepository;
    private String[] sections;
    private SpecificationRunnerMonitor monitor;
    private Class<? extends InterpreterSelector> interpreterSelectorClass;
    private boolean lazy;
    
    public DocumentRunner()
    {
        monitor = new NullSpecificationRunnerMonitor();
    }

    public void setMonitor( SpecificationRunnerMonitor monitor )
    {
        this.monitor = monitor;
    }

    public void setSystemUnderDevelopment( SystemUnderDevelopment systemUnderDevelopment )
    {
        this.systemUnderDevelopment = systemUnderDevelopment;
    }

    public void setInterpreterSelector( Class<? extends InterpreterSelector> interpreterSelectorClass )
    {
        this.interpreterSelectorClass = interpreterSelectorClass;
    }

    public void setRepository( DocumentRepository documentRepository )
    {
        this.documentRepository = documentRepository;
    }

    public void setSections( String... sections )
    {
        this.sections = sections;
    }

    public void setReportGenerator( ReportGenerator generator )
    {
        this.reportGenerator = generator;
    }

    public void setLazy(boolean lazy)
    {
        this.lazy = lazy;
    }

    public void run( String input, String output )
    {
        Report report = null;

        try
        {
            report = reportGenerator.openReport( output );
            monitor.testRunning( input );

			final Timer total = new Timer().start();

            Document document = documentRepository.loadDocument( input );
			document.setSections(sections);
            document.addFilter( new CommentTableFilter() );
            document.addFilter( new SectionsTableFilter( sections ) );
            document.addFilter( new GreenPepperTableFilter( lazy ) );

			final Timer execution = new Timer().start();

            systemUnderDevelopment.onStartDocument(document);
            document.execute( newInterpreterSelector(systemUnderDevelopment) );
            systemUnderDevelopment.onEndDocument(document);

			execution.stop();
            document.done();
			total.stop();

			document.getTimeStatistics().tally( total.elapse(), execution.elapse() );

            report.generate( document );
            Statistics stats = document.getStatistics();
            monitor.testDone( stats.rightCount(), stats.wrongCount(), stats.exceptionCount(), stats.ignoredCount() );
        }
        catch (Exception e)
        {
            if (report != null) report.renderException( e );
            monitor.exceptionOccured( e );
        }
        finally
        {
            closeReport( report );
        }

    }

    private void closeReport( Report report )
    {
        if (report == null) return;
        try
        {
            reportGenerator.closeReport( report );
        }
        catch (Exception e)
        {
            monitor.exceptionOccured( e );
        }
    }

    private InterpreterSelector newInterpreterSelector(SystemUnderDevelopment systemUnderDevelopment) throws Exception
    {
		Constructor<? extends InterpreterSelector>  constructor = interpreterSelectorClass.getConstructor(SystemUnderDevelopment.class);
		return constructor.newInstance( systemUnderDevelopment );
	}
}
