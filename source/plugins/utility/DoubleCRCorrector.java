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
 *	<code>DoubleCRCorrector</code>
 *	A standard plug-in for Bytecode Maker.
 *
 *	@version 1.03f4
 */

public final class DoubleCRCorrector extends Object {
	
	private byte[] result = null;
	private String filename;
	char crChar;
	
	public DoubleCRCorrector() {
		this('\r');
	}
	
	public DoubleCRCorrector(char crChar) {
		super();
		this.crChar = crChar;
	}
	
	public String getGroupName() {
		return "Text Utilities";
	}
	
	public String getPlugInName() {
		return "Double CR Corrector";
	}
	
	public void plugin() {
		init();
		write();
	}
	
	public void init() {
		String path = OpenDialog.showOpenFileDialog("Open file");
		if (path == null) return;
		this.filename = (new File(path)).getName();
		
		byte[] data = null;
		try {
			data = FileUtilities.getByteArray(path);
		} catch (IOException ioe) {}
		
		if (data != null) {
			ByteArrayIStream is = new ByteArrayIStream(data);
			ByteArrayOStream os = process(is);
			this.result = os.toByteArray();
		}
	}
	
	public void write() {
		if (this.result != null) {
			String path = SaveDialog.showSaveFileDialog(this.filename);
			if (path != null) {
				AutoTypeCreatorFixer.getCurrentFixer().add(
					new File(path), "TEXT", "ByTe"
				);
				FileUtilities.writeBytesToFile(this.result, path);
			}
		}
	}
	
	public String get() {
		return (this.result == null) ? "" : new String(this.result);
	}
	
	private ByteArrayOStream process(ByteArrayIStream in) {
		ByteArrayOStream out = new ByteArrayOStream();
		boolean firstCR = true;
		int b;
		
		try {
			for (;;) {
				b = in.read();
				
				if (b == crChar) {
					if (firstCR) {
						firstCR = false;
					} else {
						firstCR = true;
						out.write(b);
					}
				} else {
					out.write(b);
				}
			}
		} catch (IOException e) { /* break the loop */ }
		
		return out;
	}
	
}
	
