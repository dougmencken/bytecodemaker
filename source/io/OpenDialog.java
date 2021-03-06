/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.io;

import java.awt.*;
import java.io.*;
import douglas.mencken.util.InvisibleFrame;

/**
 *	<code>OpenDialog</code>
 *
 *	@version 0.46f3
 */

public class OpenDialog extends FileDialog {
	
	public OpenDialog(Frame parent, String prompt) {
		super(parent, prompt, FileDialog.LOAD);
	}
	
	public OpenDialog(Frame parent) {
		this(parent, "Open");
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
	
	public static String showOpenFileDialog(String prompt, FilenameFilter filter) {
		OpenDialog od = new OpenDialog(new InvisibleFrame(), prompt);
		if (filter != null) {
			od.setFilenameFilter(filter);
		}
		
		// show and wait for path
		od.setVisible(true);
		return od.getPath();
	}
	
	public static String showOpenFileDialog(String prompt) {
		return showOpenFileDialog(prompt, null);
	}
	
	public static String showOpenFileDialog(FilenameFilter filter) {
		return showOpenFileDialog("Open", filter);
	}
	
}