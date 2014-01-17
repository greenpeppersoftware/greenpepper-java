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

package com.greenpepper.html;

import junit.framework.TestCase;

public class BulletListFilterTest extends TestCase {

    private BulletListFilter filter;

    protected void setUp() {
        filter = new BulletListFilter();
    }

    protected void tearDown() {
    }

    public void testElementsOtherThanBulletsAreNotAffected() {
        assertFalse(filter.handles("div"));
    }

    public void testTextShouldBeWrappedInSpan() throws Exception {
        String html = "1";
        assertEquals("<span>1</span>", filter.process(html));
    }

    public void testEmptyElementsShouldBeStripped() throws Exception {
        String html = "<b></b><em></em>";
        assertEquals("", filter.process(html));
    }

    public void testElementsWithContentShouldBeLeftUnchanged() throws Exception {
        String html = "<p class='style'>1</p>";
        assertEquals(html, filter.process(html));
    }

    public void testTextAndElementsCanBeMixedToDelimitCells() throws Exception {
        String html = "1<b>2</b>3<i>4</i>5";
        assertEquals("<span>1</span><b>2</b><span>3</span><i>4</i><span>5</span>", filter.process(html));
    }
}
