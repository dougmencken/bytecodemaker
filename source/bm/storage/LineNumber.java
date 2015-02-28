// ===========================================================================
//	LineNumber.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

import java.io.*;

/**
 *	<code>LineNumber</code>
 *
 *	@version 2.0d0
 */

public class LineNumber extends Object
implements JavaMethodIndexedMember, Externalizable {
	
	protected JavaMethod ownerMethod = null;
	protected int index = -1;
	
	/**
	 *	Writes line number to the ObjectOutput.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		// ...
	}
	
	/**
	 *	Reads line number from the ObjectInput.
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// ...
	}
	
	public JavaMethod getOwnerMethod() {
		return this.ownerMethod;
	}
	
	public void setOwnerMethod(JavaMethod owner) {
		// ...
		this.ownerMethod = owner;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public void setIndex(int index) {
		// ...
	}
	
	public void setOwnerMethod(JavaMethod owner, int index) {
		// ...
		this.ownerMethod = owner;
		this.index = index;
	}
	
	// ...
	
}
