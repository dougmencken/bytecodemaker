// ===========================================================================
//	BMMain.java (part of douglas.mencken.bm package)
// ===========================================================================

package douglas.mencken.bm;

import com.apple.mrj.MRJOpenDocumentHandler;

import douglas.mencken.bm.menu.BMMenuBar;
import douglas.mencken.bm.frames.BMAbout;
import douglas.mencken.bm.frames.ClassFrame;

import douglas.mencken.tools.*;
import douglas.mencken.exceptions.TooManyInstancesException;
import douglas.mencken.bm.storage.prefs.BMPreferencesManager;

/**
 *	Bytecode Maker's main entry point.
 *
 *	@version 1.05f2
 */

public final class BMMain extends Object {
	
	public static void main(String[] args) {
		//testAndDebug();
		
		if (!BMEnvironment.isMacOS()) {
			UsefulModalDialogs.doInformationDialog(
				"Mac OS required to run this version of Bytecode Maker."
			);
			BMEnvironment.quit();
		}
		
		BMPreferencesManager.readPreferences();
		
		try {
			new ErrOutSetter();
		} catch (TooManyInstancesException ignored) {}
		
		new BMAbout();
		
		BMMenuBar mbar = new BMMenuBar();
		BMEnvironment.initialize((MRJOpenDocumentHandler)mbar);
		
		ClassFrame frame = new ClassFrame();
		frame.setMenuBar(mbar);
		frame.setVisible(true);
	}
	
	/*
	private static void testAndDebug() {
		douglas.mencken.beans.RectWindow rw = new douglas.mencken.beans.RectWindow();
		rw.setBounds(100, 100, 160, 120);
		rw.setVisible(true);
		
		douglas.mencken.beans.TitledRectWindow tw = new douglas.mencken.beans.TitledRectWindow(
			"TEST TitledRectWindow"
		);
		tw.setBounds(300, 100, 300, 150);
		tw.setVisible(true);
	}
	*/
	
}
