/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.plugins;

/**
 *	<code>RemoveStringDecryptMethod</code>
 *
 *	@version 0.5d0
 */

public class RemoveStringDecryptMethod extends Object {
	
	public RemoveStringDecryptMethod() {
		super();
	}
	
	public void plugin() {
		// ...
		java.awt.Toolkit.getDefaultToolkit().beep();
	}
	
	public String getPlugInName() {
		return "Remove String Decrypt Method";
	}
	
	public String getGroupName() {
		return "Deobfuscator";
	}
	
}
