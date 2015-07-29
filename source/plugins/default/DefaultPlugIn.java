/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.plugins;

/**
 *	<code>DefaultPlugIn</code>
 *	
 *	@version	1.01f
 *	@since		Bytecode Maker 0.5.9
 */

public abstract class DefaultPlugIn extends Object {
	
	public abstract void plugin();
	
	public abstract String getPlugInName();
	
	public final String getGroupName() {
		return "default";
	}
	
	protected DefaultPlugIn() { super(); }
	
}
