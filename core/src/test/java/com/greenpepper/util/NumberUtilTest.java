package com.greenpepper.util;

import junit.framework.*;
import com.greenpepper.util.NumberUtil;

public class NumberUtilTest
		extends TestCase
{
	public void testCanSubstituteCommaToPeriod() {
		assertEquals("1.1", NumberUtil.substituteDecimalSeparatorToPeriod("1,1"));
	}
}