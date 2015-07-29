/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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
