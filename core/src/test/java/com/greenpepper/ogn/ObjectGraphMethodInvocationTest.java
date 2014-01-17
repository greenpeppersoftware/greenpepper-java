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
package com.greenpepper.ogn;

import junit.framework.TestCase;

import java.lang.reflect.Method;

public class ObjectGraphMethodInvocationTest extends TestCase
{
    private Method getterMethod;
    private Method setterMethod;
    private Target target;
    
    public void setUp() throws Exception
    {
        target = new Target();
        getterMethod = Target.class.getMethod("getField"); 
        setterMethod = Target.class.getMethod("setField", new Class[] {int.class});
    }
    
    public void testInvokeSuccesfullGetterOnMethod() throws Exception
    {
        ObjectGraphMethodInvocation invocation = new ObjectGraphMethodInvocation(getterMethod, true);
        
        Object received = invocation.invoke(target);
        assertEquals(1, received);
    }
    
    public void testInvokeSuccesfullSetterOnMethod() throws Exception
    {
        ObjectGraphMethodInvocation invocation = new ObjectGraphMethodInvocation(setterMethod, false);

        try 
        {
            invocation.invoke(target);
            
            fail("Invoking target with no parameters fail!");
        }
        catch (Exception ex)
        {
            assertTrue(true);
        }
        
        Object received = invocation.invoke(target, "999");
        assertNull(received);
        
        assertEquals(999, target.getField());
    }
    
    public void testInvokeMethodThatDoNotBelongingToTargetClass() throws Exception {

        ObjectGraphMethodInvocation invocation = new ObjectGraphMethodInvocation(getterMethod, true);
        
        try
        {
            invocation.invoke(new Object());
            
            fail("Invoking target with object not of the type of the target fail");
        }
        catch (Exception ex) 
        {
            assertTrue(true);
        }
    }
    
    public void testInvokeSetterWithWrongParameterType() throws Exception
    {
        ObjectGraphMethodInvocation invocation = new ObjectGraphMethodInvocation(setterMethod, false);
        
        try
        {
            invocation.invoke(target, "abc");
            
            fail("Invoking target with wrong parameter type fail");
        }
        catch (NumberFormatException ex) 
        {
            assertTrue(true);
        }
    }
    
    private static class Target
    {
        private int field = 1;
        
        public int getField()
        {
            return field;
        }
        
        public void setField(int field)
        {
            this.field = field; 
        }
    }
}
