/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans;

import java.beans.PropertyEditorSupport;

import douglas.mencken.util.StringUtilities;

/**
 *	Property editor for array of strings.
 *
 *	@version	1.0
 */

public class StringArrayEditor extends PropertyEditorSupport {
	
	public static char DELIMITER = ',';
	public static String STRING_PREFIX = "\"";
	public static String STRING_SUFFIX = "\"";
	public static String NULL_STRING_ARRAY = "( null )";

	protected char delimiter;

	public StringArrayEditor() {
		this(StringArrayEditor.DELIMITER);
	}
	
	public StringArrayEditor(char delim) {
		super();
		this.delimiter = delim;
	}
	
	public void setAsText(String newStr) throws IllegalArgumentException {
		if (newStr.equals(StringArrayEditor.NULL_STRING_ARRAY)) {
			super.setValue(null);
		} else {
			String tokenizeMe = newStr.replaceAll(StringArrayEditor.STRING_SUFFIX + this.delimiter, "\n");
			String[] newValues = StringUtilities.tokenize(tokenizeMe, "\n");
			int strs = newValues.length;
			for (int i = 0; i < strs; i++) {
				newValues[i] = newValues[i].trim();
				if (newValues[i].startsWith(StringArrayEditor.STRING_PREFIX)) {
					newValues[i] = newValues[i].substring(StringArrayEditor.STRING_PREFIX.length());
				}
				if (newValues[i].endsWith(StringArrayEditor.STRING_SUFFIX)) {
					newValues[i] = newValues[i].substring(0, newValues[i].length() - StringArrayEditor.STRING_SUFFIX.length());
				}
			}
			super.setValue(newValues);
		}
		super.firePropertyChange();
	}

	public String getAsText() {
		String[] strArray = (String[])(super.getValue());
		if (strArray == null) return StringArrayEditor.NULL_STRING_ARRAY;
		/* return StringUtilities.stringArrayToString(strArray, String.valueOf(this.delimiter)); */

		int strs = strArray.length;
		if (strs == 0) return "";

		StringBuffer returnStr = new StringBuffer();
		for (int i = 0; i < strs; i++) {
			returnStr.append(StringArrayEditor.STRING_PREFIX).append(strArray[i]).append(StringArrayEditor.STRING_SUFFIX);
			if (i < (strs - 1)) returnStr.append(this.delimiter);
		}
		return returnStr.toString();
	}
	
}
