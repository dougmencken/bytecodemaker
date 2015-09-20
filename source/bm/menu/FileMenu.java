/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.menu;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.apple.mrj.MRJOpenDocumentHandler;

import douglas.mencken.io.*;
import douglas.mencken.util.event.CancelEvent;
import douglas.mencken.util.ClassUtilities;
import douglas.mencken.util.FileUtilities;
import douglas.mencken.util.MenuUtilities;

import douglas.mencken.beans.support.NewBeanCustomizerDialog;

import douglas.mencken.tools.DialogFactory;
import douglas.mencken.tools.LogMonitor;
import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.tools.zip.*;

import douglas.mencken.bm.engine.RandomAccessJavaClass;
import douglas.mencken.bm.storage.JavaClass;
import douglas.mencken.bm.storage.JavaClassMaker;
import douglas.mencken.bm.frames.ClassFrame;

import douglas.mencken.bm.storage.prefs.BMPreferencesManager;
import douglas.mencken.bm.BMEnvironment;

/**
 *	Menu <b>File</b> for Bytecode Maker.
 *
 *	@version 1.71
 */

public final class FileMenu extends Menu
implements BMMenu, MRJOpenDocumentHandler {

	private static final String[] MENU_DESCR = {
		"New...", "N", "NEW", "",
		"Open...", "O", "OPEN", "",
		">Open Recent", null, null, "f",
		"Close", "W", "CLOSE", "f",
		"-", null, null, null,
		"Save", "S", "SAVE", "f",
		"Save As...", "", "SAVE_AS", "f",
		"Revert...", "", "REVERT", "f",
		"-", null, null, null,
		"Quit", "Q", "QUIT", ""
	};
	
	private MenuItem newClass;
	private MenuItem open;
	private Menu openRecent;
	private MenuItem close;
	private MenuItem save;
	private MenuItem saveAs;
	private MenuItem revert;
	
	private ZipCatalogFrame zipCatalog;
	private String revertPath = "";
	
	public FileMenu() {
		super("File");
		MenuUtilities.fillMenuByDesc(FileMenu.MENU_DESCR, this, this);
		
		this.newClass = MenuUtilities.findItemByLabel(this, FileMenu.MENU_DESCR[0*4]);
		this.open = MenuUtilities.findItemByLabel(this, FileMenu.MENU_DESCR[1*4]);
		this.openRecent = (Menu)MenuUtilities.findItemByLabel(this, FileMenu.MENU_DESCR[2*4]);
		this.close = MenuUtilities.findItemByLabel(this, FileMenu.MENU_DESCR[3*4]);
		
		this.save = MenuUtilities.findItemByLabel(this, FileMenu.MENU_DESCR[5*4]);
		this.saveAs = MenuUtilities.findItemByLabel(this, FileMenu.MENU_DESCR[6*4]);
		this.revert = MenuUtilities.findItemByLabel(this, FileMenu.MENU_DESCR[7*4]);
		
		this.updateMenu();
	}
	
	public void updateMenu() {
		if (BMEnvironment.getCurrentClass() == null) {
			close.setEnabled(false);
			save.setEnabled(false);
			saveAs.setEnabled(false);
			
			this.revertPath = "";
		} else {
			close.setEnabled(true);
			save.setEnabled(true);
			saveAs.setEnabled(true);
		}
		
		String[] recentlyUsedFiles = BMPreferencesManager.getRecentlyUsedFiles();
		this.openRecent.removeAll();
		this.openRecent.setEnabled(false);
		
		if ((recentlyUsedFiles != null) && (recentlyUsedFiles.length != 0)) {
			int len = recentlyUsedFiles.length;
			
			File[] recent = new File[len];
			for (int i = 0; i < len; i++) {
				recent[i] = (recentlyUsedFiles[i] == null) ? null : new File(recentlyUsedFiles[i]);
			}
			
			boolean[] includeFullPath = new boolean[len];
			for (int i = 0; i < len; i++) {
				for (int j = (i+1); j < len; j++) {
					if ((i != j) && (recent[i] != null) && (recent[j] != null)) {
						if (recent[i].getName().equals(recent[j].getName())) {
							includeFullPath[i] = true;
							includeFullPath[j] = true;
							break;
						}
					}
				}
			}
			
			for (int i = len-1; i >= 0; i--) {
				File current = recent[i];
				if (current != null) {
					String addWhat = (includeFullPath[i]) ?
						(current.getPath()).replace('/', ':').substring(1) :
						current.getName();
					MenuItem recentFile = new MenuItem(addWhat);
					recentFile.setActionCommand("RECENT_" + i);
					recentFile.addActionListener(this);
					this.openRecent.add(recentFile);
				}
			}
			
			this.openRecent.setEnabled(true);
			this.openRecent.addSeparator();
		}
		
		MenuItem clearOpenRecentMenu = new MenuItem("Clear Menu");
		clearOpenRecentMenu.setActionCommand("CLEAR_OPEN_RECENT_MENU");
		clearOpenRecentMenu.addActionListener(this);
		this.openRecent.add(clearOpenRecentMenu);
		
		this.revert.setEnabled(this.revertPath.length() > 0);
	}
	
	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();
		
		if (command.equals("ZIP_CATALOG")) {
			try {
				InputStream is = zipCatalog.getSelectedInputStream();
				this.readFromInputStream(is);
			} catch (Exception exc) {
				Toolkit.getDefaultToolkit().beep();
				UsefulMessageDialogs.doErrorDialog(
			"FileMenu.actionPerformed(ActionEvent): readFromInputStream caught an Exception (" +
			exc.getClass().getName() + "): " + exc.getMessage()
				);
			}
			
			return;
		}
		
		if (command.equals("NEW")) {
			this.newClass();
		} else if (command.equals("OPEN")) {
			this.open();
		} else if (command.equals("CLOSE")) {
			this.close();
		} else if (command.equals("SAVE")) {
			this.save();
		} else if (command.equals("SAVE_AS")) {
			this.saveAs();
		} else if (command.equals("REVERT")) {
			this.revert();
		} else if (command.equals("QUIT")) {
			BMEnvironment.quit();
		} else if (command.startsWith("RECENT")) {
			String[] recentlyUsedFiles = BMPreferencesManager.getRecentlyUsedFiles();
			int index = Integer.parseInt(command.substring(7));
			this.handleOpenFile(new File(recentlyUsedFiles[index]));
		} else if (command.equals("CLEAR_OPEN_RECENT_MENU")) {
			BMPreferencesManager.clearRecentlyUsedFileList();
			this.updateMenu();
		}
	}
	
	private void newClass() {
		NewBeanCustomizerDialog customizer =
			new NewBeanCustomizerDialog("Class", JavaClass.class);
		customizer.setVisible(true);
		
		if (customizer.getBean() != null) {
			JavaClass newClass = (JavaClass)customizer.getBean();
			JavaClassMaker.addDefaultConstructor(newClass);
			LogMonitor.showCurrent();
			
			if (newClass != null) {
				BMEnvironment.setCurrentClass(newClass);
				ClassFrame.getCurrentFrame().setClass(newClass);
			}
		}
		
		this.revertPath = "";
	}
	
	private void open() {
		String path = OpenDialog.showOpenFileDialog(new JavaFilenameFilter());
		if (path != null) {
			this.handleOpenFile(new File(path));
			this.revertPath = path;
		}
		
		this.updateMenu();
	}
	
	public void close() {
		BMEnvironment.setCurrentClass(null);
		
		try {
			ClassFrame.getCurrentFrame().setClass(null);
		} catch (Exception ignored) { /* ..... */ }
	}
	
	/* IN PROGRESS */
	private void save() {
		// ...
		
		/* Save As... */ this.saveAs();
	}
	
	private void saveAs() {
		JavaClass currentClass = BMEnvironment.getCurrentClass();
		ByteArrayOStream out = new ByteArrayOStream();
		
		try {
			RandomAccessJavaClass classio = new RandomAccessJavaClass(out);
			classio.writeJavaClass(currentClass);
		} catch (IOException ioe) {}
		
		String name = ClassUtilities.getClassName(currentClass.getClassName()) + ".class";
		String path = SaveDialog.showSaveFileDialog(name, "Save As");
		
		if (path != null) {
			AutoTypeCreatorFixer.getCurrentFixer().add(
				new File(path), BMPreferencesManager.getClassType(), "ByTe"
			);
			FileUtilities.writeBytesToFile(out.toByteArray(), path);
			
			BMPreferencesManager.addRecentlyUsedFile(path);
			this.revertPath = path;
			revert.setEnabled(false);
			
			this.updateMenu();
		}
	}
	
	private void revert() {
		if (this.revertPath.length() == 0) {
			Toolkit.getDefaultToolkit().beep();
			revert.setEnabled(false);
		} else {
			// ask "are you sure?"
			String filename = (new File(this.revertPath)).getName();
			int revert_no = DialogFactory.doTwoButtonsDialog(
				null, "Do you want to throw away all unsaved changes to \"" + filename + "\"?",
				"Revert", "Cancel"
			);
			
			if (revert_no == 0) {
				this.handleOpenFile(new File(this.revertPath));
				// revert.setEnabled(false);
			}
		}
	}
	
	public void handleOpenFile(File file) {
		if (this.readFromFile(file)) {
			String path = file.getPath();
			BMPreferencesManager.addRecentlyUsedFile(path);
			this.revertPath = path;
			this.updateMenu();
		}
	}
	
	private boolean readFromInputStream(InputStream is) {
		try {
			if (is != null) {
				return this.readBytes(FileUtilities.getByteArray(is), null);
			}
		} catch (IOException ioe) {}
		
		return false;
	}
	
	private boolean readFromFile(File file) {
		try {
			if (file != null) {
				return this.readBytes(FileUtilities.getByteArray(file), file.getPath());
			}
		} catch (IOException ioe) {}
		
		return false;
	}
	
	private boolean readBytes(byte[] data, String path) {
		boolean success = false;
		
		if (data != null) {
			if (FileUtilities.isClassFile(data)) {
				try {
					RandomAccessJavaClass classio = new RandomAccessJavaClass(
						new ByteArrayIStream(data)
					);
					JavaClass clazz = classio.readJavaClass();
					String className = clazz.getClassName();
					
					// check if class format version is not 45.3
/////					if (clazz.getFormatVersion() != 45.3f) {
/////						if (BMPreferencesManager.getShowLog()) {
/////							LogMonitor.addToCurrentLog(
/////								"class format version is not 45.3"
/////							);
/////						}
/////					}
					
					// check if class and file names doesn't match
					if (path != null) {
						String filename = (new File(path)).getName();
						String classname = ClassUtilities.getClassName(className) + ".class";
						
						if (!filename.equals(classname)) {
							if (filename.length() != 31) {
								if (BMPreferencesManager.getShowLog()) {
									LogMonitor.addToCurrentLog(
										"class (" + classname + ") and file (" + filename +
										") names doesn't match"
									);
								}
							}
						}
					}
					
					if (BMPreferencesManager.getShowLog()) {
						LogMonitor.showCurrent();
					}
					
					ClassFrame.getCurrentFrame().setClass(clazz);
					BMEnvironment.setCurrentClass(clazz);
					
					success = true;
				} catch (IOException ioe) {
					UsefulMessageDialogs.doErrorDialog(
						"FileMenu.readBytes ~ caught " +
						ClassUtilities.getClassName(ioe.getClass().getName()) + 
						": " + ioe.getMessage()
					);
				} catch (Throwable t) {
					UsefulMessageDialogs.doErrorDialog(t, true);
				}
			} else if (FileUtilities.isZipArchive(data)) {
				try {
					if (path != null) {
						this.zipCatalog = new ZipCatalogFrame(path);
						zipCatalog.setActionCommand("ZIP_CATALOG");
						zipCatalog.addActionListener(this);
						zipCatalog.setVisible(true);
						
						success = true;
					}
				} catch (Exception e) {
					UsefulMessageDialogs.doWarningDialog("Unknown zip file format");
				}
			} else {
				/* ... TODO: text editor for text files ... */
			}
		}
		
		return success;
	}
	
}
