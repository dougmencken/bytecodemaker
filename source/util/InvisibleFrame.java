/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.util;

import java.awt.Frame;
import java.awt.Window;
import java.awt.HeadlessException;

/**
 *	InvisibleFrame is an invisible Frame to be
 *	the owner for Dialogs and Windows.
 *
 *	@version 1.1
 */

public class InvisibleFrame extends Frame {
	
	public InvisibleFrame() throws HeadlessException {
		super("invisible frame");
	}
	
	/**
	 *	Since <i>this frame can never be shown</i>,
	 *	this method is empty.
	 */
	public void show() {}
	
	/**
	 *	Since <i>this frame can never be shown or hidden</i>,
	 *	this method is empty.
	 */
	public void setVisible(boolean newVisible) {}
	
	/**
	 *	Note:	to avoid deadlocks, 'dispose()' is *not* synchronized.
	 */
	public void dispose() {
		try {
			// getToolkit().getSystemEventQueue();
			super.dispose();
		} catch (Exception e) {}
	}
	
}
