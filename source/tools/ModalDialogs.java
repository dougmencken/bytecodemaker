// ===========================================================================
//	ModalDialogs.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.awt.*;
import douglas.mencken.icons.Icon;
import douglas.mencken.util.InvisibleFrame;

/**
 *	<code>ModalDialogs</code>
 *
 *	@version 1.05f
 */

public final class ModalDialogs extends Object {
	
	private ModalDialogs() { super(); }
	
	private static InvisibleFrame intermediaryParent;
	
	static {
		intermediaryParent = new InvisibleFrame();
	}
	
	public static void doDialog(Icon icon, String message, String buttonLabel) {
		Dialog dialog = ModalDialogMaker.makeModalDialog(
			intermediaryParent,
			icon,
			message,
			new Button(buttonLabel)
		);
		dialog.setVisible(true);
	}
	
	public static void doDialog(Icon icon, String message) {
		doDialog(icon, message, "   OK   ");
	}
	
	public static void doDialog(String message) {
		doDialog(null, message, "   OK   ");
	}
	
	public static int doTwoButtonsDialog(Icon icon, String message, String l1, String l2) {
		Dialog dialog = ModalDialogMaker.makeModalDialog(
			intermediaryParent,
			icon,
			message,
			new Button[] { new Button(l1), new Button(l2) }
		);
		dialog.setVisible(true);
		
		return ModalDialogMaker.getLastPressedButtonNumber();
	}
	
	public static int doThreeButtonsDialog(	Icon icon, String message,
											String l1, String l2, String l3) {
		Dialog dialog = ModalDialogMaker.makeModalDialog(
			intermediaryParent,
			icon,
			message,
			new Button[] { new Button(l1), new Button(l2), new Button(l3) }
		);
		dialog.setVisible(true);
		
		return ModalDialogMaker.getLastPressedButtonNumber();
	}
	
	public static String askForOneString(String in) {
		AskForOneStringDialog dialog = new AskForOneStringDialog(intermediaryParent, in);
		dialog.setVisible(true);
		return dialog.getString();
	}
	
}