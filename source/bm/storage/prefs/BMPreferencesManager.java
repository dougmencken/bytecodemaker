// ===========================================================================
//	BMPreferencesManager.java (part of douglas.mencken.bm.storage.prefs package)
// ===========================================================================

package douglas.mencken.bm.storage.prefs;

import douglas.mencken.util.StringUtilities;

/**
 *	<code>BMPreferencesManager</code> is an interface to set
 *	of Bytecode Maker preferences.
 *	
 *	@version	1.04v
 *	@since		Bytecode Maker 0.5.9
 */

public class BMPreferencesManager extends Object {
	
	public static final String DEFAULT_CLASS_TYPE = "Clss";
	public static final String JDK_CLASS_TYPE_display = "CO\u00E5k";
	public static final String JDK_CLASS_TYPE = "CO\u008Ck";
	
	/**
	 *	No instances of this class can be created.
	 */
	private BMPreferencesManager() { super(); }
	
	private static BMPreferences thePreferences = new BMPreferences();
	
	// --------------------------------------------------------------------------------------
	
	public static BMPreferences getPreferences() {
		return thePreferences;
	}
	
	public static void setPreferences(BMPreferences prefs) {
		if (prefs != thePreferences) {
			thePreferences = prefs;
			thePreferences.save();
		}
	}
	
	// --------------------------------------------------------------------------------------
	
	public static void readPreferences() {
		thePreferences.load();
	}
	
	/**
	 *	Reset all preferences to defaults.
	 */
	public static void useDefaultPreferences() {
		thePreferences.resetToDefaults();
	}
	
	/**
	 *	Show the special dialog to customize the preferences.
	 *	
	 *	@see	douglas.mencken.bm.frames.PreferencesDialog
	 */
	public static void showPreferencesDialog() {
		BMPreferencesDialog prefsDialog = new BMPreferencesDialog();
		prefsDialog.setVisible(true);
	}
	
	// ------------------------------- General Preferences ------------------------------- //
	
	public static String getClassType() {
		return (String)(thePreferences.get("douglas.mencken.bm.class_type"));
	}
	
	public static void setClassType(String newType) {
		if ((newType.equals(DEFAULT_CLASS_TYPE)) || (newType.equals(JDK_CLASS_TYPE))) {
			thePreferences.set("douglas.mencken.bm.class_type", newType);
		} else {
			throw new IllegalArgumentException("illegal class type: " + newType);
		}
	}
	
	public static int getBranchDisplayMode() {
		return Integer.parseInt((String)(thePreferences.get("douglas.mencken.bm.branch_display_mode")));
	}
	
	public static void setBranchDisplayMode(int newval) {
		thePreferences.set("douglas.mencken.bm.branch_display_mode", String.valueOf(newval));
	}
	
	public static boolean getShowProgressBar() {
		return thePreferences.get("douglas.mencken.bm.show_progress_bar").equals("true");
	}
	
	public static void setShowProgressBar(boolean show) {
		thePreferences.set("douglas.mencken.bm.show_progress_bar", (show) ? "true" : "false");
	}
	
	public static boolean getShowMemoryMonitorAtStartup() {
		return thePreferences.get("douglas.mencken.bm.show_memory_monitor").equals("true");
	}
	
	public static void setShowMemoryMonitorAtStartup(boolean use) {
		thePreferences.set("douglas.mencken.bm.show_memory_monitor", (use) ? "true" : "false");
	}
	
	public static boolean getShowLog() {
		return thePreferences.get("douglas.mencken.bm.show_log").equals("true");
	}
	
	public static void setShowLog(boolean use) {
		thePreferences.set("douglas.mencken.bm.show_log", (use) ? "true" : "false");
	}
	
	public static boolean getUseFullyQualifiedNames() {
		return thePreferences.get("douglas.mencken.bm.always_use_fully_qualified_names").equals("true");
	}
	
	public static void setUseFullyQualifiedNames(boolean use) {
		thePreferences.set("douglas.mencken.bm.always_use_fully_qualified_names", (use) ? "true" : "false");
	}
	
	// ----------------------------- Decompiler Preferences ------------------------------ //
	
	public static boolean getUsePackageImport() {
		return thePreferences.get("douglas.mencken.bm.use_package_import").equals("true");
	}
	
	public static void setUsePackageImport(boolean use) {
		thePreferences.set("douglas.mencken.bm.use_package_import", (use) ? "true" : "false");
	}
	
	public static boolean getUsePackageImport_java_sun() {
		return thePreferences.get("douglas.mencken.bm.java_sun_packages").equals("true");
	}
	
	public static void setUsePackageImport_java_sun(boolean use) {
		thePreferences.set("douglas.mencken.bm.java_sun_packages", (use) ? "true" : "false");
	}
	
	public static boolean getUsePackageImportIfMoreThan3Imports() {
		return thePreferences.get("douglas.mencken.bm.more_than_3_imports").equals("true");
	}
	
	public static void setUsePackageImportIfMoreThan3Imports(boolean use) {
		thePreferences.set("douglas.mencken.bm.more_than_3_imports", (use) ? "true" : "false");
	}
	
	// -------------------------------- Recent Used Files -------------------------------- //
	
	public static String[] getRecentUsedFiles() {
		return StringUtilities.tokenize(
			(String)(thePreferences.get("douglas.mencken.bm.recent_used_files")),
			"\n"
		);
	}
	
	public static void addRecentUsedFile(String file) {
		thePreferences.addRecentUsedFile(file);
	}
	
	public static void clearRecentUsedFileList() {
		thePreferences.clearRecentUsedFileList();
	}
	
}
