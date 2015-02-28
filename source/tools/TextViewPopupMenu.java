// ===========================================================================
//	TextViewPopupMenu.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import douglas.mencken.util.FileUtilities;
import douglas.mencken.io.SaveDialog;
import douglas.mencken.io.AutoTypeCreatorFixer;
import douglas.mencken.beans.TextView;
import douglas.mencken.tools.ClipboardHandler;

/**
 *	<code>TextViewPopupMenu</code>
 *
 *	@version 1.07f4
 */

public class TextViewPopupMenu extends PopupMenu
implements MouseListener, ActionListener {
	
	private TextView textView;
	private String filename;
	
	public TextViewPopupMenu(TextView textView, String filename) {
		this(textView, filename, true);
	}
	
	public TextViewPopupMenu(TextView textView, String filename, boolean withCutClear) {
		this.textView = textView;
		this.filename = filename;
		
		if (ClipboardHandler.getCurrentClipboardHandler() == null) {
			new ClipboardHandler();
		}
		
		String[] popupLabels = null;
		String[] popupCommands = null;
		if (withCutClear) {
			popupLabels = new String[5];
			popupLabels[0] = "Save";
			popupLabels[1] = "-";
			popupLabels[2] = "Cut";
			popupLabels[3] = "Copy";
			popupLabels[4] = "Clear";
			
			popupCommands = new String[5];
			popupCommands[0] = "SAVE";
			popupCommands[1] = null;
			popupCommands[2] = "CUT";
			popupCommands[3] = "COPY";
			popupCommands[4] = "CLEAR";
		} else {
			popupLabels = new String[3];
			popupLabels[0] = "Save";
			popupLabels[1] = "-";
			popupLabels[2] = "Copy";
			
			popupCommands = new String[3];
			popupCommands[0] = "SAVE";
			popupCommands[1] = null;
			popupCommands[2] = "COPY";
		}
		
		for (int i = 0; i < popupLabels.length; i++) {
			MenuItem mi = new MenuItem(popupLabels[i]);
			if (popupCommands[i] != null) {
				mi.setActionCommand(popupCommands[i]);
				mi.addActionListener(this);
			}
			super.add(mi);
		}
		
		textView.addMouseListener(this);
	}
	
	public void setFilename(String fname) {
		this.filename = fname;
	}
	
    public void mousePressed(MouseEvent e) {
    	if (e.isPopupTrigger()) {
			super.show(textView, e.getX(), e.getY());
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		
		if (c.equals("CUT")) {
			ClipboardHandler.getCurrentClipboardHandler().setStringContents(
				textView.getContents()
			);
			textView.clear();
		} else if (c.equals("COPY")) {
			ClipboardHandler.getCurrentClipboardHandler().setStringContents(
				textView.getContents()
			);
		} else if (c.equals("CLEAR")) {
			textView.clear();
		} else if (c.equals("SAVE")) {
			String path = SaveDialog.showSaveFileDialog(filename);
			if (path != null) {
				AutoTypeCreatorFixer.getCurrentFixer().add(new File(path), "TEXT", "ttxt");
				FileUtilities.writeBytesToFile(
					textView.getContents().getBytes(),
					path
				);
			}
		}
	}
	
}