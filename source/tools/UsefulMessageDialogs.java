// ===========================================================================
//	UsefulMessageDialogs.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import douglas.mencken.icons.*;

/**
 *	<code>UsefulMessageDialogs</code>
 *
 *	@version 1.03f1
 */

public final class UsefulMessageDialogs extends Object {
	
	public static void doErrorDialog(Throwable t, boolean printStackTrace) {
		DialogFactory.doDialog(
			new CautionIcon(), t.getClass().getName() + ": " + t.getMessage()
		);
		
		if (printStackTrace) {
			t.printStackTrace();
		}
	}
	
	public static void doErrorDialog(String message) {
		if (message == null) {
			message = "null";
		}
		
		DialogFactory.doDialog(new PalmStopIcon(), message);
	}
	
	public static void doWarningDialog(String message) {
		if (message == null) {
			message = "null";
		}
		
		DialogFactory.doDialog(new NoteIcon(), message);
	}
	
	public static void doInformationDialog(String message) {
		if (message == null) {
			message = "null";
		}
		
		DialogFactory.doDialog(new NoteIcon(), message);
	}
	
	public static void doCautionDialog(String message) {
		if (message == null) {
			message = "null";
		}
		
		DialogFactory.doDialog(new CautionIcon(), message, " Stop ");
	}
	
	public static void tellAboutNoMemoryAvailable() {
		DialogFactory.doDialog(
			new PalmStopIcon(),
			"Not enough memory available",
			" Stop "
		);
	}
	
	public static void tellAboutError(String errorText) {
		UsefulMessageDialogs.doErrorDialog(errorText);
	}
	
	public static void tellAboutError(int errorNumber, String errorText) {
		StringBuffer errorDescription = new StringBuffer();
		errorDescription.append('#').append(Integer.toString(errorNumber));
		
		if ((errorText != null) && (errorText.length() != 0)) {
			errorDescription.append(": ").append(errorText);
		}
		
		UsefulMessageDialogs.doErrorDialog(errorDescription.toString());
	}
	
	public static void tellAboutInternalError(String message) {
		DialogFactory.doDialog(
			new NoteIcon(),
			"Internal Error: " + message
		);
	}
	
}
