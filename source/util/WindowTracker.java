// ===========================================================================
//	WindowTracker.java (part of douglas.mencken.util package)
// ===========================================================================

package douglas.mencken.util;

import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *	<code>WindowTracker</code>
 *
 *	@version 1.12f0
 */

public class WindowTracker extends Object implements WindowListener {
	
	protected Hashtable windowTable = new Hashtable();
	protected Hashtable boundsTable = new Hashtable();
	
	/**
	 *	The default constructor.
	 */
	public WindowTracker() {
		super();
	}
	
	public boolean isVisible(String key) {
		return this.windowTable.containsKey(key);
	}
	
	public void showWindow(String key, Window window) {
		this.showWindow(key, window, true);
	}
	
	public void showWindow(String key, Window window, boolean usePreviousBounds) {
		Object oldWindow = windowTable.get(key);
		Object bounds = boundsTable.get(key);
		
		if ((oldWindow != null) && ((Window)oldWindow).isVisible()) {
			// window with this key exists & visible - just bring it to front
			((Window)oldWindow).toFront();
		} else {
			if ((bounds == null) || (!usePreviousBounds)) {
				this.boundsTable.put(key, window.getBounds());
			} else {
				window.setBounds((Rectangle)bounds);
			}
			
			window.setVisible(true);
			window.addWindowListener(this);
			this.windowTable.put(key, window);
		}
	}
	
	/**	Note:	if the window may be closed not only by clicking its
	 *			close box (for example, from menu), add WindowListener
	 *			to your own class:<p>
	 *	<pre>
	 *	public void windowClosing(WindowEvent evt) {
	 *		windowTracker.closeWindow(evt.getWindow());
	 *	}
	 *	</pre>
	 */
	public void closeWindow(String key) {
		Object oldWindow = windowTable.get(key);
		if (oldWindow != null) {
			// update bounds before disposing
			this.boundsTable.put(key, ((Window)oldWindow).getBounds());
			((Window)oldWindow).dispose();
			
			this.windowTable.remove(key);
		}
	}
	
	public void closeWindow(Window window) {
		Enumeration allKeys = windowTable.keys();
		while (allKeys.hasMoreElements()) {
			String key = (String)allKeys.nextElement();
			if ((Window)windowTable.get(key) == window) {
				this.closeWindow(key);
				return;
			}
		}
	}
	
	public void closeAll() {
		Enumeration allKeys = windowTable.keys();
		while (allKeys.hasMoreElements()) {
			String key = (String)allKeys.nextElement();
			Window window = (Window)windowTable.get(key);
			window.dispose();
			
			this.windowTable.remove(key);
		}
	}
	
	public void windowActivated(WindowEvent evt) {}
	public void windowDeactivated(WindowEvent evt) {}
	public void windowIconified(WindowEvent evt) {}
	public void windowDeiconified(WindowEvent evt) {}
	public void windowOpened(WindowEvent evt) {}
	public void windowClosed(WindowEvent evt) {}
	
	public void windowClosing(WindowEvent evt) {
		this.closeWindow(evt.getWindow());
	}
	
}