// ===========================================================================
//	BcFFCoder.java
// ===========================================================================

package douglas.mencken.bm.plugins;

import java.awt.Toolkit;
import java.io.*;
import douglas.mencken.io.*;
import douglas.mencken.util.FileUtilities;

/**
 *	<code>BcFFCoder</code>
 *	A standard plug-in for Bytecode Maker.
 *
 *	@version 1.03f5
 */

public final class BcFFCoder extends Object {
	
	private byte[] result = null;
	
	public BcFFCoder() {
		super();
	}
	
	public String getGroupName() {
		return "Text Utilities";
	}
	
	public String getPlugInName() {
		return "BcFF Coder";
	}
	
	public void plugin() {
		init();
		write();
	}
	
	public void init() {
		String path = OpenDialog.showOpenFileDialog("Open file");
		if (path == null) return;
		
		byte[] data = null;
		try {
			data = FileUtilities.getByteArray(path);
		} catch (IOException ioe) {}
		
		if (data != null) {
			ByteArrayIStream is = new ByteArrayIStream(data);
			ByteArrayOStream os = new ByteArrayOStream();
			
			new BcFFEngine(is, os);
			this.result = os.toByteArray();
		}
	}
	
	public void write() {
		if (this.result != null) {
			String path = SaveDialog.showSaveFileDialog("untitled.bcff");
			if (path != null) {
				AutoTypeCreatorFixer.getCurrentFixer().add(
					new File(path), "TEXT", "ByTe"
				);
				FileUtilities.writeBytesToFile(result, path);
			}
		}
	}
	
	public String get() {
		return (result == null) ? "" : new String(this.result);
	}
	
}
