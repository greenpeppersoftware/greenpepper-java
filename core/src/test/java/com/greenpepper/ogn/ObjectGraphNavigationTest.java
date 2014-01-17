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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.greenpepper.reflect.Message;
import junit.framework.TestCase;

public class ObjectGraphNavigationTest extends TestCase
{
	private ObjectGraphNavigation objectGraphNavigation;
	private List<ObjectGraphNavigationInfo> received;
	private int counter = 0;

	@Override
	protected void setUp()
			throws Exception
	{
		received = new LinkedList<ObjectGraphNavigationInfo>();

		objectGraphNavigation = new ObjectGraphNavigation(
				false,
				new ObjectGraphNavigationMessageResolver() {
					public Message resolve(ObjectGraphNavigationInfo info) {
						received.add(info);
						return null;
					}
				});
	}

	public void testWithNoNavigation()
    {
		objectGraphNavigation.resolveMessage("Name");

        List<ObjectGraphNavigationInfo> expected = Arrays.asList(
                new ObjectGraphNavigationInfo("Name"));

        assertGraphStrict(expected, received);
    }

    public void testThatWeStartWithTheFirstLevelGraph()
    {
		objectGraphNavigation.resolveMessage("Employee Name First");

        List<ObjectGraphNavigationInfo> expected = Arrays.asList(
                new ObjectGraphNavigationInfo("EmployeeNameFirst"));

        assertGraphHead(expected, received);
    }

    public void testThatWeSplitMethodWithOneDepth()
    {
		objectGraphNavigation.resolveMessage("Employee Name");

        List<ObjectGraphNavigationInfo> expected = Arrays.asList(
                new ObjectGraphNavigationInfo("EmployeeName"),
                new ObjectGraphNavigationInfo("Employee", "Name"));

        assertGraphStrict(expected, received);
    }

    public void testThatWeSplitMethodFromRightToLeftWithOneDepth()
    {
		objectGraphNavigation.resolveMessage("a b c");

        List<ObjectGraphNavigationInfo> expected = Arrays.asList(
                new ObjectGraphNavigationInfo("abc"),
                new ObjectGraphNavigationInfo("ab", "c"),
                new ObjectGraphNavigationInfo("a", "bc"),
                new ObjectGraphNavigationInfo("a.b", "c"));

        assertGraphHead(expected, received);

		received.clear();
		objectGraphNavigation.resolveMessage("Employee Name First");

        expected = Arrays.asList(
                new ObjectGraphNavigationInfo("EmployeeNameFirst"),
                new ObjectGraphNavigationInfo("EmployeeName", "First"),
                new ObjectGraphNavigationInfo("Employee", "NameFirst"),
                new ObjectGraphNavigationInfo("Employee.Name", "First"));

        assertGraphHead(expected, received);
    }

    public void testThatWeCanSplitTwoDepth()
    {
		objectGraphNavigation.resolveMessage("a b c d");

        List<ObjectGraphNavigationInfo> expected = Arrays.asList(
				new ObjectGraphNavigationInfo("abcd"),
                new ObjectGraphNavigationInfo("abc", "d"),
                new ObjectGraphNavigationInfo("ab", "cd"),
                new ObjectGraphNavigationInfo("ab.c", "d"),
                new ObjectGraphNavigationInfo("a", "bcd"),
                new ObjectGraphNavigationInfo("a.bc", "d"),
                new ObjectGraphNavigationInfo("a.b", "cd"),
                new ObjectGraphNavigationInfo("a.b.c", "d"));

        assertGraphHead(expected, received);
    }

	public void testWithManyWordsToDetectOutOfMemory()
	{
		objectGraphNavigation = new ObjectGraphNavigation(
				false,
				new ObjectGraphNavigationMessageResolver() {
					public Message resolve(ObjectGraphNavigationInfo info) {
						counter++;
						return null;
					}
				});

		objectGraphNavigation.resolveMessage("a b c d e f g h i j k l m n o p");
		assertEquals(32768, counter);
	}

    private void assertGraphStrict(Collection<ObjectGraphNavigationInfo> expected, Collection<ObjectGraphNavigationInfo> received)
    {
        assertEquals("Size mismatch", expected.size(), received.size());
        assertGraphHead(expected, received);
    }

    private void assertGraphHead(Collection<ObjectGraphNavigationInfo> expected, Collection<ObjectGraphNavigationInfo> received)
    {
        assertTrue("Size mismatch", received.size() >= expected.size());

        Iterator<ObjectGraphNavigationInfo> itrExpected = expected.iterator();
        Iterator<ObjectGraphNavigationInfo> itrReceived = received.iterator();

        while (itrExpected.hasNext())
        {
            ObjectGraphNavigationInfo exp = itrExpected.next();
            ObjectGraphNavigationInfo rec = itrReceived.next();
            assertEquals(exp.getTarget(), rec.getTarget());
            assertEquals(exp.getMethodName(), rec.getMethodName());
        }
    }
}
