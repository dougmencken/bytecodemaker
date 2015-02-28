// ===========================================================================
//	ModalDialogMaker.java (part of douglas.mencken.tools package)
//		public class ModalDialogMaker
//		class ButtonSetActionListener
//
// ===========================================================================

package douglas.mencken.tools;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.icons.*;
import douglas.mencken.util.FontUtilities;
import douglas.mencken.util.WindowUtilities;

/**
 *	<code>ModalDialogMaker</code>
 *	All methods here are 'static'.
 *
 *	@version 0.80f2
 */

public class ModalDialogMaker extends Object {
	
	private static int lastPressedButtonNumber = 0;
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private ModalDialogMaker() { super(); }
	
	public static Dialog makeModalDialog(Frame parent, String message, Button button) {
		return makeModalDialog(parent, null, message, new Button[] { button });
	}
	
	public static Dialog makeModalDialog(Frame parent, Icon icon, String message, Button button) {
		return makeModalDialog(parent, icon, message, new Button[] { button });
	}
	
	public static Dialog makeModalDialog(Frame parent, String message, Button[] buttons) {
		return makeModalDialog(parent, null, message, buttons);
	}
	
	public static Dialog makeModalDialog(Frame parent, Icon icon, String message, Button[] buttons) {
		if ((buttons == null) || (buttons.length == 0)) {
			throw new IllegalArgumentException(
				"buttons cannot be null or empty"
			);
		}
		
		final Dialog theNewOne = new Dialog(parent, "", true);
		theNewOne.setBackground(Color.white);
		theNewOne.setLayout(new BorderLayout());
		
		Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		int buttonCount = buttons.length;
		for (int i = 0; i < buttonCount; i++) {
			final int copyOf_i = i;
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					theNewOne.dispose();
					ModalDialogMaker.lastPressedButtonNumber = copyOf_i;
				}
			});
			
			buttonPanel.add(buttons[copyOf_i]);
		}
		
		theNewOne.add("South", buttonPanel);
		
		LabelPanel messagePanel = new LabelPanel(
			message, FontUtilities.createFont(FontUtilities.MONACO_9)
		);
		Panel forMessage = new Panel(new FlowLayout(FlowLayout.CENTER));
		forMessage.add(messagePanel);
		theNewOne.add("Center", forMessage);
		
		if (icon != null) {
			IconCanvas canvas = new IconCanvas(icon);
			theNewOne.add("West", canvas);
		}
		
		theNewOne.setBounds(WindowUtilities.getPreferredDialogBounds(
			messagePanel, (icon != null), buttonPanel
		));
		theNewOne.setResizable(false);
		
		return theNewOne;
	}
	
	public static int getLastPressedButtonNumber() {
		return lastPressedButtonNumber;
	}
	
}