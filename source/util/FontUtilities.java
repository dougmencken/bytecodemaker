/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.util;

import java.awt.Font;

/**
 *	<code>FontUtilities</code>
 *
 *	@version 0.2d1
 */

public final class FontUtilities extends Object {
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private FontUtilities() { super(); }
	
	public static final int MONACO_9 = 1;
	public static final int MONACO_12 = 2;
	public static final int GENEVA_9 = 3;
	public static final int GENEVA_10 = 4;
	public static final int COURIER_9 = 5;
	public static final int COURIER_12 = 6;
	public static final int DIALOG_12 = 7;
	
	public static Font createFont(int fontIndex) {
		Font font = new Font("Geneva", Font.PLAIN, 10);
		
		switch (fontIndex) {
			case MONACO_9:
				font = new Font("Monaco", Font.PLAIN, 9);
				break;
			case MONACO_12:
				font = new Font("Monaco", Font.PLAIN, 12);
				break;
			case GENEVA_9:
				font = new Font("Geneva", Font.PLAIN, 9);
				break;
			case GENEVA_10:
				font = new Font("Geneva", Font.PLAIN, 10);
				break;
			case COURIER_9:
				font = new Font("Courier", Font.PLAIN, 9);
				break;
			case COURIER_12:
				font = new Font("Courier", Font.PLAIN, 12);
				break;
			case DIALOG_12:
				font = new Font("Dialog", Font.PLAIN, 12);
				break;
			
			default:
				break;
		}
		
		return font;
	}
	
	public static Font makeBold(Font in) {
		if (!in.isBold()) {
			int style = Font.BOLD;
			if (in.isItalic()) {
				style |= Font.ITALIC;
			}
			
			return new Font(in.getName(), style, in.getSize());
		} else {
			return in;
		}
	}
	
	public static Font makeItalic(Font in) {
		if (!in.isItalic()) {
			int style = Font.ITALIC;
			if (in.isBold()) {
				style |= Font.BOLD;
			}
			
			return new Font(in.getName(), style, in.getSize());
		} else {
			return in;
		}
	}
	
}