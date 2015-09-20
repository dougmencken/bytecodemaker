/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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
 *	@version 1.2
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
		if (this.windowTable.containsKey(key)) {
			Object theWindow = this.windowTable.get(key);
			return ((Window)theWindow).isVisible();
		}
		
		return false;
	}
	
	public void showWindow(String key, Window window) {
		this.showWindow(key, window, true);
	}
	
	public void showWindow(String key, Window window, boolean usePreviousBounds) {
		Object curWindow = this.windowTable.get(key);
		Object bounds = this.boundsTable.get(key);
		
		if (curWindow != null) {
			if ( ((Window)curWindow).isVisible() ) {
				// window with this key exists & visible ~ just bring it to front
				((Window)curWindow).toFront();
			} else {
				((Window)curWindow).setVisible(true);
			}
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
	 *		close box (for example, from menu), add WindowListener
	 *		to your own class:<p>
	 *	<pre>
	 *	public void windowClosing(WindowEvent evt) {
	 *		windowTracker.closeWindow(evt.getWindow());
	 *	}
	 *	</pre>
	 */
	public void closeWindow(String key) {
		Object curWindow = windowTable.get(key);
		if (curWindow != null) {
			// update bounds before disposing
			this.boundsTable.put(key, ((Window)curWindow).getBounds());
			((Window)curWindow).dispose();
			
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
	
	public String toString() {
		return	"windowTable:" + windowTable.toString() + "," +
			"boundsTable:" + boundsTable.toString();
	}
	
}