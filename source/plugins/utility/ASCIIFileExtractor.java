/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.plugins;

import java.io.*;
import douglas.mencken.io.*;
import douglas.mencken.util.FileUtilities;

/**
 *	<code>ASCIIFileExtractor</code>
 *	A standard plug-in for Bytecode Maker.
 *
 *	@version	1.03f2
 *	@since		Bytecode Maker 0.5.9
 */

public final class ASCIIFileExtractor extends Object {
	
	private byte[] data;
	private String filename;
	
	public ASCIIFileExtractor() {
		super();
		
		this.data = null;
		this.filename = "untitled";
	}
	
	public String getGroupName() {
		return "Text Utilities";
	}
	
	public String getPlugInName() {
		return "ASCII File Extractor";
	}
	
	public void plugin() {
		this.read();
		
		if (this.data != null) {
			this.data = ASCIIFilter.getFiltered(this.data);
		}
		
		this.write();
	}
	
	public void read() {
		String path = OpenDialog.showOpenFileDialog("Open file");
		if (path == null) return;
		this.filename = (new File(path)).getName();
		
		this.data = null;
		try {
			this.data = FileUtilities.getByteArray(path);
		} catch (IOException ioe) {}
	}
	
	public void write() {
		if (this.data != null) {
			String path = SaveDialog.showSaveFileDialog(this.filename);
			if (path != null) {
				AutoTypeCreatorFixer.getCurrentFixer().add(
					new File(path), "TEXT", "ByTe"
				);
				FileUtilities.writeBytesToFile(this.data, path);
			}
		}
	}
	
	public String get() {
		return (this.data == null) ? "" : new String(this.data);
	}
	
}
