// ===========================================================================
//	FileMenu.java (part of douglas.mencken.bm.menu package)
// ===========================================================================

package douglas.mencken.bm.menu;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.apple.mrj.MRJOpenDocumentHandler;

import douglas.mencken.io.*;
import douglas.mencken.util.event.CancelEvent;
import douglas.mencken.util.ClassUtilities;
import douglas.mencken.util.FileUtilities;

import douglas.mencken.beans.support.NewBeanCustomizerDialog;

import douglas.mencken.tools.ModalDialogs;
import douglas.mencken.tools.LogMonitor;
import douglas.mencken.tools.UsefulModalDialogs;
import douglas.mencken.tools.zip.*;

import douglas.mencken.bm.engine.RandomAccessJavaClass;
import douglas.mencken.bm.storage.JavaClass;
import douglas.mencken.bm.frames.ClassFrame;

import douglas.mencken.bm.storage.prefs.BMPreferencesManager;
import douglas.mencken.bm.BMEnvironment;

/**
 *	Menu <b>File</b>.
 *
 *	@version 1.56f5
 */

public final class FileMenu extends Menu
implements BMMenu, MRJOpenDocumentHandler {
	
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
		
		newClass = new MenuItem("New...", new MenuShortcut(KeyEvent.VK_N));
		newClass.setActionCommand("NEW");
		newClass.addActionListener(this);
		super.add(newClass);
		
		open = new MenuItem("Open...", new MenuShortcut(KeyEvent.VK_O));
		open.setActionCommand("OPEN");
		open.addActionListener(this);
		super.add(open);
		
		openRecent = new Menu("Open Recent");
		openRecent.setEnabled(false);
		super.add(openRecent);
		
		close = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
		close.setActionCommand("CLOSE");
		close.addActionListener(this);
		super.add(close);
		
		super.addSeparator();
		
		save = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
		save.setActionCommand("SAVE");
		save.addActionListener(this);
		super.add(save);
		
		saveAs = new MenuItem("Save As...");
		saveAs.setActionCommand("SAVE_AS");
		saveAs.addActionListener(this);
		super.add(saveAs);
		
		revert = new MenuItem("Revert...");
		revert.setActionCommand("REVERT");
		revert.addActionListener(this);
		super.add(revert);
		
		super.addSeparator();
		
		MenuItem quitItem = new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_Q));
		quitItem.setActionCommand("QUIT");
		quitItem.addActionListener(this);
		super.add(quitItem);
		
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
		
		String[] recentUsedFiles = BMPreferencesManager.getRecentUsedFiles();
		this.openRecent.removeAll();
		this.openRecent.setEnabled(false);
		
		if ((recentUsedFiles != null) && (recentUsedFiles.length != 0)) {
			int len = recentUsedFiles.length;
			
			File[] recent = new File[len];
			for (int i = 0; i < len; i++) {
				recent[i] = (recentUsedFiles[i] == null) ? null : new File(recentUsedFiles[i]);
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
				UsefulModalDialogs.doErrorDialog(
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
			String[] recentUsedFiles = BMPreferencesManager.getRecentUsedFiles();
			int index = Integer.parseInt(command.substring(7));
			this.handleOpenFile(new File(recentUsedFiles[index]));
		} else if (command.equals("CLEAR_OPEN_RECENT_MENU")) {
			BMPreferencesManager.clearRecentUsedFileList();
			this.updateMenu();
		}
	}
	
	private void newClass() {
		NewBeanCustomizerDialog newDialog = new NewBeanCustomizerDialog(
			"Class", JavaClass.class
		);
		newDialog.setVisible(true);
		
		if (newDialog.getBean() != null) {
			JavaClass newJavaClass = (JavaClass)newDialog.getBean();
			JavaClass.addDefaultConstructor(newJavaClass);
			LogMonitor.showCurrent();
			
			if (newJavaClass != null) {
				BMEnvironment.setCurrentClass(newJavaClass);
				ClassFrame.getCurrentFrame().setClass(newJavaClass);
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
		} catch (IncompleteException ignored) {}
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
			
			BMPreferencesManager.addRecentUsedFile(path);
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
			int revert_no = ModalDialogs.doTwoButtonsDialog(
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
			BMPreferencesManager.addRecentUsedFile(path);
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
					if (clazz.getFormatVersion() != 45.3f) {
						if (BMPreferencesManager.getShowLog()) {
							LogMonitor.addToCurrentLog(
								"class format version is not 45.3"
							);
						}
					}
					
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
					UsefulModalDialogs.doErrorDialog(
						"FileMenu.openFile() caught " +
						ClassUtilities.getClassName(ioe.getClass().getName()) + 
						": " + ioe.getMessage()
					);
				} catch (Throwable t) {
					UsefulModalDialogs.doErrorDialog(t, true);
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
					UsefulModalDialogs.doWarningDialog("Unknown zip file format");
				}
			} else {
				// ... SHOW TEXT EDITOR etc.
			}
		}
		
		return success;
	}
	
}