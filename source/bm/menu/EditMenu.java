/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.menu;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.tools.ClipboardFrame;
import douglas.mencken.tools.ClipboardHandler;
import douglas.mencken.util.MenuUtilities;
import douglas.mencken.bm.storage.prefs.BMPreferencesManager;
import douglas.mencken.bm.BMEnvironment;

/**
 *	Menu <b>Edit</b> for Bytecode Maker.
 *
 *	@version 1.4
 */

public final class EditMenu extends Menu
implements BMMenu, WindowListener {
	
	private static final String[] MENU_DESCR = {
		"Undo", "Z", "EDIT_UNDO_REDO", "f",
		"-", null, null, null,
		"Cut", "X", "EDIT_CUT", "f",
		"Copy", "C", "EDIT_COPY", "f",
		"Paste", "V", "EDIT_PASTE", "f",
		"Clear", "", "EDIT_CLEAR", "f",
		"-", null, null, null,
		"Preferences", ";", "PREFERENCES", "",
		"-", null, null, null,
		"Show/Hide Clipboard", "", "SHOW_HIDE_CLIPBOARD", ""
	};
	
	private ClipboardFrame clipboardWindow;
	
	private MenuItem undoRedo;
	private MenuItem cut;
	private MenuItem copy;
	private MenuItem paste;
	private MenuItem clear;
	private MenuItem prefs;
	private MenuItem showHideClipboard;
	
	public EditMenu() {
		super("Edit");
		this.clipboardWindow = new ClipboardFrame(this);
		MenuUtilities.fillMenuByDesc(EditMenu.MENU_DESCR, this, this);
		
		this.undoRedo = MenuUtilities.findItemByLabel(this, EditMenu.MENU_DESCR[0*4]);
		
		this.cut = MenuUtilities.findItemByLabel(this, EditMenu.MENU_DESCR[2*4]);
		this.copy = MenuUtilities.findItemByLabel(this, EditMenu.MENU_DESCR[3*4]);
		this.paste = MenuUtilities.findItemByLabel(this, EditMenu.MENU_DESCR[4*4]);
		this.clear = MenuUtilities.findItemByLabel(this, EditMenu.MENU_DESCR[5*4]);
		
		this.prefs = MenuUtilities.findItemByLabel(this, EditMenu.MENU_DESCR[7*4]);
		
		this.showHideClipboard = MenuUtilities.findItemByLabel(this, EditMenu.MENU_DESCR[9*4]);
		
		this.updateMenu();
	}
	
	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();
		
		if (command.equals("EDIT_UNDO_REDO")) {
			this.undoRedo();
		} else if (command.equals("EDIT_CUT")) {
			this.cut();
		} else if (command.equals("EDIT_COPY")) {
			this.copy();
		} else if (command.equals("EDIT_PASTE")) {
			this.paste();
		} else if (command.equals("EDIT_CLEAR")) {
			this.clear();
		} else if (command.equals("PREFERENCES")) {
			this.doPreferences();
		} else if (command.equals("SHOW_HIDE_CLIPBOARD")) {
			this.toggleClipboard();
		}
	}
	
// ---- /*** IN PROGRESS ***/ ---- //
	private void undoRedo() {
		if (undoRedo.getLabel().equals("Undo")) {
			undoRedo.setLabel("Redo");
			// ...
		} else {
			undoRedo.setLabel("Undo");
			// ...
		}
	}
	
// ---- /*** IN PROGRESS ***/ ---- //
	private void cut() {}
	private void copy() {}
	private void paste() {}
	private void clear() {}
	
	private void doPreferences() {
		BMPreferencesManager.showPreferencesDialog();
	}
	
	private void toggleClipboard() {
		this.clipboardWindow.setVisible(!this.clipboardWindow.isVisible());
		this.updateMenu();
	}
	
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	
	public void windowClosing(WindowEvent evt) {
		if (evt.getWindow() == this.clipboardWindow) {
			this.clipboardWindow.setVisible(false);
			this.updateMenu();
		}
	}
	
	public void updateMenu() {
		if (this.clipboardWindow.isVisible()) {
			this.showHideClipboard.setLabel("Hide Clipboard");
		} else {
			this.showHideClipboard.setLabel("Show Clipboard");
		}
		
		// FIXME: current class as switch to enable all edit commands
		if (BMEnvironment.getCurrentClass() == null) {
			this.undoRedo.setLabel("Undo");
			this.undoRedo.setEnabled(false);
			
			this.cut.setEnabled(false);
			this.copy.setEnabled(false);
			this.paste.setEnabled(false);
			
			this.clear.setEnabled(false);
		} else {
			this.undoRedo.setLabel("Undo");
			this.undoRedo.setEnabled(true);
			
			this.cut.setEnabled(true);
			this.copy.setEnabled(true);
			if (!ClipboardHandler.getCurrentClipboardHandler().isEmpty()) {
				this.paste.setEnabled(true);
			}
			
			this.clear.setEnabled(true);
		}
	}
	
}
