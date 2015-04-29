// ===========================================================================
// DialogFactory.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.awt.*;
import douglas.mencken.icons.Icon;
import douglas.mencken.util.InvisibleFrame;

/**
 *	<code>DialogFactory</code>
 *
 *	@version 1.1
 */

public final class DialogFactory extends Object {
	
	private DialogFactory() { super(); }
	
	private static InvisibleFrame intermediaryParent;
	
	static {
		intermediaryParent = null;
		try {
			intermediaryParent = new InvisibleFrame();
		} catch (HeadlessException hless) {
			/* System.err.println("on headless system!"); */
		}
	}
	
	public static void doDialog(Icon icon, String message, String buttonLabel) {
		Dialog dialog = null;
		if (intermediaryParent != null) {
			dialog = MessageDialogMaker.makeMessageDialog(
				intermediaryParent,
				icon,
				message,
				new Button(buttonLabel)
			);
		}
		if (dialog != null) {
			dialog.setVisible(true);
		}
	}
	
	public static void doDialog(Icon icon, String message) {
		doDialog(icon, message, "   OK   ");
	}
	
	public static void doDialog(String message) {
		doDialog(null, message, "   OK   ");
	}
	
	public static int doTwoButtonsDialog(Icon icon, String message, String l1, String l2) {
		Dialog dialog = null;
		if (intermediaryParent != null) {
			dialog = MessageDialogMaker.makeMessageDialog(
				intermediaryParent,
				icon,
				message,
				new Button[] { new Button(l1), new Button(l2) }
			);
		}
		if (dialog != null) {
			dialog.setVisible(true);
		}
		
		return MessageDialogMaker.getLastPressedButtonNumber();
	}
	
	public static int doThreeButtonsDialog(Icon icon, String message, String l1, String l2, String l3) {
		Dialog dialog = null;
		if (intermediaryParent != null) {
			dialog = MessageDialogMaker.makeMessageDialog(
				intermediaryParent,
				icon,
				message,
				new Button[] { new Button(l1), new Button(l2), new Button(l3) }
			);
		}
		if (dialog != null) {
			dialog.setVisible(true);
		}
		
		return MessageDialogMaker.getLastPressedButtonNumber();
	}
	
	public static String askForOneString(String in) {
		AskForOneStringDialog dialog = null;
		if (intermediaryParent != null) {
			dialog = new AskForOneStringDialog(intermediaryParent, in);
		}
		if (dialog != null) {
			dialog.setVisible(true);
			return dialog.getString();
		} else {
			return "null <dialog>";
		}
	}
	
}
