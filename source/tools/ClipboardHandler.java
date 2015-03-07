// ===========================================================================
//	ClipboardHandler.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.lang.String;
import java.lang.Exception;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.*;

import douglas.mencken.tools.*;
import douglas.mencken.icons.*;
import douglas.mencken.util.*;

/**
 *	<code>ClipboardHandler</code>
 *
 *	@version 1.07f2
 */

public class ClipboardHandler extends Object {
	
	private Clipboard clipboard;
	private static ClipboardHandler currentHandler;
	
	public ClipboardHandler() {
		this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		currentHandler = this;
	}
	
	public boolean isEmpty() {
		Transferable t = null;
		try {
			t = clipboard.getContents(this);
		} catch (OutOfMemoryError err) {}
		
		return (t == null);
	}
	
	public Transferable getContents() {
		Transferable t = null;
		try {
			t = clipboard.getContents(this);
		} catch (OutOfMemoryError err) {}
		
		if (t == null) {
			Toolkit.getDefaultToolkit().beep();
			DialogFactory.doDialog(
				new PalmStopIcon(),
				"The Clipboard is empty"
			);
		}
		
		return t;
	}
	
	public String getStringContents() {
		Transferable t = null;
		try {
			t = clipboard.getContents(this);
		} catch (OutOfMemoryError err) {}
		
		if (t == null) {
			Toolkit.getDefaultToolkit().beep();
			DialogFactory.doDialog(
				new PalmStopIcon(),
				"The Clipboard is empty"
			);
			
			return null;
		}
		
		String contents = null;
		try {
			contents = StringUtilities.macToUnixLines(
				(String)t.getTransferData(DataFlavor.stringFlavor)
			);
		} catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			DialogFactory.doDialog(
				new PalmStopIcon(),
				"Clipboard: Transfer Data is not a String"
			);
			
			contents = null;
		}
		
		return contents;
	}
	
	public void setContents(Transferable contents, ClipboardOwner owner) {
		if ((contents != null) && (owner != null)) {
			clipboard.setContents(contents, owner);
		} else {
			Toolkit.getDefaultToolkit().beep();
			DialogFactory.doDialog(
				new PalmStopIcon(),
				"Clipboard: Contents and/or Owner missed"
			);
		}
	}
	
	public void setStringContents(String contents) {
		if ((contents != null) && (!contents.equals(""))) {
			StringSelection s = new StringSelection(StringUtilities.unixToMacLines(contents));
			clipboard.setContents(s, s);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	public static ClipboardHandler getCurrentClipboardHandler() {
		return currentHandler;
	}
	
}