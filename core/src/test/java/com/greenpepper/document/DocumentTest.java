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
package com.greenpepper.document;

import com.greenpepper.AlternateCalculator;
import com.greenpepper.Assertions;
import static com.greenpepper.Assertions.assertNotAnnotated;
import com.greenpepper.Example;
import com.greenpepper.Interpreter;
import com.greenpepper.Specification;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.interpreter.RuleForInterpreter;
import com.greenpepper.interpreter.SetOfInterpreter;
import com.greenpepper.interpreter.collection.RowFixtureTarget;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.util.Tables;
import static com.greenpepper.util.Tables.parse;
import com.greenpepper.util.TestCase;

public class DocumentTest extends TestCase
{
    private Tables tables;

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    public void testInterpretsASequenceOfTables()
    {
        tables = parse(
            "[" + RuleForInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
            "[a][b][sum?]\n" +
            "[6][2][8]\n" +
            "[5][2][8]\n" +
            "****\n" +
            "[" + SetOfInterpreter.class.getName() + "][" + RowFixtureTarget.class.getName() + "]\n" +
            "[a][b][c]\n" +
            "[1][2][3]"
        );

        Document document = Document.text( tables );
        execute( document );
        assertEquals( 2, document.getStatistics().rightCount() );
        assertEquals( 3, document.getStatistics().wrongCount() );
    }

    public void testComplainsIfInterpreterCannotBeLoaded()
    {
        tables = parse(
            "[NotAnInterpreter]\n" +
            "[1][2][3]"
        );
        execute( document() );
        Assertions.assertAnnotatedException( tables.at( 0, 0, 0 ) );
    }

    public void testDocumentCanBeFiltered()
    {
        tables = parse(
            "[" + RuleForInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
            "[a][b][sum()]\n" +
            "[6][2][8]\n" +
            "****\n" +
            "[Begin Info]\n" +
            "****\n" +
            "[should not be interpreted]" +
            "****\n" +
            "[End Info]\n" +
            "****\n" +
            "[" + RuleForInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
            "[a][b][sum()]\n" +
            "[6][2][8]\n" +
            "****\n" +
            "[Begin Info]\n" +
            "****\n" +
            "[should not be interpreted]" +
            "****\n" +
            "[End Info]\n" +
            "****\n" +
            "[section]\n" +
            "[unix]\n" +
            "****\n" +
            "****\n" +
            "[should not be interpreted]" +
            "****\n" +
            "[section]\n" +
            "****\n" +
            "[Begin Info]\n" +
            "****\n" +
            "[should not be interpreted]" +
            "****\n" +
            "[End Info]\n" +
            "****\n" +
            "[" + RuleForInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
            "[a][b][sum()]\n" +
            "[6][2][8]\n"
        );

        Document document = document();
        document.addFilter( new CommentTableFilter() );
        document.addFilter( new SectionsTableFilter( "mac" ) );
        execute( document );
        assertEquals( 3, document.getStatistics().rightCount() );
    }

     public void testInLazyModeDocumentWithNoGreenPepperTestTagIsNotInterpreted()
     {
         tables = parse(
                 "[" + RuleForInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
                 "[a][b][sum()]\n" +
                 "[6][2][8]\n" +
                 "****\n" +
                 "[Begin Info]\n" +
                 "****\n" +
                 "[should not be interpreted]" +
                 "****\n" +
                 "[End Info]\n" +
                 "****\n" +
                 "[" + RuleForInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
                 "[a][b][sum()]\n" +
                 "[6][2][8]\n" +
                 "****\n" +
                 "[Begin Info]\n" +
                 "****\n" +
                 "[should not be interpreted]" +
                 "****\n" +
                 "[End Info]\n" +
                 "****\n" +
                 "[section]\n" +
                 "[unix]\n" +
                 "****\n" +
                 "****\n" +
                 "[should not be interpreted]" +
                 "****\n" +
                 "[section]\n" +
                 "****\n" +
                 "[Begin Info]\n" +
                 "****\n" +
                 "[should not be interpreted]" +
                 "****\n" +
                 "[End Info]\n" +
                 "****\n" +
                 "[" + RuleForInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
                 "[a][b][sum()]\n" +
                 "[6][2][8]\n"
             );

             Document document = document();
             document.addFilter( new CommentTableFilter() );
             document.addFilter( new SectionsTableFilter( "mac" ) );
             document.addFilter( new GreenPepperTableFilter(true));
             execute( document );
             assertEquals( 0, document.getStatistics().rightCount() );
     }

     public void testInLazyModeDocumentWithGreenPepperTestTagIsInterpretedWhereTagsAre()
     {
         tables = parse(
                 "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
                 "****\n" +
                 "[" + RuleForInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
                 "[a][b][sum()]\n" +
                 "[6][2][8]\n" +
                 "****\n" +
                 "[Begin Info]\n" +
                 "****\n" +
                 "[should not be interpreted]" +
                 "****\n" +
                 "[End Info]\n" +
                 "****\n" +
                 "[" + RuleForInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
                 "[a][b][sum()]\n" +
                 "[6][2][8]\n" +
                 "****\n" +
                 "[Begin Info]\n" +
                 "****\n" +
                 "[should not be interpreted]" +
                 "****\n" +
                 "[End Info]\n" +
                 "****\n" +
                 "[section]\n" +
                 "[unix]\n" +
                 "****\n" +
                 "****\n" +
                 "[should not be interpreted]" +
                 "****\n" +
                 "[section]\n" +
                 "****\n" +
                 "[Begin Info]\n" +
                 "****\n" +
                 "[should not be interpreted]" +
                 "****\n" +
                 "[End Info]\n" +
                 "****\n" +
                 "[" + GreenPepperTableFilter.END_GP_TEST + "]\n" +
                 "****\n" +
                 "[" + RuleForInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
                 "[a][b][sum()]\n" +
                 "[6][2][8]\n"
             );

             Document document = document();
             document.addFilter( new CommentTableFilter() );
             document.addFilter( new SectionsTableFilter( "mac" ) );
             document.addFilter( new GreenPepperTableFilter(true));
             execute( document );
             assertEquals( 2, document.getStatistics().rightCount() );
     }

    public void testThatFirstCellOfTableCanSpecifyAnInterpreterClass()
    {
        tables = parse(
            "[" + InterpreterExpectingASystemUnderDevelopment.class.getName() + "]"
        );
        Example cell = tables.at( 0, 0, 0 );
        execute( document() );
        assertNotAnnotated( cell );
    }

    public void testThatInterpreterCanHaveAFixtureSpecifiedInSecondCell()
    {
        tables = parse(
            "[" + InterpreterExpectingAFixture.class.getName() + "][" + Target.class.getName() + "]"
        );

        execute( document() );
        assertNotAnnotated( tables.at( 0, 0, 0 ) );
        assertNotAnnotated( tables.at( 0, 0, 1 ) );
    }

    public void testComplainsWhenFixtureIntantiationFails()
    {
        tables = parse(
            "[" + InterpreterExpectingAFixture.class.getName() + "][NonExistingFixture]"
        );

        execute( document() );
        Assertions.assertAnnotatedException( tables.at( 0, 0, 0 ) );
    }

    private Document document()
    {
        return Document.text( tables );
    }

    private void execute( Document document )
    {
        final DefaultSystemUnderDevelopment systemUnderDevelopment = new DefaultSystemUnderDevelopment();
        document.execute(new GreenPepperInterpreterSelector(systemUnderDevelopment));
    }

    public static class InterpreterExpectingASystemUnderDevelopment implements Interpreter
    {
        public InterpreterExpectingASystemUnderDevelopment( SystemUnderDevelopment systemUnderDevelopment )
        {
        }

        public void interpret( Specification specification )
        {
            specification.nextExample();
        }

        public void execute( Example example )
        {
        }
    }

    public static class InterpreterExpectingAFixture implements Interpreter
    {
        public InterpreterExpectingAFixture( Fixture fixture )
        {
        }

        public void interpret( Specification specification )
        {
            specification.nextExample();
        }

        public void execute( Example example )
        {
        }
    }

    public static class Target
    {
    }
}