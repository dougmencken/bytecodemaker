// ===========================================================================
//	ClipboardFrame.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import douglas.mencken.util.StringUtilities;
import douglas.mencken.util.FileUtilities;
import douglas.mencken.io.SaveDialog;
import douglas.mencken.io.AutoTypeCreatorFixer;
import douglas.mencken.beans.TextView;
import douglas.mencken.beans.TextViewFrame;

/**
 *	<code>ClipboardFrame</code>
 *
 *	@version 1.11f0
 */

public class ClipboardFrame extends TextViewFrame implements ActionListener {
	
	protected WindowListener windowListener;
	private final MenuBar mbar;
	
	/**
	 *	Constructs a new ClipboardFrame object.
	 *	
	 *	@param	theWindowListener	The main WindowListener of this ClipboardFrame.
	 *								Used while processing File->Close menu command.
	 */
	public ClipboardFrame(WindowListener theWindowListener) {
		super("Clipboard", new TextView());
		TextViewPopupMenu popupMenu = new TextViewPopupMenu(
			super.getTextView(), "java.clipboard", false
		);
		popupMenu.remove(1);
		popupMenu.remove(1);
		super.add(popupMenu);
		
		Dimension screen = getToolkit().getScreenSize();
		setBounds(7, screen.height - 210, screen.width - 200, 200);
		
		this.mbar = new MenuBar();
		Menu file = new Menu("File");
		MenuItem save = new MenuItem("Save...", new MenuShortcut(KeyEvent.VK_S));
		save.setActionCommand("SAVE");
		save.addActionListener(this);
		file.add(save);
		MenuItem close = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
		close.setActionCommand("CLOSE");
		close.addActionListener(this);
		file.add(close);
		mbar.add(file);
		
		this.windowListener = theWindowListener;
		super.addWindowListener(theWindowListener);
	}
	
	/**
	 *	Sometimes menu bar disappears. So set it every time.
	 */
	public void setVisible(boolean newVisible) {
		super.setVisible(newVisible);
		super.setMenuBar(this.mbar);
		
		if (newVisible) {
			this.updateContents();
		}
	}
	
	private void updateContents() {
		super.getTextView().clear();
		
		ClipboardHandler handler = ClipboardHandler.getCurrentClipboardHandler();
		if (!handler.isEmpty()) {
			String contents = handler.getStringContents();
			if (contents != null) {
				super.getTextView().appendln(StringUtilities.detab(contents, 4));
			}
		}
	}
	
	public void actionPerformed(ActionEvent evt) {
		String c = evt.getActionCommand();
		if (c.equals("CLOSE")) {
			this.windowListener.windowClosing(
				new WindowEvent(this, WindowEvent.WINDOW_CLOSING)
			);
		} else if (c.equals("SAVE")) {
			String path = SaveDialog.showSaveFileDialog("java.clipboard");
			if (path != null) {
				AutoTypeCreatorFixer.getCurrentFixer().add(new File(path), "TEXT", "ttxt");
				FileUtilities.writeBytesToFile(
					super.getTextView().getContents().getBytes(),
					path
				);
			}
		}
	}
	
}