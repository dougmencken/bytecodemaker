// ===========================================================================
// BMPreferencesManager.java (part of douglas.mencken.bm.storage.prefs package)
// ===========================================================================

package douglas.mencken.bm.storage.prefs;

import douglas.mencken.util.StringUtilities;

/**
 *	<code>BMPreferencesManager</code> is interface to set
 *	of preferences for Bytecode Maker.
 *	
 *	@version	1.1
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
	 *	@see	douglas.mencken.bm.storage.prefs.BMPreferencesDialog
	 */
	public static void showPreferencesDialog() {
		BMPreferencesDialog prefsDialog = new BMPreferencesDialog();
		prefsDialog.setVisible(true);
	}
	
	// ------------------------------- General Preferences ------------------------------- //
	
	public static String getClassType() {
		return thePreferences.getPreference(BMPreferences.PREF_SAVE_CLASS_TYPE);
	}
	
	public static void setClassType(String newType) {
		if ((newType.equals(DEFAULT_CLASS_TYPE)) || (newType.equals(JDK_CLASS_TYPE))) {
			thePreferences.setPreference(BMPreferences.PREF_SAVE_CLASS_TYPE, newType);
		} else {
			throw new IllegalArgumentException("illegal class type: " + newType);
		}
	}
	
	public static int getBranchDisplayMode() {
		return Integer.parseInt(thePreferences.getPreference(BMPreferences.PREF_BRANCH_DISPLAY_MODE));
	}
	
	public static void setBranchDisplayMode(int newval) {
		thePreferences.setPreference(BMPreferences.PREF_BRANCH_DISPLAY_MODE, String.valueOf(newval));
	}
	
	public static boolean getShowProgressBar() {
		return thePreferences.getPreference(BMPreferences.PREF_SHOW_PROGRESS_BAR).equals("true");
	}
	
	public static void setShowProgressBar(boolean show) {
		thePreferences.setPreference(BMPreferences.PREF_SHOW_PROGRESS_BAR, (show) ? "true" : "false");
	}
	
	public static boolean getShowMemoryMonitorAtStartup() {
		return thePreferences.getPreference(BMPreferences.PREF_SHOW_MEMORY_MONITOR).equals("true");
	}
	
	public static void setShowMemoryMonitorAtStartup(boolean use) {
		thePreferences.setPreference(BMPreferences.PREF_SHOW_MEMORY_MONITOR, (use) ? "true" : "false");
	}
	
	public static boolean getShowLog() {
		return thePreferences.getPreference(BMPreferences.PREF_SHOW_LOG_WINDOW).equals("true");
	}
	
	public static void setShowLog(boolean use) {
		thePreferences.setPreference(BMPreferences.PREF_SHOW_LOG_WINDOW, (use) ? "true" : "false");
	}
	
	public static boolean getUseFullyQualifiedNames() {
		return thePreferences.getPreference(BMPreferences.PREF_QUALIFIED_NAMES_ONLY).equals("true");
	}
	
	public static void setUseFullyQualifiedNames(boolean use) {
		thePreferences.setPreference(BMPreferences.PREF_QUALIFIED_NAMES_ONLY, (use) ? "true" : "false");
	}
	
	// ----------------------------- Decompiler Preferences ------------------------------ //
	
	public static boolean getUsePackageImport() {
		return thePreferences.getPreference(BMPreferences.PREF_USE_PACKAGE_IMPORT).equals("true");
	}
	
	public static void setUsePackageImport(boolean use) {
		thePreferences.setPreference(BMPreferences.PREF_USE_PACKAGE_IMPORT, (use) ? "true" : "false");
	}
	
	public static boolean getUsePackageImport_java_sun() {
		return thePreferences.getPreference(BMPreferences.PREF_JAVA_SUN_PACKAGES).equals("true");
	}
	
	public static void setUsePackageImport_java_sun(boolean use) {
		thePreferences.setPreference(BMPreferences.PREF_JAVA_SUN_PACKAGES, (use) ? "true" : "false");
	}
	
	public static boolean getUsePackageImportIfMoreThan3Imports() {
		return thePreferences.getPreference(BMPreferences.PREF_MORE_THAN_3_IMPORTS).equals("true");
	}
	
	public static void setUsePackageImportIfMoreThan3Imports(boolean use) {
		thePreferences.setPreference(BMPreferences.PREF_MORE_THAN_3_IMPORTS, (use) ? "true" : "false");
	}
	
	// -------------------------------- Recent Used Files -------------------------------- //
	
	public static String[] getRecentlyUsedFiles() {
		return StringUtilities.tokenize(
			thePreferences.getPreference(BMPreferences.PREF_RECENTLY_USED_FILES),
			"\n"
		);
	}
	
	public static void addRecentlyUsedFile(String file) {
		thePreferences.addRecentlyUsedFile(file);
	}
	
	public static void clearRecentlyUsedFileList() {
		thePreferences.clearRecentlyUsedFileList();
	}
	
}
