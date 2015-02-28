// ===========================================================================
//	QuitHandler.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.apple.mrj.MRJApplicationUtils;
import com.apple.mrj.MRJQuitHandler;
import douglas.mencken.beans.LabelWindow;

/**
 *	<code>QuitHandler</code>
 *
 *	@version 1.01f3
 */

public class QuitHandler extends Object
implements ActionListener, MRJQuitHandler {
	
	public QuitHandler() {
		MRJApplicationUtils.registerQuitHandler(this);
	}
	
	public void handleQuit() {
		System.setOut(null);
		System.setErr(null);
		
		LabelWindow quitWindow = new LabelWindow(
			"Cleaning up...", new Font("Sand", Font.PLAIN, 18)
		);
		quitWindow.setVisible(true);
		
		Runtime.getRuntime().runFinalizersOnExit(true);
		System.exit(0);
	}
	
	public void actionPerformed(ActionEvent e) {
		this.handleQuit();
	}
	
}