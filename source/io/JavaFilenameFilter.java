/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.io;

import java.io.*;
import com.apple.mrj.MRJOSType;
import com.apple.mrj.MRJFileUtils;

import douglas.mencken.util.FileUtilities;

/**
 *	<code>JavaFilenameFilter</code> accepts:
 *	1. Java classes (CAFEBABE)
 *	2. ZIP (JAR) archives
 *	3. TEXT, BcFF files (by type)
 *
 *	@version 1.10f1
 */

public class JavaFilenameFilter extends Object implements FilenameFilter {
	
	public static final int MAGIC_NUMBER_JAVA_CLASS = /* -889275714 */ 0xCAFEBABE;
	public static final int	MAGIC_NUMBER_ZIP_ARCHIVE = 1347093252;
	
	/**
	 *	The default constructor.
	 */
	public JavaFilenameFilter() { super(); }
	
	/**
	 *	Tests if a specified file should be included in a file list.
	 *	This is the main method for FilenameFilters.
	 */
	public boolean accept(File dir, String name) {
		int magicNumber = 0;
		try {
			magicNumber = FileUtilities.readMagicNumber(dir, name);
		} catch (IOException ioe) {
		} catch (IllegalArgumentException iae) {}
		
		switch (magicNumber) {
			case MAGIC_NUMBER_JAVA_CLASS:
			case MAGIC_NUMBER_ZIP_ARCHIVE:
				return true;
		}
		
		try {
			File file = new File(dir, name);
			MRJOSType type = MRJFileUtils.getFileType(file);
			String fileType = type.toString();
			if (fileType.equals("TEXT") || fileType.equals("BcFF")) return true;
		} catch (IOException ioe) {}
		
		return false;
	}
	
}