/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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
import douglas.mencken.tools.UsefulMessageDialogs;

/**
 *	<code>BMPreferences</code>
 *	(local to package)
 *	
 *	@version 2.1
 */

final class BMPreferences extends Preferences {
	
	private static final String MESSAGE_OLD_PREFERENCES =
			"The preferences file belongs to another version of Bytecode Maker.";
	private static final String MESSAGE_DEFAULT_VALUES = "The default values are used.";
	
	// --------------------------------------------------------------------------------------

	static final String PREF_BRANCH_DISPLAY_MODE	= "bytecode.maker.branch_display_mode";
	static final String PREF_SHOW_PROGRESS_BAR	= "bytecode.maker.show_progress_bar";
	static final String PREF_SHOW_MEMORY_MONITOR	= "bytecode.maker.show_memory_monitor";
	static final String PREF_SHOW_LOG_WINDOW	= "bytecode.maker.show_log";
	static final String PREF_QUALIFIED_NAMES_ONLY	= "bytecode.maker.use_fully_qualified_names";

	static final String PREF_USE_PACKAGE_IMPORT	= "bytecode.maker.use_package_import";
	static final String PREF_JAVA_SUN_PACKAGES	= "bytecode.maker.java_sun_packages";
	static final String PREF_MORE_THAN_3_IMPORTS	= "bytecode.maker.more_than_3_imports";

	static final String PREF_SAVE_CLASS_TYPE	= "bytecode.maker.class_type";

	static final String PREF_RECENTLY_USED_FILES	= "bytecode.maker.recently_used_files";

	// --------------------------------------------------------------------------------------
	
	/** The default preferences */
	private static final Preferences DEFAULT_PREFERENCES;
	
	static {
		DEFAULT_PREFERENCES = new Preferences();
		
		// general preferences
		DEFAULT_PREFERENCES.put(PREF_BRANCH_DISPLAY_MODE, "2");
		DEFAULT_PREFERENCES.put(PREF_SHOW_PROGRESS_BAR, "true");
		DEFAULT_PREFERENCES.put(PREF_SHOW_MEMORY_MONITOR, "false");
		DEFAULT_PREFERENCES.put(PREF_SHOW_LOG_WINDOW, "true");
		DEFAULT_PREFERENCES.put(PREF_QUALIFIED_NAMES_ONLY, "true");
		
		// decompiler preferences
		DEFAULT_PREFERENCES.put(PREF_USE_PACKAGE_IMPORT, "false");
		DEFAULT_PREFERENCES.put(PREF_JAVA_SUN_PACKAGES, "true");
		DEFAULT_PREFERENCES.put(PREF_MORE_THAN_3_IMPORTS, "false");
		
		// save preferences
		DEFAULT_PREFERENCES.put(PREF_SAVE_CLASS_TYPE, "Clss");
		
		// recently used files
		DEFAULT_PREFERENCES.put(PREF_RECENTLY_USED_FILES, "");
	}
	
	// --------------------------------------------------------------------------------------
	
	private static final int DISPLAY_PACKAGES_java_sun = 2;
	private static final int DISPLAY_PACKAGES_IF_MORE_THAN_3 = 4;
	
	private static final int MAGIC = 0xFF84834C;
	private static final long FORMAT_VERSION = /*A.6 0 1.1.7*/ 160117L;
	
	private static final int MAX_RECENTLY_USED_FILES = 10;
	
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
				super.setPreference(PREF_SAVE_CLASS_TYPE, classType);
				
				String branchDisplayMode = String.valueOf(/* unsigned byte */ is.read());
				super.setPreference(PREF_BRANCH_DISPLAY_MODE, branchDisplayMode);
				
				int showProgressBar = /* unsigned byte */ is.read();
				super.setPreference(
					PREF_SHOW_PROGRESS_BAR,
					(showProgressBar == 0) ? "false" : "true"
				);
				
				int showMemoryMonitor = /* unsigned byte */ is.read();
				super.setPreference(
					PREF_SHOW_MEMORY_MONITOR,
					(showMemoryMonitor == 0) ? "false" : "true"
				);
				
				int showLog = /* unsigned byte */ is.read();
				super.setPreference(
					PREF_SHOW_LOG_WINDOW,
					(showLog == 0) ? "false" : "true"
				);
				
				String useFullyQualifiedNames = "false";
				if (/* unsigned byte */ is.read() == 1) {
					useFullyQualifiedNames = "true";
				}
				super.setPreference(PREF_QUALIFIED_NAMES_ONLY, useFullyQualifiedNames);
				
				// decompiler preferences
				int usePackageImport = /* unsigned byte */ is.read();
				super.setPreference(
					PREF_USE_PACKAGE_IMPORT,
					(usePackageImport == 0) ? "false" : "true"
				);
				
				int packageImportFilter = /* unsigned byte */ is.read();
				boolean java_sun_packages = (packageImportFilter & DISPLAY_PACKAGES_java_sun) != 0;
				super.setPreference(PREF_JAVA_SUN_PACKAGES, (java_sun_packages) ? "true" : "false");
				
				boolean more_than_3_imports =
							(packageImportFilter & DISPLAY_PACKAGES_IF_MORE_THAN_3) != 0;
				super.setPreference(
					PREF_MORE_THAN_3_IMPORTS,
					(more_than_3_imports) ? "true" : "false"
				);
				
				// recent used files
				int recentlyUsedFileCount = is.readByte();
				if (recentlyUsedFileCount > 0) {
					String[] recentlyUsedFiles = new String[recentlyUsedFileCount];
					for (int i = 0; i < recentlyUsedFileCount;  i++) {
						recentlyUsedFiles[i] = /* UTF */ is.readUTF();
					}
					
					recentlyUsedFiles = FileUtilities.checkAndFilterFileList(
						recentlyUsedFiles, MAX_RECENTLY_USED_FILES
					);
					
					if (recentlyUsedFiles != null) {
						super.setPreference(
							PREF_RECENTLY_USED_FILES,
							StringUtilities.stringArrayToString(recentlyUsedFiles, "\n")
						);
						return;
					}
				}
				
				// 'recentlyUsedFileCount <= 0' or 'recentlyUsedFiles == null'
				super.setPreference(PREF_RECENTLY_USED_FILES, "");
			} else {
				UsefulMessageDialogs.doErrorDialog(
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
			os.writeUTF(/* UTF */ (String)super.getPreference(PREF_SAVE_CLASS_TYPE));
			os.write(/* byte */ Integer.parseInt(
				(String)super.getPreference(PREF_BRANCH_DISPLAY_MODE)
			));
			
			os.write(/* byte */
				(super.getPreference(PREF_SHOW_PROGRESS_BAR).equals("true")) ? 1 : 0
			);
			os.write(/* byte */
				(super.getPreference(PREF_SHOW_MEMORY_MONITOR).equals("true")) ? 1 : 0
			);
			os.write(/* byte */
				(super.getPreference(PREF_SHOW_LOG_WINDOW).equals("true")) ? 1 : 0
			);
			
			os.write(/* byte */
				(super.getPreference(PREF_QUALIFIED_NAMES_ONLY).equals("true")) ? 1 : 0
			);
			
			// decompiler preferences
			int usePackageImport = 0;
			if (super.getPreference(PREF_USE_PACKAGE_IMPORT).equals("true")) {
				usePackageImport = 1;
			}
			os.write(/* byte */ usePackageImport);
			
			int packageImportFilter = 0;
			if (super.getPreference(PREF_JAVA_SUN_PACKAGES).equals("true")) {
				packageImportFilter = packageImportFilter^DISPLAY_PACKAGES_java_sun;
			}
			if (super.getPreference(PREF_MORE_THAN_3_IMPORTS).equals("true")) {
				packageImportFilter = packageImportFilter^DISPLAY_PACKAGES_IF_MORE_THAN_3;
			}
			os.write(/* byte */ packageImportFilter);
			
			// recent used files
			String recentlyUsedFiles = (String)super.getPreference(PREF_RECENTLY_USED_FILES);
			if (recentlyUsedFiles.length() != 0) {
				String[] files = StringUtilities.tokenize(recentlyUsedFiles, "\n");
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
	public void setPreference(String preference, String value) {
		super.setPreference(preference, value);
		super.appendToSystemProperties();
		this.save();
	}
	
	/**
	 *	Add a new recent used file to the list.
	 */
	public void addRecentlyUsedFile(String newRecent) {
		// ignore 'null' and 'empty' ("") strings
		if ((newRecent == null) || (newRecent.length() == 0)) return;
		
		String recentlyUsedFiles = super.getPreference(PREF_RECENTLY_USED_FILES);
		
		if (recentlyUsedFiles.length() == 0) {
			this.setPreference(PREF_RECENTLY_USED_FILES, newRecent);
			return;
		}
		
		String[] files = StringUtilities.tokenize(recentlyUsedFiles, "\n");
		int count = files.length;
		
		for (int i = 0; i < count; i++) {
			if (files[i].equals(newRecent)) {
				// move it to the top
				files = ArrayUtilities.move(files, i, count-1);
				
				this.setPreference(
					PREF_RECENTLY_USED_FILES,
					StringUtilities.stringArrayToString(files, "\n")
				);
				return;
			}
		}
		
		if (count < MAX_RECENTLY_USED_FILES) {
			this.setPreference(
				PREF_RECENTLY_USED_FILES,
				recentlyUsedFiles + '\n' + newRecent
			);
		} else if (count == MAX_RECENTLY_USED_FILES) {
			String withoutFirst = StringUtilities.getAfterFirst(recentlyUsedFiles, '\n');
			this.setPreference(
				PREF_RECENTLY_USED_FILES,
				withoutFirst + '\n' + newRecent
			);
		}
	}
	
	public void clearRecentlyUsedFileList() {
		this.setPreference(PREF_RECENTLY_USED_FILES, "");
	}
	
}
