// ===========================================================================
// BMEnvironment.java (part of douglas.mencken.bm package)
// ===========================================================================

package douglas.mencken.bm;

import java.io.File;
import com.apple.mrj.MRJApplicationUtils;
import com.apple.mrj.MRJOpenDocumentHandler;

import douglas.mencken.tools.ClipboardHandler;
import douglas.mencken.tools.ErrOutSetter;
import douglas.mencken.tools.QuitHandler;
import douglas.mencken.tools.LogMonitor;
import douglas.mencken.util.TempFileContentsManager;

import douglas.mencken.bm.storage.JavaClass;
import douglas.mencken.bm.frames.*;
import douglas.mencken.bm.menu.*;

/**
 *	The utility class <code>BMEnvironment</code>.
 *	(static methods only)
 *
 *	@version 1.4
 */

public class BMEnvironment extends Object {

	public static final String CURRENT_VERSION = "version A.6";

	private static QuitHandler quitHandler;
	private static JavaClass currentClass;
	
	/**
	 *	You cannot construct a new <code>BMEnvironment</code> object.
	 */
	private BMEnvironment() { super(); }
	
	public static boolean isOnMacOS() {
		return System.getProperty("os.name", "unknown").equals("Mac OS");
	}

	public static boolean isOnMacOSX() {
		return System.getProperty("os.name", "unknown").equals("Mac OS X");
	}
	
	public static boolean isHeadfulSystem() {
		return System.getProperty("java.awt.headless", "false").equals("false");
	}
	
	/**
	 *	Prepares the environment.
	 */
	public static void initialize(MRJOpenDocumentHandler handler) {
		TempFileContentsManager tfcm = new TempFileContentsManager(
			(new File(System.getProperty("user.dir"), "Temporary Files")).getPath(),
			"Temporary Data 1"
		);
		tfcm.setFileTypeAndCreator("rsrc", "ByTe");
		TempFileContentsManager.setCurrentManager(tfcm);
		
		TempFileContentsManager tfcm2 = new TempFileContentsManager(
			(new File(System.getProperty("user.dir"), "Temporary Files")).getPath(),
			"Temporary Data 2"
		);
		tfcm2.setFileTypeAndCreator("rsrc", "ByTe");
		TempFileContentsManager.setSpecialPurposeManager(tfcm2);
		
		TempFileContentsManager.cleanUp();
		
		if (ClipboardHandler.getCurrentClipboardHandler() == null) {
			new ClipboardHandler();
		}
		
		if (quitHandler == null) {
			quitHandler = new QuitHandler();
		}
		
		MRJApplicationUtils.registerOpenDocumentHandler(handler);
		currentClass = null;
	}
	
	/**
	 *	Quit.
	 */
	public static void quit() {
		ErrOutSetter.disposeFrames();
		TempFileContentsManager.cleanUp();
		
		if (quitHandler == null) {
			quitHandler = new QuitHandler();
		}
		quitHandler.handleQuit();
	}

	public static String makeVersionString() {
                return BMEnvironment.CURRENT_VERSION;
        }

	public static JavaClass getCurrentClass() {
		return currentClass;
	}
	
	public static void updateFrames() {
		// JVMInstructionSetEditor.hideAllEditors(); ///// JVMInstructionSetEditor -> JVMInstructionSetEditor
		
		try {
			ClassFrame.getCurrentFrame().updateContents();
		} catch (Exception e) { /* ... */ }
	}
	
	public static void setCurrentClass(JavaClass newClass) {
		currentClass = newClass;
		LogMonitor.clearCurrent();
		
		BMMenuBar mbar = (BMMenuBar)(ClassFrame.getCurrentFrame().getMenuBar());
		mbar.updateMenuBar();
		
		WindowMenu windowMenu = mbar.getWindowMenu();
		windowMenu.hideWindows(false);
	}
	
}
