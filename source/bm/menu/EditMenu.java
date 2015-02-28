// ===========================================================================
//	EditMenu.java (part of douglas.mencken.bm.menu package)
// ===========================================================================

package douglas.mencken.bm.menu;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.tools.ClipboardFrame;
import douglas.mencken.tools.ClipboardHandler;
import douglas.mencken.bm.storage.prefs.BMPreferencesManager;
import douglas.mencken.bm.BMEnvironment;

/**
 *	<code>EditMenu</code>
 *
 *	@version 1.35f4
 */

public final class EditMenu extends Menu
implements BMMenu, WindowListener {
	
	private ClipboardFrame clipboardWindow;
	
	private MenuItem undoRedo;
	private MenuItem cut;
	private MenuItem copy;
	private MenuItem paste;
	private MenuItem clear;
	private MenuItem prefs;
	private MenuItem showClipboard;
	
	public EditMenu() {
		super("Edit");
		
		undoRedo = new MenuItem("Undo", new MenuShortcut(KeyEvent.VK_Z));
		undoRedo.setActionCommand("EDIT_UNDO_REDO");
		undoRedo.addActionListener(this);
		super.add(undoRedo);
		
		super.addSeparator(); 
		
		cut = new MenuItem("Cut", new MenuShortcut(KeyEvent.VK_X));
		cut.setActionCommand("EDIT_CUT");
		cut.addActionListener(this);
		super.add(cut);
		
		copy = new MenuItem("Copy", new MenuShortcut(KeyEvent.VK_C));
		copy.setActionCommand("EDIT_COPY");
		copy.addActionListener(this);
		super.add(copy);
		
		paste = new MenuItem("Paste", new MenuShortcut(KeyEvent.VK_V));
		paste.setActionCommand("EDIT_PASTE");
		paste.addActionListener(this);
		super.add(paste);
		
		clear = new MenuItem("Clear");
		clear.setActionCommand("EDIT_CLEAR");
		clear.addActionListener(this);
		super.add(clear);
		
		super.addSeparator(); 
		
		prefs = new MenuItem("Preferences", new MenuShortcut(KeyEvent.VK_SEMICOLON));
		prefs.setActionCommand("PREFERENCES");
		prefs.addActionListener(this);
		super.add(prefs);
		
		super.addSeparator();
		
		this.clipboardWindow = new ClipboardFrame(this);
		
		showClipboard = new MenuItem("Show Clipboard");
		showClipboard.setActionCommand("SHOW_CLIPBOARD");
		showClipboard.addActionListener(this);
		super.add(showClipboard);
		
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
			this.preferences();
		} else if (command.equals("SHOW_CLIPBOARD")) {
			this.showClipboard();
		}
	}
	
/*** UNDO/REDO IN PROGRESS ***/
	private void undoRedo() {
		if (undoRedo.getLabel().equals("Undo")) {
			undoRedo.setLabel("Redo");
			// ...
		} else {
			undoRedo.setLabel("Undo");
			// ...
		}
	}
	
	private void cut() {}
	private void copy() {}
	private void paste() {}
	private void clear() {}
	
	private void preferences() {
		BMPreferencesManager.showPreferencesDialog();
	}
	
	private void showClipboard() {
		if (showClipboard.getLabel().startsWith("Show")) {
			clipboardWindow.setVisible(true);
			showClipboard.setLabel("Hide Clipboard");
		} else {
			clipboardWindow.setVisible(false);
			showClipboard.setLabel("Show Clipboard");
		}
	}
	
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	
	public void windowClosing(WindowEvent e) {
		if (e.getWindow() == clipboardWindow) {
			clipboardWindow.dispose();
			showClipboard.setLabel("Show Clipboard");
		}
	}
	
	public void updateMenu() {
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
