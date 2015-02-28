// ===========================================================================
//	CancelKeyListener.java (part of douglas.mencken.util.event package)
// ===========================================================================

package douglas.mencken.util.event;

import java.awt.Window;
import java.awt.event.*;

/**
 *	<code>CancelKeyListener</code>
 *
 *	@version 1.02f
 */

public class CancelKeyListener extends Object implements KeyListener {
	
	protected String cancelDescription;
	protected int cancel_key;
	private CancelListener listener;
	
	public CancelKeyListener() {
		this.cancel_key = KeyEvent.VK_PERIOD;
	}
	
	public void addCancelListener(CancelListener listener) {
		this.listener = listener;
	}
	
	public void removeCancelListener(CancelListener listener) {
		if (this.listener == listener) {
			this.listener = null;
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (e.isMetaDown()) {
			if (code == cancel_key) {
				if (listener != null) {
					listener.operationCanceled(new CancelEvent(this));
				}
			}
		}
	}
	
	public int getCancelKey() { return cancel_key; }
	public void setCancelKey(int key) { this.cancel_key = key; }
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
}