// ===========================================================================
//	SaveDialog.java (part of douglas.mencken.io package)
// ===========================================================================

package douglas.mencken.io;

import java.awt.*;
import java.io.*;
import douglas.mencken.util.InvisibleFrame;

/**
 *	<code>SaveDialog</code>
 *
 *	@version 0.52f2
 */

public class SaveDialog extends FileDialog {
	
	public SaveDialog(Frame parent, String file, String prompt) {
		super(parent, prompt, FileDialog.SAVE);
		super.setFile(file);
	}
	
	public SaveDialog(Frame parent, String file) {
		this(parent, file, "Save");
	}
	
	public SaveDialog(Frame parent) {
		this(parent, "untitled", "Save");
	}
	
	public SaveDialog(String file) {
		this(new InvisibleFrame(), file, "Save");
	}
	
	public SaveDialog() {
		this(new InvisibleFrame(), "untitled", "Save");
	}
	
	public String getPath() {
		if ((super.getDirectory() == null) && (super.getFile() == null)) {
			return null;
		} else {
			return super.getDirectory() + super.getFile();
		}
	}
	
	public String getFilename() {
		return super.getFile();
	}
	
	public static String showSaveFileDialog(String file, String prompt) {
		SaveDialog od = new SaveDialog(new InvisibleFrame(), file, prompt);
		
		// show and wait for path
		od.setVisible(true);
		return od.getPath();
	}
	
	public static String showSaveFileDialog(String file) {
		return showSaveFileDialog(file, "Save");
	}
	
}
