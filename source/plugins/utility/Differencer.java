// ===========================================================================
//	Differencer.java
// ===========================================================================

package douglas.mencken.bm.plugins;

import java.awt.Font;
import java.io.*;

import douglas.mencken.io.*;
import douglas.mencken.util.FileUtilities;
import douglas.mencken.util.StringUtilities;
import douglas.mencken.util.DecHexConverter;
import douglas.mencken.icons.StopIcon;
import douglas.mencken.tools.*;

import douglas.mencken.beans.TextView;
import douglas.mencken.beans.TextViewFrame;

/**
 *	<code>Differencer</code>
 *	A standard plug-in for Bytecode Maker.
 *
 *	@version 1.04f
 */

public class Differencer extends Object {
	
	private char[] result = null;
	
	public Differencer() {
		super();
	}
	
	public String getGroupName() {
		return "Text Utilities";
	}
	
	public String getPlugInName() {
		return "Differencer";
	}
	
	public void plugin() {
		init();
		show();
	}
	
	public void init() {
		String path = OpenDialog.showOpenFileDialog("Choose the 1st file to compare...");
		if (path == null) return;
		byte[] data1 = null;
		try {
			data1 = FileUtilities.getByteArray(path);
		} catch (IOException ioe) {}
		if (data1 == null) return;
		
		ByteArrayIStream is1 = new ByteArrayIStream(data1);
		int size1 = data1.length;
		
		path = OpenDialog.showOpenFileDialog("Choose the 2nd file to compare...");
		if (path == null) return;
		
		byte[] data2 = null;
		try {
			data2 = FileUtilities.getByteArray(path);
		} catch (IOException ioe) {}
		if (data2 == null) return;
		
		ByteArrayIStream is2 = new ByteArrayIStream(data2);
		int size2 = data2.length;
		
		if (size1 == size2) {
			this.result = getDifferences(is1, is2, "total size is " + size1);
		} else {
			ModalDialogs.doDialog(
				new StopIcon(),
				this.getPlugInName() + ": Try only at files with the same length!"
			);
		}
	}
	
	public void show() {
		TextView tp = new TextView();
		tp.setFont(new Font("Monaco", Font.PLAIN, 9));
		
		if (this.result != null) {
			tp.append(result);
			TextViewFrame tpf = new TextViewFrame("Differencer's result", tp, true);
			tpf.getTextView().add(new TextViewPopupMenu(tpf.getTextView(), "Differencer's result", false));
			tpf.setVisible(true);
		}
	}
	
	public void write() {
		if (this.result != null) {
			String path = SaveDialog.showSaveFileDialog("Differencer's result");
			if (path != null) {
				AutoTypeCreatorFixer.getCurrentFixer().add(
					new File(path), "TEXT", "ByTe"
				);
				FileUtilities.writeBytesToFile(FileUtilities.charsToBytes(result), path);
			}
		}
	}
	
	public String get() {
		return (this.result != null) ? new String(result) : "";
	}
	
	
	public static char[] getDifferences(ByteArrayIStream is1, ByteArrayIStream is2,
										String additionalInfo) {
		StringBuffer buf = new StringBuffer();
		int count = 0;
		
		try {
			for (;;) {
				int meter = is1.getStreamPointer();
				int b1 = is1.read();
				int b2 = is2.read();
				
				if (b1 != b2) {
					count++;
					
					buf.append('#').append(DecHexConverter.DECtoHEX(meter));
					buf.append(" (").append(meter).append("):\n");
					
					buf.append((char)b1);
					buf.append(" (#").append(DecHexConverter.DECtoHEX(b1));
					buf.append(" [").append(b1).append("])	 ");
					
					buf.append((char)b2);
					buf.append(" (#").append(DecHexConverter.DECtoHEX(b2));
					buf.append(" [").append(b2).append("])	 ");
					
					buf.append("\n\n");
				}
			}
		} catch (IOException e) { /* break the loop */ }
		
		if (count > 0) buf.append('\n');
		buf.append(count).append(" differences found\n");
		buf.append(additionalInfo);
		
		return buf.toString().toCharArray();
	}
	
}