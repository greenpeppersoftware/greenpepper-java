package com.greenpepper.util;

import com.greenpepper.Example;

public interface MarkupPrinter {

	String print(Example cell);

    public static class Default implements MarkupPrinter
    {
		public String print(Example cell) {
            return cell.getContent();
		}
    }
}
