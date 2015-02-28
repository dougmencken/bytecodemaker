// ===========================================================================
//	Preferences.java (part of douglas.mencken.util package)
// ===========================================================================

package douglas.mencken.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Properties;

/**
 *	The <code>Preferences</code> class represents a persistent set of 
 *	an application preferences. The <code>Preferences</code> can be saved to 
 *	and loaded from a special file in Preferences folder (Mac OS) or in 
 *	application folder (other platforms).
 *	
 *	A preference list can contain another preference list as its 
 *	"defaults".
 *
 *	@version 1.01f
 */

public class Preferences extends Properties {
	
	/** The version of the preferences file format. */
	private long formatVersion = /* 1.0.0 */ 100L;
	
	public static final File PREFERENCES_FOLDER;
	public static final Properties SYSTEM_PROPERTIES;
	
	static {
		if (System.getProperty("os.name").equals("Mac OS")) {
			File prefsFolder = null;
			try {
				prefsFolder = com.apple.mrj.MRJFileUtils.findFolder(
					com.apple.mrj.MRJFileUtils.kPreferencesFolderType
				);
			} catch (FileNotFoundException fnf) {}
			
			PREFERENCES_FOLDER = prefsFolder;
		} else {
			PREFERENCES_FOLDER = new File(System.getProperty("user.dir"));
		}
		
		SYSTEM_PROPERTIES = System.getProperties();
	}
	
	/** The name of the preferences file. */
	private String preferencesFileName = "preferences";
	
	/**
	 *	Creates an empty preference list with no default values.
	 */
	public Preferences() {
		this(null);
	}
	
	/**
	 *	Creates an empty preference list with the specified defaults.
	 */
	public Preferences(Preferences defaults) {
		super(defaults);
	}
	
	/**
	 *	Reset all preferences to its default values.
	 */
	public void resetToDefaults() {
		Preferences defaults = (Preferences)super.defaults;
		
		if (defaults != null) {
			super.clear();
			Enumeration keys = defaults.keys();
			
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				super.put(key, defaults.get(key));
			}
		}
	}
	
	public void appendToSystemProperties() {
		System.setProperties(concat(SYSTEM_PROPERTIES, this));
	}
	
	/**
	 *	Sets the preference to the specified value.
	 */
	public void set(String preference, String value) {
		super.put(preference, value);
	}
	
	/**
	 *	@return		the preferences file in the preferences folder.
	 */
	public File getPreferencesFile() {
		return new File(PREFERENCES_FOLDER, this.preferencesFileName);
	}
	
	public void setFormatVersion(long version) {
		this.formatVersion = version;
	}
	
	public long getFormatVersion() {
		return this.formatVersion;
	}
	
	public void setPreferencesFileName(String name) {
		this.preferencesFileName = name;
	}
	
	public String getPreferencesFileName() {
		return this.preferencesFileName;
	}
	
	/**
	 *	Concatenates two sets of properties into a single one.
	 *	(static method)
	 */
	public static final Properties concat(Properties p1, Properties p2) {
		Properties pp = new Properties();
		
		{
			Enumeration e = p1.keys();
			while (e.hasMoreElements()) {
				String key = (String)e.nextElement();
				pp.put(key, p1.get(key));
			}
		}
		
		{
			Enumeration e = p2.keys();
			while (e.hasMoreElements()) {
				String key = (String)e.nextElement();
				pp.put(key, p2.get(key));
			}
		}
		
		return pp;
	}
	
}