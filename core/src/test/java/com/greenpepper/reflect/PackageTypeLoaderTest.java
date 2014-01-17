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

package com.greenpepper.reflect;

import static org.mockito.Mockito.*;

import junit.framework.TestCase;

public class PackageTypeLoaderTest extends TestCase
{
    private PackageTypeLoader<?> loader = new PackageTypeLoader<Object>( new JavaTypeLoader<Object>( Object.class ) );

    public void testUsesPackagesToCompleteTypeName() throws Exception
    {
        loader.searchPackage( "com.greenpepper" );
        assertNotNull( loader.loadType( "Calculator" ) );
    }

    public void testPackagesAddedLastAreConsideredFirst() throws Exception
    {
		loader.searchPackage( "com.greenpepper.reflect.override" );
        loader.searchPackage( "com.greenpepper.reflect.masked" );
        loader.searchPackage( "com.greenpepper.reflect.override" );
        assertEquals( com.greenpepper.reflect.override.Fixture.class, loader.loadType( "Fixture" ).getUnderlyingClass() );
    }

	@SuppressWarnings("unchecked")
	public void testPackagesAddedMustBeUnique() throws Exception
	{
		TypeLoader<Object> typeLoaderMock = mock(TypeLoader.class);

		when(typeLoaderMock.loadType( "Calculator" )).thenReturn( null );
		when(typeLoaderMock.loadType( "com.greenpepper.Calculator" )).thenReturn( null );

		PackageTypeLoader<?> loader = new PackageTypeLoader<Object>(typeLoaderMock);

		loader.searchPackage( "com.greenpepper" );
		loader.searchPackage( "com.greenpepper" );
		assertNull( loader.loadType( "Calculator" ) );

		verify( typeLoaderMock ).loadType( "Calculator" );
		verify( typeLoaderMock ).loadType( "com.greenpepper.Calculator" );
		verifyNoMoreInteractions( typeLoaderMock );
	}
}
