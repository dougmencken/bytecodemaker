// ===========================================================================
//	Log.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.util.Vector;
import douglas.mencken.util.ArrayUtilities;
import douglas.mencken.util.InvisibleFrame;

/**
 *	<code>Log</code>
 *
 *	@version 1.01f
 */

public class Log extends Object {
	
	protected Vector records = new Vector();
	private boolean changed;
	
	/**
	 *	Initializes a new <code>Log</code> object.
	 */
	public Log() { super(); }
	
	public void addRecord(String record) {
		this.changed = true;
		this.records.addElement(record);
	}
	
	public String[] getRecords() {
		return ArrayUtilities.vectorToStringArray(this.records);
	}
	
	public boolean isEmpty() {
		return this.records.isEmpty();
	}
	
	public void clear() {
		this.changed = true;
		this.records.removeAllElements();
	}
	
	public boolean isChanged() {
		return this.changed;
	}
	
	public void setChanged(boolean newChanged) {
		this.changed = newChanged;
	}
	
}
