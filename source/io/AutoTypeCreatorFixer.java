// ===========================================================================
//	AutoTypeCreatorFixer.java (part of douglas.mencken.io package)
// ===========================================================================

package douglas.mencken.io;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import com.apple.mrj.MRJOSType;
import com.apple.mrj.MRJFileUtils;

/**
 *	<code>AutoTypeCreatorFixer</code>
 *
 *	@version 1.0f3
 */

public class AutoTypeCreatorFixer extends Thread {
	
	/**
	 *	The hashtable containing java.io.File objects as keys,
	 *	and java.lang.String objects (like "TEXTttxt") as values.
	 */
	protected Hashtable queue = new Hashtable();
	
	private static AutoTypeCreatorFixer currentFixer;
	static {
		new AutoTypeCreatorFixer();
	}
	
	/**
	 *	Default constructor.
	 */
	public AutoTypeCreatorFixer() {
		super("Thread-AutoTypeCreatorFixer");
		super.setPriority(Thread.NORM_PRIORITY - 1);
		super.setDaemon(true);
		
		if (currentFixer != null) {
			currentFixer.stop();
		}
		currentFixer = this;
		
		super.start();
	}
	
	public void add(File file, String type, String creator) {
		if ((type.length() != 4) || (creator.length() != 4)) {
			throw new IllegalArgumentException();
		}
		
		synchronized (this.queue) {
			queue.put(file, (new StringBuffer(type)).append(creator).toString());
		}
	}
	
	public void remove(File file) {
		synchronized (this.queue) {
			queue.remove(file);
		}
	}
	
	public void run() {
		while (true) {
			if (!queue.isEmpty()) {
				Enumeration files = this.queue.keys();
				while (files.hasMoreElements()) {
					File file = (File)files.nextElement();
					if (file.exists()) {
						String typeAndCreator = (String)this.queue.get(file);
						String type = typeAndCreator.substring(0, 4);
						String creator = typeAndCreator.substring(4, 8);
						
						try {
							MRJFileUtils.setFileTypeAndCreator(
								file,
								new MRJOSType(type),
								new MRJOSType(creator)
							);
							this.queue.remove(file);
						} catch (IOException ioe) {}
					}
				}
			}
		}
	}
	
	public static AutoTypeCreatorFixer getCurrentFixer() {
		return currentFixer;
	}
	
}