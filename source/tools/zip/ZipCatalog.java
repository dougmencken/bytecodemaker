// ===========================================================================
//	ZipCatalog.java (part of douglas.mencken.tools.zip package)
// ===========================================================================

package douglas.mencken.tools.zip;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 *	<code>ZipCatalog</code>
 *
 *	@version 1.05f
 */

public class ZipCatalog extends Object {
	
	private ZipFile file;
	private ZipEntry[] entries;
	private String[] names;
	
	public ZipCatalog(File zipFile) {
		this(zipFile.getPath());
	}
	
	public ZipCatalog(String name) {
		try {
			this.file = new ZipFile(name);
			Enumeration e = file.entries();
			
			Vector v = new Vector();
			while (e.hasMoreElements()) {
				v.addElement(e.nextElement());
			}
			
			int vsize = v.size();
			this.entries = new ZipEntry[vsize];
			this.names = new String[vsize];
			for (int i = 0; i < vsize; i++) {
				entries[i] = (ZipEntry) v.elementAt(i);
				names[i] = entries[i].getName();
			}
		} catch (IOException e) {}
	}
	
	public ZipEntry[] getEntries() { return this.entries; }
	public String[] getNames() { return this.names; }
	
	public InputStream getInputStream(ZipEntry entry) throws IOException {
		return (entry.isDirectory()) ? null : this.file.getInputStream(entry);
	}
	
	public ZipEntry findEntryByName(String name) {
		int len = this.entries.length;
		for (int i = 0; i < len; i++) {
			if (this.entries[i].getName().equals(name)) {
				return this.entries[i];
			}
		}
		
		return null;
	}
	
}