/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.storage;

/**
 *	<code>JavaClassIndexedMember</code>
 *
 *	@version	0.7d0
 *	@since		Bytecode Maker 0.6.0
 */

public interface JavaClassIndexedMember extends JavaClassMember {
	
	public int getIndex();
	public void setIndex(int index);
	public void setOwnerClass(JavaClass owner, int index);
	
}
