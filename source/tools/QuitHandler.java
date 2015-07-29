/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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
 *	@version 1.2
 */

public class QuitHandler extends Object
implements ActionListener, MRJQuitHandler {

	public static final int EXIT_SUCCESS = 0;
	public static final int EXIT_FAILURE = -1;

	public QuitHandler() {
		MRJApplicationUtils.registerQuitHandler(this);
	}
	
	public void handleQuit() {
		System.setOut(null);
		System.setErr(null);
		
		if (System.getProperty("java.awt.headless", "false").equals("false")) {
			new LabelWindow(
				"Cleaning up...", new Font("Sand", Font.PLAIN, 18)
			).setVisible(true);
		}
		
		Runtime.getRuntime().runFinalizersOnExit(true);
		System.exit(EXIT_SUCCESS);
	}
	
	public void actionPerformed(ActionEvent e) {
		this.handleQuit();
	}
	
}
