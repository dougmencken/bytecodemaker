/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.storage.constants;

import java.awt.Toolkit;

/**
 *	<code>BMErrorStrings</code> contains one static method
 *	and <code>public</code> array of error strings.
 *
 *	@version 1.01v
 */

public class BMErrorStrings extends Object {
	
	public static final String[] ERROR_STRINGS = {
/*  0 */  "Unknown error",
/*  1 */  "No 'class' or 'interface' definition found",
/*  2 */  "'class' or 'interface' definitions cannot include 'private', 'protected', 'static', 'volatile', 'transient', 'native', and 'strict' access modifiers",
/*  3 */  "Repeated modifier",
/*  4 */  "Unknown modifier",
/*  5 */  "'extends' or 'implements' keyword expected",
/*  6 */  "'class' and 'interface' keywords cannot be used together",
/*  7 */  "'abstract' methods can't be 'static'",
/*  8 */  "Class (interface) names cannot contain space characters",
/*  9 */  "Class (interface) or superclass name is not a java identifier",
/* 10 */  "Fields can't be 'synchronized', 'abstract', 'native' or 'interface'",
/* 11 */  "Methods can't be 'volatile', 'transient' or 'interface'",
/* 12 */  "The superclass for 'interface' can only be 'java.lang.Object'"
	};
	
	/**
	 *	Note:	number -1 is used for "Internal Error".
	 */
	public static String getErrorString(int number) {
		if (number == -1) {
			return "Internal Error";
		}
		
		try {
			return BMErrorStrings.ERROR_STRINGS[number];
		} catch (ArrayIndexOutOfBoundsException aibe) {
			Toolkit.getDefaultToolkit().beep();
			return BMErrorStrings.ERROR_STRINGS[0];
		}
	}
	
	/**
	 *	Don't let anyone instantiate this class.
	 */
	private BMErrorStrings() {}
	
}
