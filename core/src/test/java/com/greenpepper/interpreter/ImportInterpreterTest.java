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
package com.greenpepper.interpreter;

import com.greenpepper.GreenPepper;
import com.greenpepper.Interpreter;
import com.greenpepper.Specification;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.testing.TestingFixture;
import com.greenpepper.util.Tables;
import junit.framework.TestCase;

public class ImportInterpreterTest
        extends TestCase
{
    private ImportInterpreter interpreter;
    private SystemUnderDevelopment systemUnderDevelopment;

    protected void setUp() throws Exception
    {
        systemUnderDevelopment = new DefaultSystemUnderDevelopment();
        interpreter = new ImportInterpreter( systemUnderDevelopment );
    }

    public void testSpecifiedPackagesShouldBeAutomaticallyImportedInSystemUnderTest() throws Throwable
    {
        Tables tables = Tables.parse(
                "[import]\n" +
                "[com.greenpepper.testing]"
        );
        interpreter.interpret(spec(tables));

        assertEquals(TestingFixture.class, systemUnderDevelopment.getFixture("TestingFixture").getTarget().getClass());
    }

    public void testSpecifiedPackagesShouldBeAutomaticallyImportedInFramework() throws Exception
    {
        Tables tables = Tables.parse(
                "[import]\n" +
                "[com.greenpepper.testing]"
        );
        interpreter.interpret(spec(tables));

        assertTrue(GreenPepper.isAnInterpreter("TestingInterpreter"));
    }

    private Specification spec(Tables tables)
    {
        return new FakeSpecification(tables);
    }

    public static class CustomInterpreter implements Interpreter
    {

        public void interpret(Specification specification)
        {
        }
    }
}