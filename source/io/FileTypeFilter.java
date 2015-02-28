// ===========================================================================
//	FileTypeFilter.java (part of douglas.mencken.io package)
// ===========================================================================

package douglas.mencken.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import com.apple.mrj.MRJOSType;
import com.apple.mrj.MRJFileUtils;

/**
 *	<code>FileTypeFilter</code>
 *
 *	@version 1.02f
 */

public class FileTypeFilter extends Object implements FilenameFilter {
	
	private String[] types;
	private int count;
	
	public FileTypeFilter(String[] fileTypes) {
		this.types = fileTypes;
		this.count = fileTypes.length;
	}
	
	/**
	 *	Tests if a specified file should be included in a file list.
	 *	This is the main method for FilenameFilters.
	 */
	public boolean accept(File dir, String name) {
		if ((dir == null) || (name == null)) return false;
		
		File file = new File(dir, name);
		if (!file.canRead()) return false;
		
		String fileType = "TEXT";
		try {
			MRJOSType type = MRJFileUtils.getFileType(file);
			fileType = type.toString();
		} catch (IOException ioe) {}
		
		for (int i = 0; i < count; i++) {
			if (types[i].equals(fileType)) return true;
		}
		
		return false;
	}
	
}