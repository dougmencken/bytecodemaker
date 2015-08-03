/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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
 *	@version 1.2
 */

public final class BMMain extends Object {
	
	public static void main(String[] args) {
		//--------- testAndDebug();
		if (!BMEnvironment.isOnMacOS() && !BMEnvironment.isOnMacOSX()) {
			String msg =	"You're on \"" + System.getProperty("os.name", "unknown") +
					"\" but Mac OS X or Mac OS is required to run this version of Bytecode Maker.";
			System.out.println(msg);
			if (BMEnvironment.isHeadfulSystem()) {
				UsefulMessageDialogs.doInfoDialog(msg);
			}
			BMEnvironment.quit();
		}
		
		System.setProperty("apple.awt.application.name", "Bytecode Maker");
		
		BMPreferencesManager.readPreferences();
		
		try {
			new ErrOutSetter();
		} catch (TooManyInstancesException ignored) {}
		
		new BMAbout();
		
		BMMenuBar mbar = new BMMenuBar();
		BMEnvironment.initialize((MRJOpenDocumentHandler)mbar);
		
		ClassFrame classFrame = new ClassFrame();
		classFrame.setMenuBar(mbar);
		classFrame.setVisible(true);

		if (BMPreferencesManager.getShowMemoryMonitorAtStartup()) {
			mbar.getWindowMenu().showHideMemoryMonitor();
			classFrame.toFront();
		}
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
