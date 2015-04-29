// ===========================================================================
// MessageDialogMaker.java (part of douglas.mencken.tools package)
//	public class MessageDialogMaker
//	class ButtonSetActionListener
// ===========================================================================

package douglas.mencken.tools;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.icons.*;
import douglas.mencken.util.FontUtilities;
import douglas.mencken.util.WindowUtilities;

/**
 *	<code>MessageDialogMaker</code>
 *	All methods here are 'static'.
 *
 *	@version 1.1
 */

public class MessageDialogMaker extends Object {
	
	private static int lastPressedButtonNumber = 0;
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private MessageDialogMaker() { super(); }
	
	public static Dialog makeMessageDialog(Frame parent, String message, Button button) {
		return makeMessageDialog(parent, null, message, new Button[] { button });
	}
	
	public static Dialog makeMessageDialog(Frame parent, Icon icon, String message, Button button) {
		return makeMessageDialog(parent, icon, message, new Button[] { button });
	}
	
	public static Dialog makeMessageDialog(Frame parent, String message, Button[] buttons) {
		return makeMessageDialog(parent, null, message, buttons);
	}
	
	public static Dialog makeMessageDialog(Frame parent, Icon icon, String message, Button[] buttons) {
		if (parent == null) {
			throw new IllegalArgumentException("parent cannot be null");
		}
		if ((buttons == null) || (buttons.length == 0)) {
			throw new IllegalArgumentException("buttons cannot be null or empty");
		}
		
		Dialog theDialog = null;
		try {
			theDialog = new Dialog(parent, "", true);
		} catch (HeadlessException headless) { return null; }
		if (theDialog == null) return null;
		
		final Dialog theNewOne = theDialog;
		theNewOne.setBackground(Color.white);
		theNewOne.setLayout(new BorderLayout());
		
		Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		int buttonCount = buttons.length;
		for (int i = 0; i < buttonCount; i++) {
			final int copyOf_i = i;
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					theNewOne.dispose();
					MessageDialogMaker.lastPressedButtonNumber = copyOf_i;
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
