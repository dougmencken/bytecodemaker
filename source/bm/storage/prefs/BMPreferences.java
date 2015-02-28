// ===========================================================================
//	BMPreferences.java (part of douglas.mencken.bm.storage.prefs package)
// ===========================================================================

package douglas.mencken.bm.storage.prefs;

import java.util.*;
import java.io.*;

import douglas.mencken.io.ByteArrayIStream;
import douglas.mencken.io.ByteArrayOStream;
import douglas.mencken.io.AutoTypeCreatorFixer;
import douglas.mencken.util.ArrayUtilities;
import douglas.mencken.util.FileUtilities;
import douglas.mencken.util.StringUtilities;
import douglas.mencken.util.Preferences;
import douglas.mencken.tools.UsefulModalDialogs;

/**
 *	<code>BMPreferences</code>
 *	(local to package)
 *	
 *	@version 2.05v
 */

final class BMPreferences extends Preferences {
	
	private static final String MESSAGE_OLD_PREFERENCES =
			"The preferences file belongs to another version of Bytecode Maker.";
	private static final String MESSAGE_DEFAULT_VALUES = "The default values are used.";
	
	// --------------------------------------------------------------------------------------
	
	/** The default preferences */
	private static final Preferences DEFAULT_PREFERENCES;
	
	static {
		DEFAULT_PREFERENCES = new Preferences();
		
		// general preferences
		DEFAULT_PREFERENCES.put("douglas.mencken.bm.branch_display_mode", "2");
		DEFAULT_PREFERENCES.put("douglas.mencken.bm.show_progress_bar", "true");
		DEFAULT_PREFERENCES.put("douglas.mencken.bm.show_memory_monitor", "false");
		DEFAULT_PREFERENCES.put("douglas.mencken.bm.show_log", "true");
		DEFAULT_PREFERENCES.put("douglas.mencken.bm.always_use_fully_qualified_names", "true");
		
		// decompiler preferences
		DEFAULT_PREFERENCES.put("douglas.mencken.bm.use_package_import", "false");
		DEFAULT_PREFERENCES.put("douglas.mencken.bm.java_sun_packages", "true");
		DEFAULT_PREFERENCES.put("douglas.mencken.bm.more_than_3_imports", "false");
		
		// save preferences
		DEFAULT_PREFERENCES.put("douglas.mencken.bm.class_type", "Clss");
		
		// recent used files
		DEFAULT_PREFERENCES.put("douglas.mencken.bm.recent_used_files", "");
	}
	
	// --------------------------------------------------------------------------------------
	
	private static final int DISPLAY_PACKAGES_java_sun = 2;
	private static final int DISPLAY_PACKAGES_IF_MORE_THAN_3 = 4;
	
	private static final int MAGIC = 0xFF84834C;
	private static final long FORMAT_VERSION = /*0.6.0 1.1.6*/ 60116L;
	
	private static final int MAX_RECENT_USED_FILES = 10;
	
	// --------------------------------------------------------------------------------------
	
	/**
	 *	The constructor.
	 */
	public BMPreferences() {
		super(DEFAULT_PREFERENCES);
		super.setFormatVersion(FORMAT_VERSION);
		super.setPreferencesFileName("Bytecode Maker Prefs");
	}
	
	public synchronized void load() {
		try {
			File prefsFile = super.getPreferencesFile();
			if (prefsFile.canRead()) {
				this.readPreferences(
					new ByteArrayIStream(FileUtilities.getByteArray(prefsFile))
				);
			}
		} catch (IOException ioe) {
			super.resetToDefaults();
			this.save();
		}
		
		super.appendToSystemProperties();
	}
	
	/**
	 *	Reads a preferences from an input stream 'in'. 
	 */
	public synchronized void load(InputStream in) throws IOException {
		this.readPreferences(
			new ByteArrayIStream(FileUtilities.getByteArray(in))
		);
		super.appendToSystemProperties();
	}
	
	private void readPreferences(ByteArrayIStream is) throws IOException {
		if (/* int */ is.readInt() == MAGIC) {
			if (/* long */ is.readLong() == super.getFormatVersion()) {
				// general preferences
				String classType = /* UTF */ is.readUTF();
				if (classType.length() != 4) throw new IOException("bad format");
				super.set("douglas.mencken.bm.class_type", classType);
				
				String branchDisplayMode = String.valueOf(/* unsigned byte */ is.read());
				super.set("douglas.mencken.bm.branch_display_mode", branchDisplayMode);
				
				int showProgressBar = /* unsigned byte */ is.read();
				super.set(
					"douglas.mencken.bm.show_progress_bar",
					(showProgressBar == 0) ? "false" : "true"
				);
				
				int showMemoryMonitor = /* unsigned byte */ is.read();
				super.set(
					"douglas.mencken.bm.show_memory_monitor",
					(showMemoryMonitor == 0) ? "false" : "true"
				);
				
				int showLog = /* unsigned byte */ is.read();
				super.set(
					"douglas.mencken.bm.show_log",
					(showLog == 0) ? "false" : "true"
				);
				
				String useFullyQualifiedNames = "false";
				if (/* unsigned byte */ is.read() == 1) {
					useFullyQualifiedNames = "true";
				}
				super.set("douglas.mencken.bm.always_use_fully_qualified_names", useFullyQualifiedNames);
				
				// decompiler preferences
				int usePackageImport = /* unsigned byte */ is.read();
				super.set(
					"douglas.mencken.bm.use_package_import",
					(usePackageImport == 0) ? "false" : "true"
				);
				
				int packageImportFilter = /* unsigned byte */ is.read();
				boolean java_sun_packages = (packageImportFilter & DISPLAY_PACKAGES_java_sun) != 0;
				super.set("douglas.mencken.bm.java_sun_packages", (java_sun_packages) ? "true" : "false");
				
				boolean more_than_3_imports =
							(packageImportFilter & DISPLAY_PACKAGES_IF_MORE_THAN_3) != 0;
				super.set(
					"douglas.mencken.bm.more_than_3_imports",
					(more_than_3_imports) ? "true" : "false"
				);
				
				// recent used files
				int recentUsedFileCount = is.readByte();
				if (recentUsedFileCount > 0) {
					String[] recentUsedFiles = new String[recentUsedFileCount];
					for (int i = 0; i < recentUsedFileCount;  i++) {
						recentUsedFiles[i] = /* UTF */ is.readUTF();
					}
					
					recentUsedFiles = FileUtilities.checkAndFilterFileList(
						recentUsedFiles, MAX_RECENT_USED_FILES
					);
					
					if (recentUsedFiles != null) {
						super.set(
							"douglas.mencken.bm.recent_used_files",
							StringUtilities.stringArrayToString(recentUsedFiles, "\n")
						);
						return;
					}
				}
				
				// 'recentUsedFileCount <= 0' or 'recentUsedFiles == null'
				super.set("douglas.mencken.bm.recent_used_files", "");
			} else {
				UsefulModalDialogs.doErrorDialog(
					MESSAGE_OLD_PREFERENCES + ' ' + MESSAGE_DEFAULT_VALUES
				);
				throw new IOException("format version mismatch");
			}
		} else {
			throw new IOException("magic number mismatch");
		}
	}
	
	public synchronized void save() {
		ByteArrayOStream os = this.writePreferences();
		AutoTypeCreatorFixer.getCurrentFixer().add(super.getPreferencesFile(), "pref", "ByTe");
		FileUtilities.writeBytesToFile(os.toByteArray(), super.getPreferencesFile());
	}
	
    /**
     *	Stores the preferences to the specified output stream. The 
	 *	'String header' is ignored here.
     */
	public synchronized void save(OutputStream out, String header) {
		ByteArrayOStream os = this.writePreferences();
		
		try {
			out.write(os.toByteArray());
		} catch (IOException ioe) {}
	}
	
	private ByteArrayOStream writePreferences() {
		ByteArrayOStream os = new ByteArrayOStream();
		
		try {
			os.writeInt(/* int */ MAGIC);
			os.writeLong(/* long */ super.getFormatVersion());
			
			// general preferences
			os.writeUTF(/* UTF */ (String)super.get("douglas.mencken.bm.class_type"));
			os.write(/* byte */ Integer.parseInt(
				(String)super.get("douglas.mencken.bm.branch_display_mode")
			));
			
			os.write(/* byte */
				(super.get("douglas.mencken.bm.show_progress_bar").equals("true")) ? 1 : 0
			);
			os.write(/* byte */
				(super.get("douglas.mencken.bm.show_memory_monitor").equals("true")) ? 1 : 0
			);
			os.write(/* byte */
				(super.get("douglas.mencken.bm.show_log").equals("true")) ? 1 : 0
			);
			
			os.write(/* byte */
				(super.get("douglas.mencken.bm.always_use_fully_qualified_names").equals("true")) ? 1 : 0
			);
			
			// decompiler preferences
			int usePackageImport = 0;
			if (super.get("douglas.mencken.bm.use_package_import").equals("true")) {
				usePackageImport = 1;
			}
			os.write(/* byte */ usePackageImport);
			
			int packageImportFilter = 0;
			if (super.get("douglas.mencken.bm.java_sun_packages").equals("true")) {
				packageImportFilter = packageImportFilter^DISPLAY_PACKAGES_java_sun;
			}
			if (super.get("douglas.mencken.bm.more_than_3_imports").equals("true")) {
				packageImportFilter = packageImportFilter^DISPLAY_PACKAGES_IF_MORE_THAN_3;
			}
			os.write(/* byte */ packageImportFilter);
			
			// recent used files
			String recentUsedFiles = (String)super.get("douglas.mencken.bm.recent_used_files");
			if (recentUsedFiles.length() != 0) {
				String[] files = StringUtilities.tokenize(recentUsedFiles, "\n");
				int count = files.length;
				os.writeByte(/* byte */ count);
				
				for (int i = 0; i < count; i++) {
					os.writeUTF(/* UTF */ files[i]);
				}
			} else {
				os.writeByte(/* byte */ -1);
			}
		} catch (UTFDataFormatException ignored) {}
		
		return os;
	}
	
	/**
	 *	Set the preference, append the list to the system properties,
	 *	and save.
	 */
	public void set(String preference, String value) {
		super.set(preference, value);
		super.appendToSystemProperties();
		this.save();
	}
	
	/**
	 *	Add a new recent used file to the list.
	 */
	public void addRecentUsedFile(String newRecent) {
		// ignore 'null' and 'empty' ("") strings
		if ((newRecent == null) || (newRecent.length() == 0)) return;
		
		String recentUsedFiles = (String)super.get("douglas.mencken.bm.recent_used_files");
		
		if (recentUsedFiles.length() == 0) {
			this.set("douglas.mencken.bm.recent_used_files", newRecent);
			return;
		}
		
		String[] files = StringUtilities.tokenize(recentUsedFiles, "\n");
		int count = files.length;
		
		for (int i = 0; i < count; i++) {
			if (files[i].equals(newRecent)) {
				// move it to the top
				files = ArrayUtilities.move(files, i, count-1);
				
				this.set(
					"douglas.mencken.bm.recent_used_files",
					StringUtilities.stringArrayToString(files, "\n")
				);
				return;
			}
		}
		
		if (count < MAX_RECENT_USED_FILES) {
			this.set(
				"douglas.mencken.bm.recent_used_files",
				recentUsedFiles + '\n' + newRecent
			);
		} else if (count == MAX_RECENT_USED_FILES) {
			String withoutFirst = StringUtilities.getAfterFirst(recentUsedFiles, '\n');
			this.set(
				"douglas.mencken.bm.recent_used_files",
				withoutFirst + '\n' + newRecent
			);
		}
	}
	
	public void clearRecentUsedFileList() {
		this.set("douglas.mencken.bm.recent_used_files", "");
	}
	
}
