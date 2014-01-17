/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
package com.greenpepper.runner.ant;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

import com.greenpepper.document.GreenPepperInterpreterSelector;

public class AntTaskRunnerTest extends BuildFileTest
{
    public void XtestRun() throws URISyntaxException
    {
        loadBuildFile( "build.xml" );

        executeTarget( "run" );
        assertThat( getLog(), containsString( "Results: 379 tests: 366 right, 0 wrong, 13 ignored, 0 exception(s) " +
                                              "for 22 specification(s)" ) );
    }

    public void testCheckParametersAssignment() throws URISyntaxException
    {
        loadBuildFile( "build-check-parameters.xml" );

        try
        {
            executeTarget( "run" );
        }
        catch (Exception ex)
        {
            //ok we just need to check parameters
        }

        /*
		<greenpepper suite="true"
                     input="input.html"
                     output="output.dir"
                     outputType="xml"
                     failonerror="true"
                     stopOnFirstFailure="true"
                     locale="fr"
                     debug="true"
                     selector="com.greenpepper.document.GreenPepperInterpreterSelector">

            <systemUnderDevelopment class="com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment">
                <arguments>A1</arguments>
                <arguments>B1</arguments>
            </systemUnderDevelopment>

            <repository class="com.greenpepper.repository.FileSystemRepository">
                <arguments>A2</arguments>
                <arguments>B2</arguments>
            </repository>

            <section>
                <include>unix</include>
            </section>
		</greenpepper>

         */
        AntTaskRunner task = getAntTaskRunnerInstanceFromProject();

        assertTrue( task.isSuite() );
        assertEquals( "input.html", task.getInput() );
        assertEquals( "output.dir", task.getOutput().getName() );
        assertEquals( "xml", task.getOutputType() );
        assertTrue( task.isFailOnError() );
        assertTrue( task.isStopOnFirstFailure() );
        assertEquals( "fr", task.getLocale() );
        assertTrue( task.isDebug() );
        assertEquals( GreenPepperInterpreterSelector.class.getName(), task.getSelector() );

        RepositoryElement repository = task.getRepository();
        assertNotNull( repository );
        String repositoryArg = repository.toArgument( task );
        assertEquals( "com.greenpepper.repository.FileSystemRepository;A2;B2", repositoryArg );

        SectionElement section = task.getSection();
        assertNotNull( section );
        String sectionArg = section.toArgument( task );
        assertEquals( "unix", sectionArg );

        SystemUnderDevelopmentElement sud = task.getSystemDevelopment();
        assertNotNull( sud );
        String sudArg = sud.toArgument( task );
        assertEquals( "com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;A1;B1", sudArg );
    }

    public void testWithANonExistingSpecification() throws URISyntaxException
    {
        loadBuildFile( "build-specification-not-found.xml" );

        try
        {
            executeTarget( "run" );

            fail();
        }
        catch (BuildException ex)
        {
            assertThat( ex.getMessage(), containsString( "Some greenpepper tests did not run" ) );
            assertThat( ex.getLocation().toString(), containsString( "build-specification-not-found.xml:23: " ) );
        }
    }

    public void testSuccesfullLocalSpecification() throws URISyntaxException
    {
        loadBuildFile( "build-local-successfull.xml" );

        executeTarget( "run" );

        assertThat( getLog(), containsString( "Results: 6 tests: 4 right, 1 wrong, 0 ignored, 1 exception(s) " +
                                              "for 1 specification(s)" ) );
    }

    private void loadBuildFile(String buildFile) throws URISyntaxException
    {
        URL url = getClass().getResource( String.format( "/%s", buildFile ) );

        File antFile = new File( url.toURI() );
        File baseDir = antFile.getParentFile();
        configureProject( antFile.getAbsolutePath() );

        Project project = this.getProject();
        project.setBasedir( baseDir.getAbsolutePath() );
    }

    private AntTaskRunner getAntTaskRunnerInstanceFromProject()
    {
        Target target = (Target) this.getProject().getTargets().get( "run" );
        Task[] tasks = target.getTasks();
        for (Task t : tasks)
        {
            if ("greenpepper".equals( t.getTaskName() ))
            {
                UnknownElement gpTask = (UnknownElement) t;
                return (AntTaskRunner) gpTask.getRealThing();
            }
        }

        throw new IllegalArgumentException( "GreenPepper task not found!" );
    }
}
