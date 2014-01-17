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

package com.greenpepper.util.cli;

import com.greenpepper.util.TestCase;
import static com.greenpepper.util.cli.OptionBuilder.create;

public class CommandLineTest extends TestCase
{
    private CommandLine cli;

    protected void setUp()
    {
        cli = new CommandLine();
    }

    public void testShouldDetectOptions() throws ParseException
    {
        cli.defineOption( create( "debug" ).withShortForm( "-x" ) );
        cli.defineOption( create( "verbose" ).withShortForm( "-v" ) );
        cli.parse( "-x" );
        assertTrue( cli.hasOptionValue( "debug" ) );
        assertFalse( cli.hasOptionValue( "verbose" ) );
    }

    public void testOptionsCanHaveParameters() throws ParseException
    {
        cli.defineOption( create( "block size" ).withShortForm( "-b" ).wantsArgument( "SIZE" ) );
        cli.parse( "-b", "1024" );
        assertTrue( cli.hasOptionValue( "block size" ) );
        assertEquals( "1024", cli.getOptionValue( "block size" ) );
    }

    public void testOptionsCanHaveALongForm() throws ParseException
    {
        cli.defineOption( create( "block size" ).withLongForm( "--block-size" ).wantsArgument( "SIZE" ) );
        cli.parse( "--block-size", "1024" );
        assertTrue( cli.hasOptionValue( "block size" ) );
        assertEquals( "1024", cli.getOptionValue( "block size" ) );
    }

    public void testShouldNotConsumeAnythingIfNoOptionIsDefined() throws ParseException
    {
        String[] remaining = cli.parse( "1", "2", "3" );
        assertEquals( new String[]{"1", "2", "3"}, remaining );
    }

    public void testConsumeOptionsAndReturnRemainingArgs() throws ParseException
    {
        cli.defineOption( create( "raw" ).withLongForm( "--raw" ) );
        String[] remaining = cli.parse( "--raw", "input", "output" );
        assertEquals( new String[]{"input", "output"}, remaining );
    }

    public void testMultipleOptionsAreSupported() throws ParseException
    {
        cli.defineOption( create( "human" ).withShortForm( "-h" ).withDescription( "Human readable format" ) );
        cli.defineOption( create( "block size" ).withLongForm( "--block-size" ).wantsArgument( "SIZE" ) );
        cli.defineOption( create( "debug" ).withShortForm( "-x" ) );

        String[] remaining = cli.parse( "-h", "--block-size", "1024", "-x", "input", "output" );
        assertTrue( cli.hasOptionValue( "human" ) );
        assertEquals( "1024", cli.getOptionValue( "block size" ) );
        assertTrue( cli.hasOptionValue( "debug" ) );
        assertEquals( new String[]{"input", "output"}, remaining );
    }

    public void testOptionValuesCanHaveAType() throws ParseException
    {
        cli.defineOption( create( "block size" ).withShortForm( "-b" ).wantsArgument( "SIZE" ).asType( int.class ) );
        cli.parse( "-b", "1024" );
        assertEquals( 1024, cli.getOptionValue( "block size" ) );
    }

    public void testOptionCanHaveADefaultValue() throws ParseException
    {
        cli.defineOption( create( "block size" ).withShortForm( "-b" ).wantsArgument( "SIZE" ).asType( int.class ).defaultingTo( 1024 ) );
        cli.parse();
        assertEquals( 1024, cli.getOptionValue( "block size" ) );
    }

    public void testHelpMessageIncludesBannerAndDescriptionsOfOptions() throws ParseException
    {
        cli.setBanner( "My cool program v1.0" );
        cli.defineOption( create( "raw" ).withLongForm( "--raw" ).withDescription( "Specifies raw ouput format" ) );
        cli.defineOption( create( "block size" ).withShortForm( "-b" ).withLongForm( "--block-size" ).wantsArgument( "SIZE" ).withDescription( "Specifies block size" ) );
        cli.defineOption( create( "debug" ).withShortForm( "-x" ).withDescription( "Turn debugging on|off" ) );
        assertEquals(
            "Usage: My cool program v1.0\n" +
            "\n" +
            "Options:\n" +
            "    --raw                     Specifies raw ouput format\n" +
            "-b, --block-size SIZE         Specifies block size\n" +
            "-x                            Turn debugging on|off",
            cli.usage() );
    }

    public void testOptionsCanBeSpecifiedInLiteralForm() throws ParseException
    {
        cli.defineOption( cli.buildOption( "block size", "-b", "--block-size SIZE", "Specifies block size" ) );
        cli.parse( "--block-size", "1024" );
        assertTrue( cli.hasOptionValue( "block size" ) );
        assertEquals( "1024", cli.getOptionValue( "block size" ) );
    }

    public void testComplainsOfInvalidOptions() throws ParseException
    {
        try
        {
            cli.parse( "-x" );
            fail();
        }
        catch (InvalidOptionException expected)
        {
            assertTrue( true );
        }
    }
}

