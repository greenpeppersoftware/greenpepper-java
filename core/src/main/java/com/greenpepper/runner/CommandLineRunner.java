/*
 * Copyright (c) 2006 Pyxis Technologies inc.
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

import static com.greenpepper.util.URIUtil.decoded;
import static com.greenpepper.util.URIUtil.flatten;

import com.greenpepper.GreenPepper;
import com.greenpepper.GreenPepperCore;
import com.greenpepper.document.GreenPepperInterpreterSelector;
import com.greenpepper.report.FileReportGenerator;
import com.greenpepper.report.PlainReport;
import com.greenpepper.report.ReportGenerator;
import com.greenpepper.report.XmlReport;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.repository.FileSystemRepository;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.util.Bean;
import com.greenpepper.util.IOUtil;
import com.greenpepper.util.cli.ArgumentMissingException;
import com.greenpepper.util.cli.CommandLine;
import com.greenpepper.util.cli.Option;
import com.greenpepper.util.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommandLineRunner
{
    private final CommandLine cli;
    private final Map<String, Object> options;
    private SpecificationRunner runner;
    private SpecificationRunnerMonitor monitor;
    private PrintStream out;

    public CommandLineRunner()
    {
        this( System.out );
    }

    public CommandLineRunner(PrintStream out)
    {
        this.cli = new CommandLine();
        this.options = new HashMap<String, Object>();
        this.monitor = new NullSpecificationRunnerMonitor();
        this.out = out;
    }

    public void setMonitor(Object monitor)
    {
        this.monitor = new SpecificationRunnerMonitorProxy( monitor );
    }

    public void run(String... args) throws Exception
    {
        defineCommandLine();
        if (!parseCommandLine( args )) return;
        runSpec();
    }

    private void runSpec() throws Exception
    {
        options.putAll( cli.getOptionValues() );
        options.put( "report generator", reportGenerator() );
        options.put( "monitor", monitor );
        options.put( "repository", repository() );
        new Bean( runner ).setProperties( options );
        runner.run( source(), destination() );
    }

    private DocumentRepository repository() throws IOException
    {
        return optionSpecified( "repository" ) ?
                (DocumentRepository) cli.getOptionValue( "repository" ) :
                new FileSystemRepository( parentFile( input() ) );
    }

    private ReportGenerator reportGenerator() throws IOException
    {
        FileReportGenerator generator = new FileReportGenerator( createOuputDirectory() );
        generator.adjustReportFilesExtensions( optionSpecified( "suite" ) || output() == null );
        generator.setReportClass( optionSpecified( "xml" ) ? XmlReport.class : PlainReport.class );
        return generator;
    }

    private File createOuputDirectory() throws IOException
    {
        return IOUtil.createDirectoryTree( outputDirectory() );
    }

    private String input()
    {
        return cli.getArgument( 0 ) != null ? decoded( cli.getArgument( 0 ) ) : null;
    }

    private String output()
    {
        return cli.getArgument( 1 ) != null ? decoded( cli.getArgument( 1 ) ) : null;
    }

    public String source()
    {
        return optionSpecified( "repository" ) ? input() : fileName( input() );
    }

    private File outputDirectory() throws IOException
    {
        if (optionSpecified( "suite" ))
            return output() != null ? new File( output() ) : (File) cli.getOptionValue( "output" );
        return output() != null ? parentFile( output() ) : (File) cli.getOptionValue( "output" );
    }

    private File parentFile(String pathname) throws IOException
    {
        return new File( pathname ).getCanonicalFile().getParentFile();
    }

    private String fileName(String pathname)
    {
        return new File( pathname ).getName();
    }

    private String destination() throws IOException
    {
        if (optionSpecified( "suite" )) return "";
        return output() != null ? fileName( output() )
                : optionSpecified( "repository" ) ? flatten( input() ) : fileName( input() );
    }

    private boolean parseCommandLine(String[] args) throws ParseException
    {
        cli.parse( args );
        if (optionSpecified( "help" )) return displayUsage();
        if (optionSpecified( "version" )) return displayVersion();
        if (input() == null) throw new ArgumentMissingException( "input" );
        runner = optionSpecified( "suite" ) ? new SuiteRunner() : new DocumentRunner();
        return true;
    }

    private boolean optionSpecified(String name)
    {
        return cli.hasOptionValue( name );
    }

    private boolean displayVersion()
    {
        out.println( String.format( "GreenPepper version \"%s\"", GreenPepperCore.VERSION ) );
        return false;
    }

    private boolean displayUsage()
    {
        out.println( cli.usage() );
        return false;
    }

    private void defineCommandLine()
    {
        File workingDirectory = new File( System.getProperty( "user.dir" ) );

        String banner = "greenpepper [options] input [ouput]\n" +
                "Run the input specification and produce a report in output file or in directory specified by -o";
        cli.setBanner( banner );

        cli.defineOption( cli.buildOption( "lazy", "--lazy", "Execute document in lazy mode" ) );
        cli.defineOption( cli.buildOption( "locale", "-l", "--locale LANG", "Set application language (en, fr, ...)" ).asType( Locale.class ).whenPresent( new SetLocale() ) );
        cli.defineOption( cli.buildOption( "system under development", "--sud", "-f CLASS;ARGS", "Use CLASS as the system under development and instantiate it with ARGS" ).
                convertedWith( new FactoryConverter() ).defaultingTo( new DefaultSystemUnderDevelopment() ) );
        cli.defineOption( cli.buildOption( "output", "-o DIRECTORY", "Produce reports in DIRECTORY (defaults to current directory)" ).defaultingTo( workingDirectory ).asType( File.class ) );
        cli.defineOption( cli.buildOption( "repository", "-r CLASS;ARGS", "Use CLASS as the document repository and instantiate it with ARGS (defaults to com.greenpepper.repository.FileSystemRepository)" ).
                convertedWith( new FactoryConverter() ) );
        cli.defineOption( cli.buildOption( "interpreter selector", "--selector CLASS", "Use CLASS as the interpreter selector (defaults to com.greenpepper.document.GreenPepperInterpreterSelector)" ).
                asType( Class.class ).
                defaultingTo( GreenPepperInterpreterSelector.class ) );
        cli.defineOption( cli.buildOption( "suite", "-s", "--suite", "Run a suite rather than a single test (output must refer to a directory)" ) );
        cli.defineOption( cli.buildOption( "sections", "-t SECTIONS", "Filter input specification to only execute SECTIONS (comma separated list of sections)" ).
                convertedWith( new StringArrayConverter( "," ) ).defaultingTo( new String[0] ) );
        cli.defineOption( cli.buildOption( "xml", "--xml", "Generate XML report (defaults to plain)" ) );
        cli.defineOption( cli.buildOption( "help", "--help", "Display this help and exit" ) );
        cli.defineOption( cli.buildOption( "version", "--version", "Output version information and exit" ) );
        cli.defineOption( cli.buildOption( "debug", "--debug", "Enable debug mode" ).whenPresent( new SetDebugMode() ) );
		cli.defineOption( cli.buildOption( "stop", "--stop", "Stop the execution of the specification on the first failure" ).whenPresent( new SetStopOnFirstFailure() ) );
    }

    public static class SetDebugMode implements Option.Stub
    {
        public void call(Option option)
        {
            GreenPepper.setDebugEnabled( true );
        }
    }

    public static class SetLocale implements Option.Stub
    {
        public void call(Option option)
        {
            GreenPepper.setLocale( (Locale) option.getValue() );
        }
    }

	public static class SetStopOnFirstFailure implements Option.Stub
	{
		public void call(Option option)
		{
			GreenPepper.setStopOnFirstFailure( true );
		}
	}
}