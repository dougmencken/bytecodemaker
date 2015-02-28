// ===========================================================================
//	InvisibleFrame.java (part of douglas.mencken.util package)
// ===========================================================================

package douglas.mencken.util;

import java.awt.Frame;
import java.awt.Window;

/**
 *	InvisibleFrame is an invisible Frame to be
 *	the owner for Dialogs and Windows.
 *
 *	@version 1.07f2
 */

public class InvisibleFrame extends Frame {
	
	public InvisibleFrame() {
		super("");
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