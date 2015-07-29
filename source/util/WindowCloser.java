/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.util;

import java.awt.*;
import java.awt.event.*;

/**
 *	Utility class to catch window close events on a target
 *	window and actually dispose the window.
 *
 *	@version 1.01
 */

public class WindowCloser extends Object implements WindowListener {
	
	private boolean exitOnClose;
	
	/**
	 * Create an adaptor to listen for window closing events
	 * on the given window and actually perform the close.
	 */
	public WindowCloser(Window w) {
		this(w, false);
	}
	
	/**
	 * Create an adaptor to listen for window closing events
	 * on the given window and actually perform the close.
	 * If "exitOnClose" is true we do a System.exit on close.
	 */
	public WindowCloser(Window w, boolean exitOnClose) {
		this.exitOnClose = exitOnClose;
		w.addWindowListener(this);
	}
	
	
	public void windowClosing(WindowEvent e) {
		if (exitOnClose) {
			System.exit(0);
		}
		e.getWindow().dispose();
	}
	
	public void windowClosed(WindowEvent e) {
		if (exitOnClose) {
			System.exit(0);
		}
	}
	
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	
}
