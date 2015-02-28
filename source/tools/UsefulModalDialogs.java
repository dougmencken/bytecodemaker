// ===========================================================================
//	UsefulModalDialogs.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import douglas.mencken.icons.*;

/**
 *	<code>UsefulModalDialogs</code>
 *
 *	@version 1.03f1
 */

public final class UsefulModalDialogs extends Object {
	
	public static void doErrorDialog(Throwable t, boolean printStackTrace) {
		ModalDialogs.doDialog(
			new CautionIcon(),	t.getClass().getName() + ": " + t.getMessage()
		);
		
		if (printStackTrace) {
			t.printStackTrace();
		}
	}
	
	public static void doErrorDialog(String message) {
		if (message == null) {
			message = "null";
		}
		
		ModalDialogs.doDialog(new PalmStopIcon(), message);
	}
	
	public static void doWarningDialog(String message) {
		if (message == null) {
			message = "null";
		}
		
		ModalDialogs.doDialog(new NoteIcon(), message);
	}
	
	public static void doInformationDialog(String message) {
		if (message == null) {
			message = "null";
		}
		
		ModalDialogs.doDialog(new NoteIcon(), message);
	}
	
	public static void doCautionDialog(String message) {
		if (message == null) {
			message = "null";
		}
		
		ModalDialogs.doDialog(new CautionIcon(), message, " Stop ");
	}
	
	public static void tellAboutNoMemoryAvailable() {
		ModalDialogs.doDialog(
			new PalmStopIcon(),
			"Not enough memory available",
			" Stop "
		);
	}
	
	public static void tellAboutError(String errorText) {
		UsefulModalDialogs.doErrorDialog(errorText);
	}
	
	public static void tellAboutError(int errorNumber, String errorText) {
		StringBuffer errorDescription = new StringBuffer();
		errorDescription.append('#').append(Integer.toString(errorNumber));
		
		if ((errorText != null) && (errorText.length() != 0)) {
			errorDescription.append(": ").append(errorText);
		}
		
		UsefulModalDialogs.doErrorDialog(errorDescription.toString());
	}
	
	public static void tellAboutInternalError(String message) {
		ModalDialogs.doDialog(
			new NoteIcon(),
			"Internal Error: " + message
		);
	}
	
}