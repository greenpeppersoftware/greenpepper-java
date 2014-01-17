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

package com.greenpepper.systemunderdevelopment;

import java.io.File;

import com.greenpepper.Calculator;
import com.greenpepper.document.Document;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.report.FileReportGenerator;
import com.greenpepper.repository.FileSystemRepository;
import com.greenpepper.runner.DocumentRunner;
import com.greenpepper.util.TestCase;
import com.greenpepper.util.URIUtil;

public class DefaultSystemUnderDevelopmentTest extends TestCase
{
    private DefaultSystemUnderDevelopment factory;

    public void setUp()
    {
        factory = new DefaultSystemUnderDevelopment();
    }

    public void testCanInstantiateAFixtureUsingNoParameter() throws Throwable
    {
        Fixture fixture = factory.getFixture( Calculator.class.getName() );
        assertNotNull( fixture );
        assertEquals( Calculator.class, fixture.getTarget().getClass() );
    }

    public void testCanInstantiateAFixtureUsingConstructorParameters() throws Throwable
    {
        Fixture fixture = factory.getFixture( Target.class.getName(), "fixture" );
        assertNotNull( fixture );
        assertEquals( Target.class, fixture.getTarget().getClass() );
        Target target = (Target) fixture.getTarget();
        assertEquals( "fixture", target.getName() );
        assertNull( target.getProperty() );
    }

    public void testThrowsExceptionIfFixtureCannotBeInstantiated()
    {
        try
        {
            factory.getFixture( BadTarget.class.getName() );
            fail( "Should have thrown an exception." );
        }
        catch (Throwable e)
        {
            assertTrue( true );
        }
    }
    
    public static class BadTarget
    {
        private BadTarget()
        {
        }
    }

    public static class Target
    {

        private String name;

        public Target()
        {
        }

        public Target( String name )
        {
            this.name = name;
        }

        public Target( String name, String param )
        {
        }

        public Target( String name, Integer numb )
        {
        }

        public String getName()
        {
            return name;
        }

        private String property;

        public void setProperty( String property )
        {
            this.property = property;
        }

        public String getProperty()
        {
            return property;
        }

    }

}
