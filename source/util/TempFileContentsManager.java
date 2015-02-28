// ===========================================================================
//	TempFileContentsManager.java (part of douglas.mencken.util package)
// ===========================================================================

package douglas.mencken.util;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Enumeration;
import netscape.util.Sort;

/**
 *	<code>TempFileContentsManager</code><br>
 *	Example of use:<br>
 *	<pre>
 *	TempFileContentsManager current = TempFileContentsManager.getCurrentManager();
 *	current.clear();
 *	
 *	if ((objects != null) && (objects.length > 0)) {
 *		int maxNumber = objects.length;
 *		int maxNumberLength = String.valueOf(maxNumber).length();
 *		
 *		for (int i = 0; i <= maxNumber; i++) {
 *			String number = StringUtilities.toPretabbedString(i, maxNumberLength);
 *			current.addObject(objects[i], "object " + number);
 *		}
 *	}
 *	</pre>
 *
 *	@version 1.23f1
 */

public class TempFileContentsManager extends Object {
	
	protected Hashtable nameOffsetTable = new Hashtable();
	protected transient TemporaryFileOnDisk file;
	
	private static TempFileContentsManager currentManager;
	private static TempFileContentsManager specialPurposeManager;
	
	/**
	 *	Creates a new TempFileContentsManager.
	 */
	public TempFileContentsManager(String folderName, String fileName) {
		super();
		this.file = new TemporaryFileOnDisk(folderName, fileName);
	}
	
	public TempFileContentsManager(String folderName) {
		this(folderName, "Temporary Data");
	}
	
	public void setFileTypeAndCreator(String type, String creator) {
		this.file.setFileTypeAndCreator(type, creator);
	}
	
	/**
	 *	Clears the temporary file.
	 */
	public void clear() {
		this.nameOffsetTable.clear();
		this.file.reset();
	}
	
	public File getEnclosingFolder() {
		return new File(this.file.getFolder());
	}
	
	public void replaceObject(Object obj, String name) throws IOException {
		if (!nameOffsetTable.containsKey(name)) {
			return;
		}
		
		int count = this.getEntryCount();
		int matchIndex = -1;
		
		Object[] objects = new Object[count];
		String[] names = this.names();
		
		for (int i = 0; i < count; i++) {
			String currentName = names[i];
			if (currentName.equals(name)) {
				matchIndex = i;
			}
			
			objects[i] = this.getObject(currentName);
		}
		
		boolean success = true;
		
		if (matchIndex >= 0) {
			this.clear();
			
			for (int i = 0; i < matchIndex; i++) {
				if (!this.addObject(objects[i], names[i])) {
					success = false;
				}
			}
			
			if (!this.addObject(obj, name)) {
				success = false;
			}
			
			for (int i = matchIndex+1; i < count; i++) {
				if (!this.addObject(objects[i], names[i])) {
					success = false;
				}
			}
		}
		
		if (!success) {
			throw new IOException("Can't replace object");
		}
	}
	
	/**
	 *	@return		<code>true</code> if the 'Object obj' successfully added;
	 *				<code>false</code> otherwise.
	 */
	public boolean addObject(Object obj, String name) {
		boolean success = false;
		
		if (!nameOffsetTable.containsKey(name)) {
			nameOffsetTable.put(name, new Long(this.file.getFileLength()));
			
			try {
				file.writeObject(obj);
				success = true;
			} catch (IOException ioe) {}
		}
		
		return success;
	}
	
	/**
	 *	Note:	'null' is never returned on errors. But always check the result
	 *			with 'instanceof'. The <code>java.lang.String</code> message is used
	 *			to notify about unpredictable situations.
	 */
	public Object getObject(String name) {
		Object offsetObj = nameOffsetTable.get(name);
		if (offsetObj == null) {
			return "key \"" + name + "\" is not mapped to any value in the hashtable";
		}
		
		long offset = ((Long)offsetObj).longValue();
		
		try {
			this.file.skip(offset);
			return file.readObject();
		} catch (IOException ioe) {
			return "can't resolve object \"" + name + "\" (" +
						StringUtilities.makeExceptionCaughtMessage(ioe, /* true */ false) + ')';
		} catch (ClassNotFoundException cnfe) {
			return "can't resolve object \"" + name + "\" (" +
						StringUtilities.makeExceptionCaughtMessage(cnfe, /* true */ false) + ')';
		}
	}
	
	public Object getObject(int index) {
		return this.getObject(this.names()[index]);
	}
	
	/**
	 *	@return		a string array of the names in the name-offset table
	 *				sorted by alphabetical order.
	 */
	public String[] names() {
		Enumeration en = this.nameOffsetTable.keys();
		int size = this.nameOffsetTable.size();
		
		String[] names = new String[size];
		for (int i = 0; i < size; i++) {
			names[i] = (String)en.nextElement();
		}
		
		Sort.sortStrings(names, 0, size, true, false);
		return names;
	}
	
	public int getEntryCount() {
		return this.nameOffsetTable.size();
	}
	
	/**
	 *	(static method)
	 */
	public static final TempFileContentsManager getCurrentManager() {
		if (currentManager == null) {
			currentManager = new TempFileContentsManager(
				(new File(System.getProperty("user.dir"), "Temporary Files")).getPath(),
				"Temporary File"
			);
		}
		
		return currentManager;
	}
	
	/**
	 *	(static method)
	 */
	public static final void setCurrentManager(TempFileContentsManager manager) {
		currentManager = manager;
	}
	
	/**
	 *	(static method)
	 */
	public static final TempFileContentsManager getSpecialPurposeManager() {
		if (specialPurposeManager == null) {
			specialPurposeManager = new TempFileContentsManager(
				(new File(System.getProperty("user.dir"), "Temporary Files")).getPath(),
				"Temporary File 0"
			);
		}
		
		return specialPurposeManager;
	}
	
	/**
	 *	(static method)
	 */
	public static final void setSpecialPurposeManager(TempFileContentsManager manager) {
		specialPurposeManager = manager;
	}
	
	/**
	 *	(static method)
	 */
	public static boolean cleanUp() {
		File folder = new File(System.getProperty("user.dir"), "Temporary Files");
		
		if (currentManager != null) {
			folder = currentManager.getEnclosingFolder();
		}
		if (specialPurposeManager != null) {
			if (currentManager == null) {
				folder = specialPurposeManager.getEnclosingFolder();
			}
		}
		
		return FileUtilities.clearAndRemoveFolder(folder);
	}
	
}
